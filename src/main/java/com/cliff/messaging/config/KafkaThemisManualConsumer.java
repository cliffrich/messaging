package com.cliff.messaging.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class KafkaThemisManualConsumer {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    public ResponseEntity<?> consumeMessage(long offset){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put("auto.offset.reset", "latest");

        KafkaConsumer consumer = new KafkaConsumer(configs);
        List<TopicPartition> partitions = new ArrayList<>();
        TopicPartition partition0 = new TopicPartition("person", 0);
        partitions.add(partition0);
        List<String> result = new ArrayList<>();
        consumer.assign(partitions);
        try{
            log.info("Consumer - partitions assigned: {}, last offset of given partition; {}, current position of consumer: ",
                    new Object[]{consumer.assignment(), consumer.endOffsets(consumer.assignment()), consumer.position(partition0)});
            consumer.seek(partition0, offset);
            log.info("Consumer - moved to requested offset {} ...... ", offset);
//            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
                records.forEach(record -> {
                    String rec = String.format("Topic: %s, Partition: %d, timestamp: %s, offset: %d, key: %s, Value: %s",
                            record.topic(), record.partition(), new Date(record.timestamp()).toString(), record.offset(), record.key(), record.value());
                    result.add(rec);
                    log.info(rec);
                });
//            }
        } catch (Exception e){
            log.info(e.getMessage());
        } finally {
            consumer.close();
        }
        return response(result);
    }
    private ResponseEntity<?> response(Collection<?> result){
        if(result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.of(Optional.empty());
    }
}
