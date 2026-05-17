package config.medicineservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisSubscriberService {
    public void handleMessage(String message) {
        log.info("Received alert from pharmacy-alerts: {}", message);
        System.out.println("ALERT RECEIVED: " + message);
    }


}
