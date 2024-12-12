package khuong.com.lasttermjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profiles")
@Data

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    private String fullName;

    private String email;

    private String birthDate;

    private String gender;

    private String bio;

    private String address;

    private String avtPhoto;

    private String phoneNumber;

    public Long getUser_id() {
        return this.user.getId();
    }

    public void setUser_id(Long user_id) {
        this.user.setId(user_id);
    }

}
