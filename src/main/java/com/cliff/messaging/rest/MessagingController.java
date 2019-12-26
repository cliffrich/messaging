package com.cliff.messaging.rest;

import com.cliff.messaging.config.KafkaThemisProducer;
import com.cliff.messaging.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/v1/api/message")
@Slf4j
public class MessagingController {
    private KafkaThemisProducer kafkaThemisProducer;
    private CountDownLatch latch;
    private final KafkaTemplate<String, String> template;

    @Autowired
    public MessagingController(final KafkaTemplate<String, String> kafkaTemplate, KafkaThemisProducer kafkaThemisProducer){
        this.kafkaThemisProducer = kafkaThemisProducer;
        this.template = kafkaTemplate;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    CompletableFuture<ResponseEntity<?>> postMessage(@RequestBody String person,
                                                       @RequestParam(value = "mock", required = false) boolean mock){
        log.info("Request received to post '{}' Person(s) to a kafka topic", person);
        kafkaThemisProducer.sendMessage("person", person);
        return CompletableFuture.completedFuture(ResponseEntity.of(Optional.of("{response:success}")));
    }

    @GetMapping("/hello")
    public String hello() throws Exception {
        latch = new CountDownLatch(1);
        IntStream.range(0, 1)
                .forEach(i -> this.template.send("person", String.valueOf(i),
                        "A Practical Advice")
                );
        latch.await(60, TimeUnit.SECONDS);
        System.out.println("All messages received");
        return "Hello Kafka!";
    }
}
