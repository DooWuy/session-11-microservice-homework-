package config.medicineservice.controller;

import config.medicineservice.entity.Medicine;
import config.medicineservice.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Slf4j
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        log.info(" Người dùng yêu cầu lấy thông tin thuốc", id);
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
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
