package config.medicineservice.service;

import config.medicineservice.entity.Medicine;
import config.medicineservice.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

public class MedicineService {
    private final MedicineRepository medicineRepository;


    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicineById(Long id) {
        log.info(" Đang truy vấn trực tiếp từ Database cho Medicine ID: {}", id);

        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + id));
    }

    @CacheEvict(value = "medicines", key = "#medicine.id")
    public Medicine updateMedicine(Medicine medicine) {
        log.info(" Đang cập nhật thuốc ID: {} - Xóa cache cũ", medicine.getId());

        medicineRepository.findById(medicine.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + medicine.getId()));

        Medicine updatedMedicine = medicineRepository.save(medicine);
        log.info(" Thuốc ID: {} đã được cập nhật. Cache đã bị xóa.", medicine.getId());

        return updatedMedicine;
    }

    public Medicine updateMedicineEvictAll(Medicine medicine) {
        log.info("Đang cập nhật thuốc ID: {} - Xóa toàn bộ cache medicines", medicine.getId());

        medicineRepository.findById(medicine.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc với ID: " + medicine.getId()));

        Medicine updatedMedicine = medicineRepository.save(medicine);
        log.info("Toàn bộ cache medicines đã bị xóa");

        return updatedMedicine;
    }
}
