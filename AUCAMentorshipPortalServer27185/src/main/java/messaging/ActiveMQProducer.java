package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ActiveMQProducer {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "notificationQueue";

    public static void sendNotification(String notificationMessage) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);
            MessageProducer producer = session.createProducer(destination);

            TextMessage message = session.createTextMessage(notificationMessage);
            producer.send(message);

            System.out.println("Sent message to queue: " + message.getText());

            session.close();
            connection.close();
        } catch (Exception e) {
            System.err.println("Failed to send message to ActiveMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
