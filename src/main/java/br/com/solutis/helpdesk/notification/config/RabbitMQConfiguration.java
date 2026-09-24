package br.com.solutis.helpdesk.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class RabbitMQConfiguration {
    
    @Value ("${api.messager.queue}")
    public String NOTIFICATION_QUEUE;
    @Value ("${api.messager.exchange}")
    public String TICKET_EXCHANGE;
    @Value ("${api.messager.routing-key}")
    public String ROUTING_KEY;

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange ticketExchange() {
        return new TopicExchange(TICKET_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange ticketExchange) {
        return BindingBuilder.bind(notificationQueue).to(ticketExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
