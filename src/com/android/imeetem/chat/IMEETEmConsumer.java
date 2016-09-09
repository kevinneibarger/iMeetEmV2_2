package com.android.imeetem.chat;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class IMEETEmConsumer {

	private IMEETEmConsumer() {
	}

	public static IMEETEmConsumer getInstance() {
		return new IMEETEmConsumer();
	}

	public void consumeMessage(String queueName) {
		try {

			// creating a connection
			ConnectionFactory cf = new com.sun.messaging.ConnectionFactory();
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(destination);
			connection.start();

			// System.out.println("Start listen HelloWorld Queue from HelloConsumer.java");
			long now = System.currentTimeMillis();
			do {
				// receive our message
				TextMessage m = (TextMessage) consumer.receive();
				System.out.println(m.getText() + " timestamp=" + m.getJMSTimestamp());

			} while (now + 1000 * 60 * 10 > System.currentTimeMillis());
			// System.out.println("End listen HelloWorld Queue from HelloConsumer.java");

			// close everything
			session.close();
			connection.close();
			consumer.close();

		} catch (JMSException ex) {
			System.out.println("Error = " + ex.getMessage());
		}
	}
}
