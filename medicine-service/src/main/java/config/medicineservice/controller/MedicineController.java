package config.medicineservice.controller;

import config.medicineservice.entity.Medicine;
import config.medicineservice.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Slf4j
public class MedicineController {
    private final MedicineService medicineService;
    private final RedissonClient redissonClient;


    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        log.info(" Người dùng yêu cầu lấy thông tin thuốc", id);
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<String> sellMedicine(@PathVariable Long id) {
        RLock lock = redissonClient.getLock("lock:medicine:" + id);
        boolean locked = false;
        try {
            locked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (locked) {
                Medicine medicine = medicineService.getMedicineById(id);
                if (medicine.getQuantity() > 0) {
                    medicine.setQuantity(medicine.getQuantity() - 1);
                    medicineService.updateMedicine(medicine);
                    log.info("Một nhân viên đã bán được 1 sản phẩm có mã " + id);
                    return ResponseEntity.ok("Thanh toán thành công!");
                } else {
                    log.info("Một nhân viên định bán sản phẩm có mã " + id + " nhưng đã hết hàng");
                    return ResponseEntity.badRequest().body("Hết hàng!");
                }
            } else {
                log.info("Một nhân viên không thể mua được sản phẩm có mã " + id + " do có người khác đang mua");
                return ResponseEntity.badRequest().body("Đã có người khác đang thanh toán, vui lòng thử lại!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Lỗi hệ thống!");
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(
            @PathVariable Long id,
            @RequestBody Medicine medicineRequest) {

        log.info(" Người dùng yêu cầu cập nhật thuốc", id);
        medicineRequest.setId(id);

        Medicine updated = medicineService.updateMedicine(medicineRequest);
        return ResponseEntity.ok(updated);
    }

}