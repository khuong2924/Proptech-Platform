package khuong.com.lasttermjava.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    private Long jobPostId;

    private String noiDung;

    private String sdtKhachHang;

    private String loaiHoSo;

    private Boolean trangThaiGiaoDich;

    private Set<String> giayToPhapLy;

    private Set<String> hopDongMuaBan;

    private Boolean trangThaiThanhToan;

    private Set<String> hopDongThue;

    private BigDecimal tienThue;

    private String ngayTraDinhKy;

    private Boolean trangThaiDatCoc;

    private Long user_id;
}