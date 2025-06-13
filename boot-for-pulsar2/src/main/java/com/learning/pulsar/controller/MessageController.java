package com.learning.pulsar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.pulsar.producer.MyProducer;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MyProducer messageProducer;

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
            // 由于 future 变量未被使用，直接调用异步发送方法，不保存返回值
            messageProducer.sendMessageAsync(content);
            return ResponseEntity.accepted().body("Message sending in progress...");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send message: " + e.getMessage());
        }
    }
}
