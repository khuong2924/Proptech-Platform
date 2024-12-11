package khuong.com.lasttermjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private Long jobPostId; // Chỉ trả về id của JobPost thay vì đối tượng JobPost
    private Long userId;    // Chỉ trả về id của User thay vì đối tượng User
    private boolean isFlagged;
    private String content;
    private Long transactionId;
}
