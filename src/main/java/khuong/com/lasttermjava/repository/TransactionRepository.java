package khuong.com.lasttermjava.repository;

import khuong.com.lasttermjava.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByLoaiHoSo(String loaiHoSo);

    // Truy vấn theo userId và trangThaiGiaoDich
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.trangThaiGiaoDich = :trangThaiGiaoDich")
    List<Transaction> findByUserIdAndTrangThaiGiaoDich(@Param("userId") Long userId, @Param("trangThaiGiaoDich") Boolean trangThaiGiaoDich);

}
