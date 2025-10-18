package com.example.bikestore.bikestore.Controllers;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HealthController {
    private final MongoTemplate mongoTemplate;

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> pingDb() {
        try {
            Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            return ResponseEntity.ok(Map.of("status", "ok", "result", result.toJson()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
