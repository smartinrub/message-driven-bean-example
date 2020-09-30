package com.sergiomartinrubio.messagedrivenbeanexample;

import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/message-driven-bean")
public class MessageDrivenBeanClient extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MessageDrivenBeanClient.class.getName());

    // We need to configure a connection factory in Payara/Glassfish with this "JNDI Name"
    // and resource type javax.jms.ConnectionFactory
    @Resource(lookup = "jms/myConnectionFactory")
    private ConnectionFactory connectionFactory;

    // This is the "JNDI Name" of the "JMS Destination Resource" in Payara/Glassfish
    @Resource(lookup = "jms/myQueue")
    private Queue queue;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String message = req.getParameter("message");

        try (Connection connection = connectionFactory.createConnection()) {
            Session queueSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = queueSession.createProducer(queue);

            connection.start();

            TextMessage textMessage = queueSession.createTextMessage(message);
            publisher.send(textMessage);
            LOGGER.info("Message " + message + " sent to queue " + queue.getQueueName());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
