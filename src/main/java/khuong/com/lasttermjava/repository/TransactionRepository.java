package khuong.com.lasttermjava.repository;

import khuong.com.lasttermjava.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByIdDesc();
    List<Transaction> findByLoaiHoSo(String loaiHoSo);
    List<Transaction> findBySdtKhachHang(String sdtKhachHang);

}
