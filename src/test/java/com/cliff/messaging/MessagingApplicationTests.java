package com.cliff.messaging;

import com.cliff.messaging.config.KafkaThemisProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class MessagingApplicationTests {
	@Autowired
	private KafkaThemisProducer kafkaThemisProducer;

	@Test
	void contextLoads() {
	}

	@Test
	public void simpleSendAndReceive() {
		kafkaThemisProducer.sendMessage("person", "adam");
	}

}
