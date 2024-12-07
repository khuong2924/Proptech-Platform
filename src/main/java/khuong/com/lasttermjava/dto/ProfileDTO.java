package khuong.com.lasttermjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private Long id;
    private String fullName;
    private String email;
    private String birthDate;
    private String gender;
    private String bio;
    private String address;
    private String avtPhoto;
    private Long userId; // Thay vì trả về đối tượng User, trả về chỉ id người dùng

}
