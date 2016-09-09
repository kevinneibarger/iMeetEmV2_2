package com.android.imeetem;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.imeetem.adapters.IMEETEmChatAdapter;
import com.android.imeetem.asynctasks.LoadChatData;
import com.android.imeetem.chat.IMEETEmDispatchQueue;
import com.android.imeetem.dialog.CustomizeDialog;
import com.android.imeetem.services.beans.IMEETEmChatMessagesBean;
import com.android.imeetem.util.IMEETEmConstants;
import com.android.imeetem.util.IMEETEmUtil;
import com.kaazing.gateway.jms.client.JmsConnectionFactory;

interface OnTaskFinishedCheckChatServer {
	void onTaskFinished(boolean isServerUp);
}

public class IMEETEmChatActivity extends ListActivity implements
		OnTaskFinishedCheckChatServer {

	private static final String TAG = "IMEETEmChatActivity";

	private String memId;
	private String imgUrl;
	private String impactId;

	private IMEETEmUtil genericUtil = IMEETEmUtil.getInstance();
	private JmsConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private IMEETEmChatAdapter adapter;
	private boolean isChatServerUp = true;

	private final IMEETEmUtil imeetEmUtil = IMEETEmUtil.getInstance();

	// Get the img of the member you're chatting with
	// private String imgUrl;
	// Get the member id of the member you are chatting with
	// private String impactId;

	private IMEETEmDispatchQueue dispatchQueue;

	private HashMap<String, ArrayDeque<MessageConsumer>> consumers = new HashMap<String, ArrayDeque<MessageConsumer>>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chat_messages_list_main);

		// enables the activity icon as a 'home' button. required if
		// "android:targetSdkVersion" > 14
		getActionBar().setHomeButtonEnabled(true);

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		memId = sharedPreferences.getString("userId", memId);
		imgUrl = sharedPreferences.getString("imageName", imgUrl);
		impactId = getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_IMPACT_ID);

		// Populate Header Row
		// Set the Name and Age of the person you are chatting with
		TextView name = (TextView) findViewById(R.id.msgChatName);
		TextView age = (TextView) findViewById(R.id.msgChatAge);

		name.setText(getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_NAME));
		age.setText(getIntent().getExtras().getString(
				IMEETEmConstants.MATCH_AGE));

		// Hook-up the buttons in the chat header
		Button backToMsgsBtn = (Button) findViewById(R.id.messagesBtn);
		Button viewProfileBtn = (Button) findViewById(R.id.viewProfile);

		backToMsgsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent messages = new Intent(v.getContext(),
						IMEETEmMessagesListActivity.class);
				v.getContext().startActivity(messages);
			}
		});

		viewProfileBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = imeetEmUtil.setClickableForImageAndText(genericUtil
						.convertToMatchInfoListFromIntent(getIntent()), memId,
						IMEETEmConstants.CALLED_FROM_CHAT,
						new Intent(v.getContext(), MatchInfoActivity.class),
						null, null);
				v.getContext().startActivity(i);
			}
		});

		Log.d(TAG,
				"\n-------------------- Loading chat list adapter for DB messages ------------------- \n");

		adapter = new IMEETEmChatAdapter(this, memId);
		setListAdapter(adapter);

		Log.d(TAG, "\n-------------------- Loading chat data for from DB ID: "
				+ this.memId + " ------------------- \n");
		LoadChatData data = new LoadChatData(this, memId, adapter, null,
				impactId, getIntent());
		data.execute(" ");
		Log.d(TAG,
				"\n-------------------- DONE: Loading chat data from DB for ID: "
						+ this.memId + " ------------------- \n");

		// Start chat session!

		// Setup the Connection Factory
		if (connectionFactory == null) {
			try {
				connectionFactory = JmsConnectionFactory
						.createConnectionFactory();
			} catch (JMSException e) {
				e.printStackTrace();
				System.out.println("EXCEPTION: " + e.getMessage());
				isChatServerUp = false;
			}
		}

		// Connect to the Gateway
		final String location = IMEETEmConstants.IMEETEM_CHAT_GATEWAY_HOST
				+ ":" + IMEETEmConstants.IMEETEM_CHAT_GATEWAY_PORT + "/"
				+ IMEETEmConstants.IMEETEM_CHAT_GATEWAY_DOMAIN;

		dispatchQueue = new IMEETEmDispatchQueue("DispatchQueue");
		dispatchQueue.start();
		dispatchQueue.waitUntilReady();
		connect(location);

		// Listen on the Queue // Build the Queue/Topic name, it's
		// /queue/imeetem_FROMID_TOID
		final String destinationName = IMEETEmConstants.IMEETEM_CHAT_DEST_TOPIC;

		System.out.println("\n\n\n ***** SUBSCRIBE to TOPIC - "
				+ destinationName + " **************** \n\n\n");

		dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {

				System.out.println("\n\n **** Listening on Destination TOPIC: "
						+ destinationName + " ****** \n\n\n");
				try {
					Destination destination = getDestination(destinationName);
					if (destination == null) {
						System.out
								.println("\n\n\n *** DESTINATION IS NULL!! **** \n\n");

						return;
					}
					MessageConsumer consumer = session
							.createConsumer(destination);

					consumer.setMessageListener(new DestinationMessageListener());
				} catch (JMSException e) {
					e.printStackTrace();
					System.out.println("EXCEPTION: " + e.getMessage());
					isChatServerUp = false;
				}
			}
		});

		final EditText textMsg = (EditText) findViewById(R.id.chatTextBox);

		Button sendBtn = (Button) findViewById(R.id.chatSendBtn);

		final IMEETEmChatActivity chatActivity = this;

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Check to see if connection was valid
				new CheckChatServerStatus(chatActivity, chatActivity, location)
						.execute(" ");
				System.out
						.println("################ CHECKING CHAT SERVER STATUS, IS IT UP? "
								+ (isChatServerUp ? "YES" : "NO")
								+ " -- #######################");

				if (isChatServerUp) {

					String text = textMsg.getText().toString();

					final String jsonText = buildSendMsg(text).getJSONObject()
							.toString();

					System.out.println("\n\n ***** SENDING MESSAGE: "
							+ jsonText + "***** TO TOPIC: " + destinationName
							+ " \n\n\n ");
					dispatchQueue.dispatchAsync(new Runnable() {
						public void run() {
							try {
								MessageProducer producer = session
										.createProducer(getDestination(destinationName));

								Message message;
								message = session.createTextMessage(jsonText);

								producer.send(message);
								producer.close();
							} catch (JMSException e) {
								e.printStackTrace();
								System.out.println(e.getMessage());
								isChatServerUp = false;
							}
						}
					});
				} else {
					// Popup Alert telling us the chat server is not up.
					System.out
							.println("^^^^^^^^^^^^^^^^^^^^^^^ - CHAT SERVER IS NOT RUNNING - ^^^^^^^^^^^^^^^^^^^^^^^^^");
					View promptView = getLayoutInflater().inflate(
							R.layout.chat_server_down, null);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							v.getContext());

					alertDialogBuilder.setView(promptView);

					Button okBtn = (Button) promptView
							.findViewById(R.id.chatServerDownOKBtn);

					final AlertDialog alertD = alertDialogBuilder.create();

					okBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							alertD.cancel();
						}
					});

					alertD.show();

				}
			}
		});

		// Clear the text box when it's unfocused
		textMsg.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				textMsg.setText("");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.action_menu_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.friends:
			Intent friendsList = new Intent(this,
					IMEETEmFriendsListActivity.class);
			startActivity(friendsList);
			return true;
		case R.id.search_update:
			Intent i = new Intent(this, SearchUpdateActivity.class);
			startActivity(i);
			return true;
		case R.id.email:
			Intent messages = new Intent(this,
					IMEETEmMessagesListActivity.class);
			startActivity(messages);
			return true;
		case android.R.id.home:
			Intent home = new Intent(this, IMEETEmSearchGridActivity.class);
			startActivity(home);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		Intent messagesList = new Intent(this,
				IMEETEmMessagesListActivity.class);
		startActivity(messagesList);
	}

	@Override
	public void onPause() {
		if (connection != null) {
			dispatchQueue.dispatchAsync(new Runnable() {
				@Override
				public void run() {
					try {
						connection.stop();
					} catch (JMSException e) {
						e.printStackTrace();
						isChatServerUp = false;
					}
				}
			});
		}

		super.onPause();
	}

	@Override
	public void onResume() {
		if (connection != null) {
			dispatchQueue.dispatchAsync(new Runnable() {
				@Override
				public void run() {
					try {
						connection.start();
					} catch (JMSException e) {
						e.printStackTrace();
						isChatServerUp = false;
					}
				}
			});
		}

		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (connection != null) {
			disconnect();
		}
		super.onDestroy();
	}

	private void disconnect() {
		System.out.println("DISCONNECTING");

		dispatchQueue.removePendingJobs();
		dispatchQueue.quit();
		new Thread(new Runnable() {
			public void run() {
				try {
					connection.close();
					System.out.println("DISCONNECTED");
				} catch (JMSException e) {
					e.printStackTrace();
					System.out.println("EXCEPTION: " + e.getMessage());
					isChatServerUp = false;
				} finally {
					connection = null;
				}
			}
		}).start();
	}

	private void connect(final String location) {

		dispatchQueue.dispatchAsync(new Runnable() {
			public void run() {
				try {

					connectionFactory.setGatewayLocation(URI.create(location));
					connection = connectionFactory.createConnection();
					connection.start();
					session = connection.createSession(false,
							Session.AUTO_ACKNOWLEDGE);
					System.out.println("CONNECTED");
					connection
							.setExceptionListener(new ConnectionExceptionListener());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("EXCEPTION: " + e.getMessage());
					isChatServerUp = false;
				}
			}
		});
	}

	private Destination getDestination(String destinationName)
			throws JMSException {

		Destination destination = null;
		if (destinationName.startsWith("/topic/")) {
			destination = session.createTopic(destinationName);
		} else if (destinationName.startsWith("/queue/")) {
			destination = session.createQueue(destinationName);
		} else {
			System.out.println("Invalid destination name: " + destinationName);
			return null;
		}

		return destination;

	}

	private class ConnectionExceptionListener implements ExceptionListener {
		public void onException(final JMSException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private class DestinationMessageListener implements MessageListener {

		private String textMessage;

		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {
					System.out.println("\n\n\n **** RECEIVED TextMessage: "
							+ ((TextMessage) message).getText());

					textMessage = ((TextMessage) message).getText();
				} else {
					System.out.println("\n\n\n **** UNKNOWN MESSAGE TYPE: "
							+ message.getClass().getSimpleName());
				}

				System.out
						.println("\n\n ############# Got Message from Queue: "
								+ message.getJMSDestination().toString() + " "
								+ textMessage + " ############## \n\n\n");

				// If "from" and "To" are appropriate for this session,
				// continue.
				if (displayAndStoreMessage(textMessage)) {

					// TextMessage will come in as JSON Object convert it to a
					// Chat
					// Message Bean
					IMEETEmChatMessagesBean fromMsg = null;
					try {
						fromMsg = genericUtil
								.convertTextMessageToChatMsgBean(textMessage);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					final LoadChatData data = new LoadChatData(
							IMEETEmChatActivity.this, fromMsg.getChatMsgToId(),
							adapter, fromMsg, fromMsg.getChatMsgFromId(),
							getIntent());

					Handler h = new Handler(Looper.getMainLooper());
					h.post(new Runnable() {

						@Override
						public void run() {
							System.out
									.println("\n\n\n ******* Running LoadChatData! ************ \n\n\n");
							data.execute(" ");
						}
					});

					Log.d(TAG,
							"\n-------------------- DONE: Loading current chat data  ------------------- \n");
				} else {
					System.out.println("\n\n @@@@@@@@ MESSAGE ON THE TOPIC: "
							+ message.getJMSDestination().toString()
							+ " WAS NOT OURS! @@@@@@@ \n\n\n");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("EXCEPTION: " + ex.getMessage());
				isChatServerUp = false;
			}
		}

	}

	private IMEETEmChatMessagesBean buildSendMsg(String msg) {

		IMEETEmChatMessagesBean chatMsg = IMEETEmChatMessagesBean.getInstance();
		chatMsg.setChatMsgImageURL(imgUrl);
		chatMsg.setChatMsgFromId(memId);
		chatMsg.setChatMsgToId(impactId);
		chatMsg.setChatMsgDate("Sent Now");
		chatMsg.setChatMsgText(msg);
		return chatMsg;
	}

	private boolean displayAndStoreMessage(String textMessage) {

		String fromId = null;
		String toId = null;

		try {
			JSONObject jsonData = new JSONObject(textMessage);

			fromId = jsonData.getString("FROM_ID");
			toId = jsonData.getString("TO_ID");

			System.out.println("############# MEM ID FROM SESSION: "
					+ this.memId + " FROM ID: " + fromId
					+ " IMPACT ID FROM SESSION: " + this.impactId + " TO ID: "
					+ toId
					+ "####################################################");

			if (fromId != null && toId != null) {
				if (fromId.equalsIgnoreCase(memId)
						&& toId.equalsIgnoreCase(impactId)
						|| fromId.equalsIgnoreCase(impactId)
						&& toId.equalsIgnoreCase(memId)) {
					return true;
				} else {
					return false;
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out
				.println("\n\n -- displayAndStoreMessage is at the end of the method and returning false, this shouldn't happen --- \n\n");
		return false;
	}

	private class CheckChatServerStatus extends
			AsyncTask<String, String, String> {

		private CustomizeDialog mCustomizeDialog;
		private Context context;
		private OnTaskFinishedCheckChatServer listener;
		private String location;

		public CheckChatServerStatus(IMEETEmChatActivity activity,
				OnTaskFinishedCheckChatServer listener, String location) {
			this.context = activity;
			this.listener = listener;
			this.location = location;
		}

		@Override
		protected void onPreExecute() {

			mCustomizeDialog = new CustomizeDialog(this.context);
			mCustomizeDialog.setTitle("");
			mCustomizeDialog.show();
			mCustomizeDialog.setCanceledOnTouchOutside(false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {

			try {
				URL myUrl = new URL(this.location);
				URLConnection connection = myUrl.openConnection();
				connection.setConnectTimeout(1000);
				connection.connect();
				return "Connected";
			} catch (Exception e) {
				return "NotConnected";
			}
		}

		protected void onPostExecute(String result) {

			if (mCustomizeDialog.isShowing()) {
				mCustomizeDialog.dismiss();
			}

			if (result != null && result.equals("Connected")) {
				listener.onTaskFinished(true);
			} else {
				listener.onTaskFinished(false);
			}
		}

	}

	@Override
	public void onTaskFinished(boolean isServerUp) {
		isChatServerUp = isServerUp;
	}
}