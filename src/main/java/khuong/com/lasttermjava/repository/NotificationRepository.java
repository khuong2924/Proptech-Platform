package khuong.com.lasttermjava.repository;

import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.entity.Notification;
import khuong.com.lasttermjava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long id);

//    @Query("SELECT n FROM Notification n WHERE DATE(n.jobPost.start) = :date and n.user.id = :userId")
//    List<Notification> findByJobPostStartDateAndUserId(LocalDate date, Long userId);

    Optional<Notification> findByUserAndJobPost(User user, JobPost jobPost);

//
//    @Query("SELECT n FROM Notification n ORDER BY n.isFlagged DESC, n.jobPost.start DESC")
//    List<Notification> findAllSorted();

}

