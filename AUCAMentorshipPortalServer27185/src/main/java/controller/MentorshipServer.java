package controller;

import messaging.ActiveMQConsumer;
import service.MentorProfileService;
import service.MentorshipProgramService;
import service.MentorshipSessionService;
import service.NotificationService;
import service.ReportService;
import service.UserService;
import service.impl.MentorProfileServiceImpl;
import service.impl.MentorshipProgramServiceImpl;
import service.impl.MentorshipSessionServiceImpl;
import service.impl.NotificationServiceImpl;
import service.impl.ReportServiceImpl;
import service.impl.UserServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import org.apache.activemq.broker.BrokerService;

public class MentorshipServer {

    public static void main(String[] args) {
        ActiveMQConsumer activeMQConsumer = new ActiveMQConsumer();

        try {
            // Start Embedded ActiveMQ Broker
            BrokerService broker = new BrokerService();
            broker.addConnector("tcp://localhost:61616");
            broker.setPersistent(false); // Do not persist messages to disk for this simple setup
            broker.start();
            System.out.println("Embedded ActiveMQ Broker started on tcp://localhost:61616.");

            // Start RMI Registry on port 1099
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started on port 1099.");

            // Instantiate services
            UserService userService = new UserServiceImpl();
            MentorProfileService mentorProfileService = new MentorProfileServiceImpl();
            MentorshipProgramService mentorshipProgramService = new MentorshipProgramServiceImpl();
            MentorshipSessionService mentorshipSessionService = new MentorshipSessionServiceImpl();
            NotificationService notificationService = new NotificationServiceImpl();
            ReportService reportService = new ReportServiceImpl();

            // Bind services to RMI Registry
            Naming.rebind("rmi://localhost:1099/UserService", userService);
            Naming.rebind("rmi://localhost:1099/MentorProfileService", mentorProfileService);
            Naming.rebind("rmi://localhost:1099/MentorshipProgramService", mentorshipProgramService);
            Naming.rebind("rmi://localhost:1099/MentorshipSessionService", mentorshipSessionService);
            Naming.rebind("rmi://localhost:1099/NotificationService", notificationService);
            Naming.rebind("rmi://localhost:1099/ReportService", reportService);

            System.out.println("All services bound successfully to the RMI Registry.");

            // Start the ActiveMQ consumer for background tasks (e.g., emails)
            activeMQConsumer.startListening();
            System.out.println("AUCA Mentorship Portal Server is running...");

            // Add shutdown hook to clean up resources
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                activeMQConsumer.stopListening();
                try {
                    broker.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }));

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
