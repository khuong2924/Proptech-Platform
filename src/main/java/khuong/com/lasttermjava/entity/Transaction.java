package khuong.com.lasttermjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jobPost_id", nullable = false)
    private JobPost jobPost;

    @Column(name = "NoiDung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "sdtKhachHang", columnDefinition = "TEXT")
    private String sdtKhachHang;

    @Column(name = "loaiHoSo", columnDefinition = "TEXT")
    private String loaiHoSo;

    @Column(name = "trangThaiGiaoDich")
    private Boolean trangThaiGiaoDich;

    @ElementCollection
    @CollectionTable(name = "giayToPhapLy_images", joinColumns = @JoinColumn(name = "transaction_id_giayToPhapLy"))
    @Column(name = "giayToPhapLy")
    private Set<String> giayToPhapLy = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "hopDongMuaBan_images", joinColumns = @JoinColumn(name = "transaction_id_hopDongMuaBan"))
    @Column(name = "hopDongMuaBan")
    private Set<String> hopDongMuaBan = new HashSet<>();

    @Column(name = "trangThaiThanhToan")
    private Boolean trangThaiThanhToan = false;

    @ElementCollection
    @CollectionTable(name = "hopDongThue_images", joinColumns = @JoinColumn(name = "transaction_id_hopDongThue"))
    @Column(name = "hopDongThue")
    private Set<String> hopDongThue = new HashSet<>();

    @Column(name = "tienThue", nullable = false, precision = 18, scale = 2)
    private BigDecimal tienThue;

    @Column(name = "ngayTraDinhKy", columnDefinition = "TEXT")
    private String ngayTraDinhKy;

}
