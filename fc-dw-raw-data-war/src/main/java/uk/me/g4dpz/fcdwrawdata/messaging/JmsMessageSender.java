package uk.me.g4dpz.fcdwrawdata.messaging;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;

@Service
public class JmsMessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * send text to default destination
     * @param text
     */
    public void send(final String text) {

        Destination destination = new ActiveMQQueue("frame_processed");
        this.jmsTemplate.setDefaultDestination(destination);
        this.jmsTemplate.setDefaultDestinationName("frame_processed");

        this.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(text);
                //set ReplyTo header of Message, pretty much like the concept of email.
                message.setJMSReplyTo(new ActiveMQQueue("frame_processed"));
                return message;
            }
        });
    }

    /**
     * Simplify the send by using convertAndSend
     * @param text
     */
    public void sendText(final String text) {
        this.jmsTemplate.convertAndSend(text);
    }

    /**
     * Send text message to a specified destination
     * @param text
     */
    public void send(final Destination dest, final String text) {

        Destination destination = new ActiveMQQueue("frame_processed");
        this.jmsTemplate.setDefaultDestination(destination);
        this.jmsTemplate.setDefaultDestinationName("frame_processed");

        this.jmsTemplate.send(dest,new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(text);
                message.setJMSReplyTo(new ActiveMQQueue("frame_processed"));
                return message;
            }
        });
    }
}
