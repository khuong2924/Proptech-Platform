package khuong.com.lasttermjava.repository;

import khuong.com.lasttermjava.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByUserId(Long userId);

    Optional<JobPost> findById(Long ID);
    //    @Query("SELECT j FROM JobPost j WHERE (:jobType IS NULL OR j.jobType LIKE %:jobType%) " +
//            "AND (:date IS NULL OR j.start >= :date) " +  // Comparing the entire LocalDateTime
//            "AND (:startSalary IS NULL OR j.salary >= :startSalary) " +
//            "AND (:endSalary IS NULL OR j.salary <= :endSalary)")
//    List<JobPost> searchJobPosts(String jobType, LocalDateTime date, BigDecimal startSalary, BigDecimal endSalary);

}
