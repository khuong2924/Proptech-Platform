package khuong.com.lasttermjava.service;

import khuong.com.lasttermjava.dto.TransactionDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.entity.Transaction;
import khuong.com.lasttermjava.repository.JobPostRepository;
import khuong.com.lasttermjava.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private JobPostRepository jobPostRepository;

    // Mapping Transaction entities to DTOs
    private List<TransactionDTO> mapToDto(List<Transaction> transactions) {
        List<TransactionDTO> transactionDTOList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDTOList.add(new TransactionDTO(
                    transaction.getId(),
                    transaction.getJobPost().getId(),
                    transaction.getNoiDung(),
                    transaction.getSdtKhachHang(),
                    transaction.getLoaiHoSo(),
                    transaction.getTrangThaiGiaoDich(),
                    transaction.getGiayToPhapLy(),
                    transaction.getHopDongMuaBan(),
                    transaction.getTrangThaiThanhToan(),
                    transaction.getHopDongThue(),
                    transaction.getTienThue(),
                    transaction.getNgayTraDinhKy()
            ));
        }
        return transactionDTOList;
    }

    // Get all transactions
    public List<TransactionDTO> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return mapToDto(transactions);
    }

    // Create a new transaction
    public void create(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setJobPost(jobPostRepository.findById(transactionDTO.getJobPostId()).get()); // Assuming JobPost constructor with ID
        transaction.setNoiDung(transactionDTO.getNoiDung());
        transaction.setSdtKhachHang(transactionDTO.getSdtKhachHang());
        transaction.setLoaiHoSo(transactionDTO.getLoaiHoSo());
        transaction.setTrangThaiGiaoDich(transactionDTO.getTrangThaiGiaoDich());
        transaction.setGiayToPhapLy(transactionDTO.getGiayToPhapLy());
        transaction.setHopDongMuaBan(transactionDTO.getHopDongMuaBan());
        transaction.setTrangThaiThanhToan(transactionDTO.getTrangThaiThanhToan());
        transaction.setHopDongThue(transactionDTO.getHopDongThue());
        transaction.setTienThue(transactionDTO.getTienThue());
        transaction.setNgayTraDinhKy(transactionDTO.getNgayTraDinhKy());
        transactionRepository.save(transaction);
    }

    // Update an existing transaction
    public void update(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setJobPost(jobPostRepository.findById(transactionDTO.getJobPostId()).get());
        transaction.setNoiDung(transactionDTO.getNoiDung());
        transaction.setSdtKhachHang(transactionDTO.getSdtKhachHang());
        transaction.setLoaiHoSo(transactionDTO.getLoaiHoSo());
        transaction.setTrangThaiGiaoDich(transactionDTO.getTrangThaiGiaoDich());
        transaction.setGiayToPhapLy(transactionDTO.getGiayToPhapLy());
        transaction.setHopDongMuaBan(transactionDTO.getHopDongMuaBan());
        transaction.setTrangThaiThanhToan(transactionDTO.getTrangThaiThanhToan());
        transaction.setHopDongThue(transactionDTO.getHopDongThue());
        transaction.setTienThue(transactionDTO.getTienThue());
        transaction.setNgayTraDinhKy(transactionDTO.getNgayTraDinhKy());
        transactionRepository.save(transaction);
    }

    // Get a transaction by ID
    public TransactionDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return new TransactionDTO(
                transaction.getId(),
                transaction.getJobPost().getId(),
                transaction.getNoiDung(),
                transaction.getSdtKhachHang(),
                transaction.getLoaiHoSo(),
                transaction.getTrangThaiGiaoDich(),
                transaction.getGiayToPhapLy(),
                transaction.getHopDongMuaBan(),
                transaction.getTrangThaiThanhToan(),
                transaction.getHopDongThue(),
                transaction.getTienThue(),
                transaction.getNgayTraDinhKy()
        );
    }

    // Delete a transaction by ID
    public void delete(Long id) {
        transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transactionRepository.deleteById(id);
    }
}
