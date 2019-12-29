package com.cliff.messaging.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaThemisConsumer {

//    @KafkaListener(topics = "person", groupId = "themis", )
    public void listen(String message) {
        log.info("Received Messasge in group themis: " + message);
    }
}
