package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import util.OTPUtil;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

public class ActiveMQConsumer implements MessageListener {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "notificationQueue";
    private Connection connection;

    public void startListening() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
            connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(this);
            System.out.println("ActiveMQ Consumer started listening on queue: " + QUEUE_NAME);
        } catch (Exception e) {
            System.err.println("Failed to start ActiveMQ Consumer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopListening() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received message from queue: " + text);

                // Simple format parsing: "OTP|user@example.com|123456"
                if (text != null && text.startsWith("OTP|")) {
                    String[] parts = text.split("\\|");
                    if (parts.length == 3) {
                        String email = parts[1];
                        String otp = parts[2];
                        OTPUtil.sendOTPEmail(email, otp);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing ActiveMQ message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
