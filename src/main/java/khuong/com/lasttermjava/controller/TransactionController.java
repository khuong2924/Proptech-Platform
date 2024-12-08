package khuong.com.lasttermjava.controller;

import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.entity.Transaction;
import khuong.com.lasttermjava.repository.TransactionRepository;
import khuong.com.lasttermjava.repository.JobPostRepository;
import khuong.com.lasttermjava.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final JobPostRepository jobPostRepository;
    @Autowired
    private final ImageUploadService imageUploadService;


    @GetMapping
    public ResponseDTO<List<Transaction>> getAllTransactions() {
        ResponseDTO<List<Transaction>> response = new ResponseDTO<>();
        response.setData(transactionRepository.findAll());
        response.setStatus(200);
        return response;
    }

    // Cập nhật Transaction
    @PutMapping("/{id}")
    public ResponseDTO<Void> updateTransaction(@PathVariable("id") Long id, @RequestBody Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Cập nhật dữ liệu cho Transaction
        existingTransaction.setJobPost(transaction.getJobPost());
        existingTransaction.setNoiDung(transaction.getNoiDung());
        existingTransaction.setSdtKhachHang(transaction.getSdtKhachHang());
        existingTransaction.setLoaiHoSo(transaction.getLoaiHoSo());
        existingTransaction.setTrangThaiGiaoDich(transaction.getTrangThaiGiaoDich());
        existingTransaction.setGiayToPhapLy(transaction.getGiayToPhapLy());
        existingTransaction.setHopDongMuaBan(transaction.getHopDongMuaBan());
        existingTransaction.setTrangThaiThanhToan(transaction.getTrangThaiThanhToan());
        existingTransaction.setHopDongThue(transaction.getHopDongThue());
        existingTransaction.setTienThue(transaction.getTienThue());
        existingTransaction.setNgayTraDinhKy(transaction.getNgayTraDinhKy());

        transactionRepository.save(existingTransaction);

        return ResponseDTO.<Void>builder()
                .status(200)
                .message("Transaction updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteTransaction(@PathVariable("id") Long id) {
        transactionRepository.deleteById(id);
        return ResponseDTO.<Void>builder()
                .status(200)
                .message("Transaction deleted successfully")
                .build();
    }

    @PostMapping("/upload")
    public ResponseDTO<Void> createOrUpdateTransaction(
            @RequestParam("jobPostId") Long jobPostId,
            @RequestParam("noiDung") String noiDung,
            @RequestParam("sdtKhachHang") String sdtKhachHang,
            @RequestParam("loaiHoSo") String loaiHoSo,
            @RequestParam("trangThaiGiaoDich") Boolean trangThaiGiaoDich,
            @RequestParam("giayToPhapLy") List<MultipartFile> giayToPhapLy,
            @RequestParam("hopDongMuaBan") List<MultipartFile> hopDongMuaBan,
            @RequestParam("trangThaiThanhToan") Boolean trangThaiThanhToan,
            @RequestParam("hopDongThue") List<MultipartFile> hopDongThue,
            @RequestParam("tienThue") BigDecimal tienThue,
            @RequestParam("ngayTraDinhKy") String ngayTraDinhKy) throws IOException {

        // Lấy JobPost
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(() -> new RuntimeException("Job post not found"));

        // Tạo hoặc cập nhật Transaction
        Transaction transaction = new Transaction();
        transaction.setJobPost(jobPost);
        transaction.setNoiDung(noiDung);
        transaction.setSdtKhachHang(sdtKhachHang);
        transaction.setLoaiHoSo(loaiHoSo);
        transaction.setTrangThaiGiaoDich(trangThaiGiaoDich);

        if (giayToPhapLy != null && !giayToPhapLy.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : giayToPhapLy) {
                String imageUrl = imageUploadService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            transaction.setGiayToPhapLy(imageUrls);
        }

        if (hopDongMuaBan != null && !hopDongMuaBan.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : hopDongMuaBan) {
                String imageUrl = imageUploadService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            transaction.setHopDongMuaBan(imageUrls);
        }

        transaction.setTrangThaiThanhToan(trangThaiThanhToan);
        if (hopDongThue != null && !hopDongThue.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : hopDongThue) {
                String imageUrl = imageUploadService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            // Thêm ảnh vào các trường giayToPhapLy, hopDongMuaBan, hopDongThue nếu có
            transaction.setHopDongThue(imageUrls); // Thêm ảnh vào giayToPhapLy
        }
        transaction.setTienThue(tienThue);
        transaction.setNgayTraDinhKy(ngayTraDinhKy);

        transactionRepository.save(transaction);

        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Transaction created or updated successfully")
                .build();
    }
}
