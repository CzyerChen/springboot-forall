package com.learning.pulsar.controller;

import com.learning.pulsar.producer.PulsarMessageProducer;
import org.apache.pulsar.client.api.MessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private PulsarMessageProducer messageProducer;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String content) {
        try {
            messageProducer.sendMessage(content);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send message: " + e.getMessage());
        }
    }

    @PostMapping("/async")
    public ResponseEntity<String> sendMessageAsync(@RequestBody String content) {
        try {
            CompletableFuture<MessageId> future = messageProducer.sendMessageAsync(content);
            return ResponseEntity.accepted().body("Message sending in progress...");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send message: " + e.getMessage());
        }
    }
}
