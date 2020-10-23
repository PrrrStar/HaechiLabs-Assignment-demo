package com.example.demo.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * 이벤트를 사용하기 위한 RabbitMQ 설정
 */
@Configuration
public class RabbitMQConfiguration implements RabbitListenerConfigurer {

    @Bean
    public TopicExchange notificationExchange(@Value("${notification.exchange}") final String exchangeName) {
        return new TopicExchange(exchangeName);
    }



    @Bean
    Queue depositMinedQueue(){
        return new Queue("depositMinedQueue", true);
    }
    @Bean
    Queue depositConfirmedQueue(){
        return new Queue("depositConfirmedQueue", true);
    }
    @Bean
    Queue withdrawPendingQueue(){
        return new Queue("withdrawPendingQueue", true);
    }
    @Bean
    Queue withdrawConfirmedQueue(){
        return new Queue("withdrawConfirmedQueue", true);
    }
    @Bean
    Queue allQueue(){
        return new Queue("allQueue", true);
    }


    @Bean
    Binding depositMinedBinding(final Queue depositMinedQueue,
                    final TopicExchange exchange){
        return BindingBuilder.bind(depositMinedQueue).to(exchange).with("queue.depositMined");
    }
    @Bean
    Binding depositConfirmedBinding(final Queue depositConfirmedQueue,
                    final TopicExchange exchange){
        return BindingBuilder.bind(depositConfirmedQueue).to(exchange).with("queue.depositConfirmed");
    }
    @Bean
    Binding withdrawPendingBinding(final Queue withdrawPendingQueue,
                                    final TopicExchange exchange){
        return BindingBuilder.bind(withdrawPendingQueue).to(exchange).with("queue.withdrawPending");
    }
    @Bean
    Binding withdrawConfirmedBinding(final Queue withdrawConfirmedQueue,
                                    final TopicExchange exchange){
        return BindingBuilder.bind(withdrawConfirmedQueue).to(exchange).with("queue.withdrawConfirmed");
    }

    @Bean
    Binding allBinding(final Queue allQueue,
                       final TopicExchange exchange){
        return BindingBuilder.bind(allQueue).to(exchange).with("queue.*");
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter(){
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory(){
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
