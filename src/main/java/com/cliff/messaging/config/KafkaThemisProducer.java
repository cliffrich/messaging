package com.cliff.messaging.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class KafkaThemisProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, Integer partition, String key, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, partition, key, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[{}], partition={}, key={} with offset=[{}]", new Object[]{message, result.getRecordMetadata().partition(),
                        key, result.getRecordMetadata().offset()});
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }

    public void sendMessage(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[{}] due to : {}", message, ex.getMessage());
            }
        });
    }
}
