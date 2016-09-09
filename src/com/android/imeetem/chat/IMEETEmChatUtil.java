/**
 * 
 */
package com.android.imeetem.chat;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import android.util.Log;

import com.android.imeetem.util.IMEETEmConstants;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmChatUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "IMEETEmChatActivity";

	public static IMEETEmChatUtil getInstance() {
		return new IMEETEmChatUtil();
	}

	private JmsConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;

	private IMEETEmDispatchQueue dispatchQueue;

	private HashMap<String, ArrayDeque<MessageConsumer>> consumers = new HashMap<String, ArrayDeque<MessageConsumer>>();

	public void startChatSession(String gatewayHost, String gatewayPort,
			final String destinationName) {

		System.out
				.println("\n\n\n ***** startChatSession - Starting Chat Session ******* \n\n\n");

		if (connectionFactory == null) {
			try {
				connectionFactory = JmsConnectionFactory
						.createConnectionFactory();

			} catch (JMSException e) {
				e.printStackTrace();
				Log.d(TAG, "EXCEPTION: " + e.getMessage());
			}
		}

		System.out
				.println("\n\n\n ***** startChatSession - Got ConnectionFactory without crashing! ******* \n\n\n");

		// Connect to the Gateway and ActiveMQ in order to send and receive
		// messages
		dispatchQueue = new IMEETEmDispatchQueue("DispatchQueue");
		dispatchQueue.start();
		dispatchQueue.waitUntilReady();
		connect(gatewayHost, gatewayPort);

		System.out
				.println("\n\n\n ***** startChatSession - We Are Connected!!!  ******* \n\n\n");

	}

	public Runnable getMessagesListener(final String destinationName,
			final Session session) {

		System.out
				.println("\n\n\n ***** startChatSession - Subscribe to Queue ******* \n\n\n");
		return new Runnable() {
			public void run() {
				try {

					System.out
							.println("\n\n **** GEtting Destination Name! **** \n\n");
					Destination destination = getDestination(destinationName,
							session);

					System.out
							.println("\n\n\n ***** startChatSession - Listening on Destination Queue: "
									+ destinationName + " ******* \n\n\n");

					if (destination == null) {
						return;
					}

					System.out
							.println("\n\n\n ***** startChatSession - Destination Not NULL ******* \n\n\n");

					MessageConsumer consumer = session
							.createConsumer(destination);
					ArrayDeque<MessageConsumer> consumersToDestination = consumers
							.get(destinationName);
					if (consumersToDestination == null) {
						consumersToDestination = new ArrayDeque<MessageConsumer>();
						consumers.put(destinationName, consumersToDestination);
					}
					consumersToDestination.add(consumer);

					System.out
							.println("\n\n\n ***** startChatSession - Listening on Destination Queue ******* \n\n\n");

					consumer.setMessageListener(new DestinationMessageListener());
				} catch (JMSException e) {
					e.printStackTrace();
					Log.d(TAG, "EXCEPTION: " + e.getMessage());
				}
			}
		};

	}

	public void sendMessage(final String text, final String sendToDestination,
			final Session session) {

		Log.d(TAG, "SENDING MESSAGE: " + text);

		dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {
				try {

					MessageProducer producer = session
							.createProducer(getDestination(sendToDestination,
									session));
					Message message;
					message = session.createTextMessage(text);

					producer.send(message);
					producer.close();
				} catch (JMSException e) {
					e.printStackTrace();
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}

	private void connect(final String gatewayHost, final String gatewayPort) {

		Log.d(TAG, "CONNECTING TO GATEWAY: " + gatewayHost + ":" + gatewayPort);

		// Since createConnection() is a blocking method which will not return
		// until
		// the connection is established or connection fails, it is a good
		// practice to
		// establish connection on a separate thread so that UI is not blocked.
		dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {
				try {
					String location = gatewayHost + ":" + gatewayPort + "/"
							+ IMEETEmConstants.IMEETEM_CHAT_GATEWAY_DOMAIN;

					System.out
							.println("\n\n\n ***** Connecting to Gateway Location: "
									+ location + " ******* \n\n\n");
					connectionFactory.setGatewayLocation(URI.create(location));
					connection = connectionFactory.createConnection();
					connection.start();
					session = connection.createSession(false,
							Session.AUTO_ACKNOWLEDGE);
					Log.d(TAG, "CONNECTED");
					connection
							.setExceptionListener(new ConnectionExceptionListener());
					// updateButtonsForConnected();
				} catch (Exception e) {
					// updateButtonsForDisconnected();
					e.printStackTrace();
					Log.d(TAG, "EXCEPTION: " + e.getMessage());
				}
			}
		});
	}

	private void disconnect() {
		Log.d(TAG, "DISCONNECTING");

		dispatchQueue.removePendingJobs();
		dispatchQueue.quit();
		new Thread(new Runnable() {
			public void run() {
				try {
					connection.close();
					Log.d(TAG, "DISCONNECTED");
				} catch (JMSException e) {
					e.printStackTrace();
					Log.d(TAG, "EXCEPTION: " + e.getMessage());
				} finally {
					connection = null;
					// updateButtonsForDisconnected();
				}
			}
		}).start();
	}

	private Destination getDestination(String destinationName, Session session)
			throws JMSException {
		Destination destination;

		if (destinationName.startsWith("/topic/")) {
			destination = session.createTopic(destinationName);
		} else if (destinationName.startsWith("/queue/")) {
			System.out
					.println("\n\n **** Setting up Destination, Session might be null: "
							+ (session == null ? "YES" : "NO") + " **** \n\n");
			destination = session.createQueue(destinationName);
		} else {
			Log.d(TAG, "Invalid destination name: " + destinationName);
			return null;
		}

		System.out.println("\n\n -- GOT THE CHAT DESTINATION: "
				+ destination.toString() + " --- \n\n\n");
		return destination;

	}

	// private void logMessage(final String message) {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// // Clear log after 100 messages
	// if (logTextView.getLineCount() > 100) {
	// logTextView.setText(message);
	// } else {
	// logTextView.setText(message + "\n" + logTextView.getText());
	// }
	//
	// }
	// });
	// }

	// private void updateButtonsForConnected() {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// connectBtn.setEnabled(false);
	// disconnectBtn.setEnabled(true);
	// subscribeBtn.setEnabled(true);
	// unsubscribeBtn.setEnabled(true);
	// sendBtn.setEnabled(true);
	// }
	// });
	// }
	//
	// private void updateButtonsForDisconnected() {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// connectBtn.setEnabled(true);
	// disconnectBtn.setEnabled(false);
	// subscribeBtn.setEnabled(false);
	// sendBtn.setEnabled(false);
	// unsubscribeBtn.setEnabled(false);
	// }
	// });
	// }

	// private ChallengeHandler createChallengehandler() {
	// final LoginHandler loginHandler = new LoginHandler() {
	// private String username;
	// private char[] password;
	//
	// @Override
	// public PasswordAuthentication getCredentials() {
	// try {
	// final Semaphore semaphore = new Semaphore(1);
	//
	// // Acquire semaphore so that subsequent acquire will block
	// // until released.
	// // This is used to wait until the login dialog is dismissed
	// semaphore.acquire();
	// final LoginDialogFragment loginDialog = new LoginDialogFragment();
	// loginDialog.setListener(new LoginDialogListener() {
	// public void onDismissed() {
	// semaphore.release();
	// }
	// });
	// runOnUiThread(new Runnable() {
	// public void run() {
	// loginDialog.show(getSupportFragmentManager(),
	// "Login Dialog Fragment");
	// loginDialog.getFragmentManager()
	// .executePendingTransactions();
	// loginDialog.getDialog().setCanceledOnTouchOutside(
	// false);
	// }
	// });
	//
	// // wait until the dialog is dismissed
	// semaphore.acquire();
	//
	// if (loginDialog.isCancelled()) {
	// return null;
	// }
	//
	// username = loginDialog.getUsername();
	// password = loginDialog.getPassword().toCharArray();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return new PasswordAuthentication(username, password);
	// }
	// };
	// BasicChallengeHandler challengeHandler = BasicChallengeHandler.create();
	// challengeHandler.setLoginHandler(loginHandler);
	// return challengeHandler;
	// }

	private class ConnectionExceptionListener implements ExceptionListener {

		public void onException(final JMSException exception) {
			Log.d(TAG, "Connection Exception: " + exception.getMessage());
		}
	}

	private class DestinationMessageListener implements MessageListener {

		public void onMessage(Message message) {
			System.out
					.println("\n\n\n ***** DestinationMessageListener:onMessage - GOT MESSAGE: "
							+ message.toString() + " ******* \n\n\n");
			try {
				if (message instanceof TextMessage) {
					Log.d(TAG, "RECEIVED TextMessage: "
							+ ((TextMessage) message).getText());
				} else if (message instanceof BytesMessage) {
					BytesMessage bytesMessage = (BytesMessage) message;

					long len = bytesMessage.getBodyLength();
					byte b[] = new byte[(int) len];
					bytesMessage.readBytes(b);

					Log.d(TAG, "RECEIVED BytesMessage: " + hexDump(b));
				} else if (message instanceof MapMessage) {
					MapMessage mapMessage = (MapMessage) message;
					Enumeration mapNames = mapMessage.getMapNames();
					while (mapNames.hasMoreElements()) {
						String key = (String) mapNames.nextElement();
						Object value = mapMessage.getObject(key);

						if (value == null) {
							Log.d(TAG, key + ": null");
						} else if (value instanceof byte[]) {
							byte[] arr = (byte[]) value;
							StringBuilder s = new StringBuilder();
							s.append("[");
							for (int i = 0; i < arr.length; i++) {
								if (i > 0) {
									s.append(",");
								}
								s.append(arr[i]);
							}
							s.append("]");
							Log.d(TAG, key + ": " + s.toString() + " (Byte[])");
						} else {
							Log.d(TAG, key + ": " + value.toString() + " ("
									+ value.getClass().getSimpleName() + ")");
						}
					}
					Log.d(TAG, "RECEIVED MapMessage: ");
				} else {
					Log.d(TAG, "UNKNOWN MESSAGE TYPE: "
							+ message.getClass().getSimpleName());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				Log.d(TAG, "EXCEPTION: " + ex.getMessage());
			}
		}

		private String hexDump(byte[] b) {
			if (b.length == 0) {
				return "empty";
			}

			StringBuilder out = new StringBuilder();
			for (int i = 0; i < b.length; i++) {
				out.append(Integer.toHexString(b[i])).append(' ');
			}
			return out.toString();
		}

	}

	public Session getSession() {
		return session;
	}
}
