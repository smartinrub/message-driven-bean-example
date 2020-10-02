package com.sergiomartinrubio.messagedrivenbeanexample;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
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

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("Post construct method is invoked.");
    }

    @PreDestroy
    public void preDestroy() {
        LOGGER.info("Pre destroy method is invoked.");
    }

    @AroundInvoke
    public Object logMethodInvocationTime(InvocationContext ctx) throws Exception {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Running method " + ctx.getMethod());

        try {
            return ctx.proceed();
        } finally {
            long totalTime = System.currentTimeMillis() - startTime;
            LOGGER.info("Method" + ctx.getMethod() + " takes " + totalTime + "ms to run!");
        }
    }
}
