package com.bobocode;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

@SuppressWarnings(value = "all")
public class MessageConsumerApp {

    /**
     * Спочатку запускаєм консюмера, він створює чергу і слухає, потім раним продюсер він створює канал і
     * робить basicPuplish в нашу чергу і тут ми його вичитуєм
     */
    @SneakyThrows
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.189");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = "test-queue";
        channel.queueDeclare(queueName, true, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println(messageStr);
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

}
