package com.cliff.messaging.rest;

import com.cliff.messaging.config.KafkaThemisManualConsumer;
import com.cliff.messaging.config.KafkaThemisProducer;
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
    private KafkaThemisManualConsumer kafkaThemisManualConsumer;
    private CountDownLatch latch;
    private final KafkaTemplate<String, String> template;

    @Autowired
    public MessagingController(final KafkaTemplate<String, String> kafkaTemplate, KafkaThemisProducer kafkaThemisProducer, KafkaThemisManualConsumer kafkaThemisManualConsumer){
        this.kafkaThemisProducer = kafkaThemisProducer;
        this.template = kafkaTemplate;
        this.kafkaThemisManualConsumer = kafkaThemisManualConsumer;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    CompletableFuture<ResponseEntity<?>> postMessage(@RequestBody String person,
                                                     @RequestParam(value = "no", required = false) Integer no,
                                                       @RequestParam(value = "mock", required = false) boolean mock){
        log.info("Request received to post '{}' Person(s) to a kafka 'person' topic", no);
        IntStream.range(0, no).forEach(value -> {
            log.info("sending message {}", value);
            kafkaThemisProducer.sendMessage("person", 0, Integer.toString(value), person +"-"+value);
        });
        return CompletableFuture.completedFuture(ResponseEntity.of(Optional.of("{response:success}")));
    }

    @GetMapping(produces = "application/json")
    public CompletableFuture<ResponseEntity<?>> retrievePerson(
            @RequestParam(value = "from", required = false) String fromOffset,
            @RequestParam(value = "mock", required = false) boolean mock) {
        log.info("Request received to retrieve Person records from offset '{}'", fromOffset);
        return CompletableFuture.completedFuture(kafkaThemisManualConsumer.consumeMessage(new Long(fromOffset)));
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
