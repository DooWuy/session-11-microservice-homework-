package config.medicineservice.service;

import config.medicineservice.entity.Medicine;
import config.medicineservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

public class MedicineService {
    @Service
    @RequiredArgsConstructor
    @Slf4j
    private final MedicineRepository medicineRepository;


    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicineById(Long id) {
        // Log này dùng để kiểm chứng cơ chế hoạt động của Cache-aside
        log.info("📢  Đang truy vấn trực tiếp từ Database PostgreSQL cho Medicine ID: {}", id);

        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));
    }
}
