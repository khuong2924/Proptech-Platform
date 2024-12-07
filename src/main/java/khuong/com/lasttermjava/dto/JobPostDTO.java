package khuong.com.lasttermjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostDTO {

    private Long id;
    private Long userId;
    private String loaiBaiDang;
    private String diaChi;
    private String loaiBDS;
    private BigDecimal dienTich;
    private BigDecimal mucGia;
    private String donVi;
    private String giayToPhapLy;
    private String noiThat;
    private Integer soPhongNgu;
    private Integer soPhongTamVeSinh;
    private String huongNha;
    private String huongBanCong;
    private BigDecimal duongVao;
    private BigDecimal matTien;
    private String tenChuBaiDang;
    private String soDienThoaiChu;
    private String tieuDe;
    private String moTa;
    private LocalDateTime thoiGianDang;
    private Boolean congKhai;
    private Set<String> imageUrls;

}
