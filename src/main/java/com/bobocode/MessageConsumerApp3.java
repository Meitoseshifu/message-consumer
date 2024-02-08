package com.bobocode;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

@SuppressWarnings(value = "all")
public class MessageConsumerApp3 {
    /**
     * match to MessageProducerApp3
     */
    @SneakyThrows
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.189");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 1. Declare a queue with your last name
        String queueName = "andrew-queue";
        //.queueDeclare create queue if there no queue with this name,
        // if queue alredy created it useless
        channel.queueDeclare(queueName, true, false,
                false, null);

        // 2. Map (binding) topic in  MessageProducer and this queue
        String exchangeName = "bobocode-topic";
        // get all announcements
        //channel.queueBind(queueName, exchangeName, "announcement.*");
        // get all for petros
        channel.queueBind(queueName, exchangeName, "*.petros");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println(messageStr);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
