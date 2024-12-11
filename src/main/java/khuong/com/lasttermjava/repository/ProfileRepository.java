package khuong.com.lasttermjava.repository;

import khuong.com.lasttermjava.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findByPhoneNumber(String phoneNumber);

    @Override
    List<Profile> findAll();
}
