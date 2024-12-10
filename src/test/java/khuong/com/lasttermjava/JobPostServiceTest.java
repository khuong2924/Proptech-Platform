package khuong.com.lasttermjava;


import khuong.com.lasttermjava.dto.JobPostDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.repository.JobPostRepository;
import khuong.com.lasttermjava.service.JobPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobPostServiceTest {

    @Mock
    private JobPostRepository jobPostRepository;

    @InjectMocks
    private JobPostService jobPostService;

    private JobPost jobPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobPost = new JobPost();
        jobPost.setId(1L);
        jobPost.setLoaiBaiDang("Bán");
        jobPost.setDiaChi("Hà Nội");
        jobPost.setLoaiBDS("Nhà ở");
        jobPost.setDienTich(BigDecimal.valueOf(100));
        jobPost.setMucGia(BigDecimal.valueOf(500000000));
        jobPost.setDonVi("VNĐ");
        jobPost.setGiayToPhapLy("Có");
        jobPost.setNoiThat("Full");
        jobPost.setSoPhongNgu(3);
        jobPost.setSoPhongTamVeSinh(2);
        jobPost.setHuongNha("Nam");
        jobPost.setHuongBanCong("Đông");
        jobPost.setMatTien(BigDecimal.valueOf(5));
        jobPost.setTenChuBaiDang("Chủ đầu tư");
        jobPost.setTieuDe("Nhà bán tại Hà Nội");
        jobPost.setMoTa("Nhà rộng rãi, đầy đủ tiện nghi");
        jobPost.setThoiGianDang(LocalDateTime.parse("2024-12-09"));
        jobPost.setCongKhai(true);
        jobPost.setImageUrls(null);
    }

    @Test
    void createJobPostTest() {
        when(jobPostRepository.save(any(JobPost.class))).thenReturn(jobPost);

        JobPostDTO jobPostDTO = new JobPostDTO(
                null, // id không cần thiết khi tạo mới
                null, // userId nếu cần có thể thêm vào
                "Bán",
                "Hà Nội",
                "Nhà ở",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(500000000),
                "VNĐ",
                "Có",
                "Full",
                3,
                2,
                "Nam",
                "Đông",
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(5),
                "Chủ đầu tư",
                "0123456789",
                "Nhà bán tại Hà Nội",
                "Nhà rộng rãi, đầy đủ tiện nghi",
                LocalDateTime.parse("2024-12-09T00:00:00"),
                true,
                Set.of("url1", "url2") // nếu cần thêm URL hình ảnh
        );

        jobPostService.create(jobPostDTO);

        verify(jobPostRepository, times(1)).save(any(JobPost.class));
    }

    @Test
    void updateJobPostTest() {
        when(jobPostRepository.findById(anyLong())).thenReturn(Optional.of(jobPost));
        when(jobPostRepository.save(any(JobPost.class))).thenReturn(jobPost);

        JobPostDTO jobPostDTO = new JobPostDTO(
                1L, // id của bài đăng
                null, // userId nếu cần có thể thêm vào
                "Thuê",
                "Hà Nội",
                "Nhà cho thuê",
                BigDecimal.valueOf(120),
                BigDecimal.valueOf(7000000),
                "VNĐ",
                "Có",
                "Full",
                4,
                3,
                "Bắc",
                "Tây",
                BigDecimal.valueOf(6),
                BigDecimal.valueOf(6),
                "Chủ cho thuê",
                "0123456789",
                "Nhà cho thuê tại Hà Nội",
                "Nhà cho thuê tiện nghi",
                LocalDateTime.parse("2024-12-09T00:00:00"),
                true,
                Set.of("url3", "url4") // nếu cần thêm URL hình ảnh
        );

        jobPostService.update(1L, jobPostDTO);

        verify(jobPostRepository, times(1)).save(any(JobPost.class));
    }


    @Test
    void getByIdTest() {
        when(jobPostRepository.findById(anyLong())).thenReturn(Optional.ofNullable(jobPost));

        JobPostDTO result = jobPostService.getById(1L);

        assertNotNull(result);
        assertEquals(jobPost.getTieuDe(), result.getTieuDe());
    }

    @Test
    void deleteJobPostTest() {
        when(jobPostRepository.findById(anyLong())).thenReturn(Optional.of(jobPost));

        jobPostService.delete(1L);

        verify(jobPostRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getByUserIdTest() {
        when(jobPostRepository.findByUserId(anyLong())).thenReturn(List.of(jobPost));

        List<JobPost> result = jobPostService.getByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
