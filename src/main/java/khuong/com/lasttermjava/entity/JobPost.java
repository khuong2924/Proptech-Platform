package khuong.com.lasttermjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    // Các cột mới
    @Column(name = "LoaiBaiDang", nullable = false, length = 50)
    private String loaiBaiDang;

    @Column(name = "DiaChi", nullable = false, columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "LoaiBDS", nullable = false, length = 50)
    private String loaiBDS;

    @Column(name = "DienTich", nullable = false, precision = 10, scale = 2)
    private BigDecimal dienTich;

    @Column(name = "MucGia", nullable = false, precision = 18, scale = 2)
    private BigDecimal mucGia;

    @Column(name = "DonVi", length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'VND'")
    private String donVi = "VND";

    @Column(name = "GiayToPhapLy", length = 100)
    private String giayToPhapLy;

    @Column(name = "NoiThat", length = 255)
    private String noiThat;

    @Column(name = "SoPhongNgu")
    private Integer soPhongNgu;

    @Column(name = "SoPhongTamVeSinh")
    private Integer soPhongTamVeSinh;

    @Column(name = "HuongNha", length = 50)
    private String huongNha;

    @Column(name = "HuongBanCong", length = 50)
    private String huongBanCong;

    @Column(name = "DuongVao", precision = 10, scale = 2)
    private BigDecimal duongVao;

    @Column(name = "MatTien", precision = 10, scale = 2)
    private BigDecimal matTien;

    @Column(name = "TenChuBaiDang", nullable = false, length = 100)
    private String tenChuBaiDang;

    @Column(name = "SoDienThoaiChu", nullable = false, length = 15)
    private String soDienThoaiChu;

    @Column(name = "TieuDe", nullable = false, columnDefinition = "TEXT")
    private String tieuDe;

    @Column(name = "MoTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "thoiGianDang", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime thoiGianDang;

    @Column(name = "CongKhai")
    private Boolean congKhai;
}
