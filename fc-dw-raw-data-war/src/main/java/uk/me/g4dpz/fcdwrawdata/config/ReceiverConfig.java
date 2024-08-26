package uk.me.g4dpz.fcdwrawdata.config;

import uk.me.g4dpz.fcdwrawdata.messaging.DefaultErrorHandler;
import uk.me.g4dpz.fcdwrawdata.messaging.Receiver;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@Configuration
@EnableJms
public class ReceiverConfig {

    private String brokerUrl = "tcp://localhost:61616";

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);

        return activeMQConnectionFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setErrorHandler(new DefaultErrorHandler());
        factory.setConcurrency("3-10");

        return factory;
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }
}
