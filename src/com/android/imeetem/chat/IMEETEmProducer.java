/**
 * 
 */
package com.android.imeetem.chat;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author kevinscomp
 * 
 */
public class IMEETEmProducer {

	private IMEETEmProducer() {
	}

	public static IMEETEmProducer getInstance() {
		return new IMEETEmProducer();
	}

	public void sendMessage(String msg, String queueName) {

		try {

			// Start connection
			ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(destination);
			connection.start();

			// create message to send
			TextMessage message = session.createTextMessage();
			message.setText(msg);

			producer.send(message);

			// close everything
			producer.close();
			session.close();
			connection.close();

		} catch (JMSException ex) {
			System.out.println("Error = " + ex.getMessage());
		}
	}
}
