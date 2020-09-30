package com.sergiomartinrubio.messagedrivenbeanexample;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        // destination is "Physical Destination Name" of "JMS Destination Resource" in Payara/Glassfish
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "myQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MyMessageDrivenBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MyMessageDrivenBean.class.getName());

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            LOGGER.info("Message received: " + textMessage.getText());
        } catch (JMSException e) {
            LOGGER.severe("Something went wrong when consuming message: " + e.getMessage());
        }
    }
}
