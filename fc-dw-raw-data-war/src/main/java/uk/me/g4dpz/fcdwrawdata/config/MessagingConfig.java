package uk.me.g4dpz.fcdwrawdata.config;

import uk.me.g4dpz.fcdwrawdata.service.HexFrameService;
import uk.me.g4dpz.fcdwrawdata.messaging.JmsMessageSender;
import uk.me.g4dpz.fcdwrawdata.service.impl.HexFrameServiceImpl;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableJms
public class MessagingConfig {

    @Bean
    HexFrameService hexFrameService() {
        return new HexFrameServiceImpl();
    }

    @Bean
    JmsMessageSender jmsMessageSender() {
        return new JmsMessageSender();
    }

    @Bean
    ActiveMQConnectionFactory amqConnectionFactory() {

        final ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        return activeMQConnectionFactory;
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(amqConnectionFactory());
    }

    @Bean
    ActiveMQQueue defaultDestination() {
        return new ActiveMQQueue("frame_processed");
    }

    @Bean
    JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setDefaultDestination(defaultDestination());
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(hexFrameService());
    }

    @Bean
    DefaultMessageListenerContainer messageListenerContainer() {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        defaultMessageListenerContainer.setDestinationName("frame_processed");
        defaultMessageListenerContainer.setMessageListener(messageListenerAdapter());
        return defaultMessageListenerContainer;
    }

}
