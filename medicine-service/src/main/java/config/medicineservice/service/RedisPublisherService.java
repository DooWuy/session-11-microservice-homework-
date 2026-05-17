package config.medicineservice.service;

import config.medicineservice.dto.PharmacyAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisherService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publishAlert(String channel, PharmacyAlert alert) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(alert);
            redisTemplate.convertAndSend(channel, jsonMessage);
            log.info("Published alert to channel {}: {}", channel, jsonMessage);
        } catch (Exception e) {
            log.error("Error publishing alert", e);
        }
    }

}
