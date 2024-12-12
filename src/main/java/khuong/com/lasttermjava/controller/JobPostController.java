package khuong.com.lasttermjava.controller;

import khuong.com.lasttermjava.dto.JobPostDTO;
import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.entity.JobPost;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.JobPostRepository;
import khuong.com.lasttermjava.repository.UserRepository;
import khuong.com.lasttermjava.service.ImageUploadService;
import khuong.com.lasttermjava.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/jobPost")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;
    @Autowired
    private final JobPostRepository jobPostRepository;
    private final ImageUploadService imageUploadService;
    @Autowired
    private UserRepository userRepository;


    // Lấy danh sách tất cả JobPost
    @GetMapping
    public ResponseDTO<List<JobPostDTO>> getAllJobPosts() {
        ResponseDTO<List<JobPostDTO>> response = new ResponseDTO<>();
        response.setData(jobPostService.getAll());
        response.setStatus(200);
        return response;
    }

    // Cập nhật JobPost
    @PutMapping("/{id}")
    public ResponseDTO<Void> updateJobPost(@PathVariable("id") Long id, @RequestBody JobPostDTO jobPostDTO) {
        jobPostService.update(id, jobPostDTO);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Job post updated successfully")
                .build();
    }

    // Xóa JobPost
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteJobPost(@PathVariable("id") Long id) {
        jobPostService.delete(id);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Job post deleted successfully")
                .build();
    }

    // Tải lên ảnh và thêm vào danh sách URL của JobPost
    @PostMapping("/{jobPostId}/uploadImages")
    public ResponseDTO<Void> uploadImages(@PathVariable("jobPostId") Long jobPostId,
                                          @RequestParam("files") List<MultipartFile> files) throws IOException {
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(() -> new RuntimeException("Job post not found"));

        for (MultipartFile file : files) {
            String imageUrl = imageUploadService.uploadImage(file);
            jobPost.getImageUrls().add(imageUrl); // Thêm URL ảnh vào danh sách
        }
        jobPostRepository.save(jobPost);

        return ResponseDTO.<Void>builder()
                .status(200)
                .message("Images uploaded successfully")
                .build();
    }
    // Tạo hoặc cập nhật JobPost
    @PostMapping("/upload")
    public ResponseDTO<Void> createOrUpdateJobPost(
            @RequestParam("userId") Long userId,
            @RequestParam("loaiBaiDang") String loaiBaiDang,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("loaiBDS") String loaiBDS,
            @RequestParam("dienTich") BigDecimal dienTich,
            @RequestParam("mucGia") BigDecimal mucGia,
            @RequestParam(value = "donVi", required = false) String donVi,
            @RequestParam("giayToPhapLy") String giayToPhapLy,
            @RequestParam("noiThat") String noiThat,
            @RequestParam("soPhongNgu") Integer soPhongNgu,
            @RequestParam("soPhongTamVeSinh") Integer soPhongTamVeSinh,
            @RequestParam("huongNha") String huongNha,
            @RequestParam("huongBanCong") String huongBanCong,
            @RequestParam("duongVao") BigDecimal duongVao,
            @RequestParam("matTien") BigDecimal matTien,
            @RequestParam("tenChuBaiDang") String tenChuBaiDang,
            @RequestParam("soDienThoaiChu") String soDienThoaiChu,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("moTa") String moTa,
            @RequestParam("congKhai") boolean congKhai,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        // Lấy danh sách các JobPost
        List<JobPost> jobPosts = jobPostRepository.findAll();

        // Kiểm tra xem có JobPost nào với userId không
        JobPost jobPost = jobPosts.stream()
                .filter(post -> post.getUser().getId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    // Nếu không tìm thấy JobPost với userId, tạo một JobPost mới
                    JobPost newJobPost = new JobPost();
                    newJobPost.setUser(userRepository.findById(userId).get());
                    return newJobPost;
                });

        jobPost.setLoaiBaiDang(loaiBaiDang);
        jobPost.setDiaChi(diaChi);
        jobPost.setLoaiBDS(loaiBDS);
        jobPost.setDienTich(dienTich);
        jobPost.setMucGia(mucGia);
        jobPost.setDonVi(donVi != null ? donVi : "VND");
        jobPost.setGiayToPhapLy(giayToPhapLy);
        jobPost.setNoiThat(noiThat);
        jobPost.setSoPhongNgu(soPhongNgu);
        jobPost.setSoPhongTamVeSinh(soPhongTamVeSinh);
        jobPost.setHuongNha(huongNha);
        jobPost.setHuongBanCong(huongBanCong);
        jobPost.setDuongVao(duongVao);
        jobPost.setMatTien(matTien);
        jobPost.setTenChuBaiDang(tenChuBaiDang);
        jobPost.setSoDienThoaiChu(soDienThoaiChu);
        jobPost.setTieuDe(tieuDe);
        jobPost.setMoTa(moTa);
        jobPost.setCongKhai(congKhai);

        // Xử lý tải ảnh và thêm URL vào JobPost
        if (images != null && !images.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : images) {
                String imageUrl = imageUploadService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            jobPost.setImageUrls(imageUrls);
        }
        jobPostRepository.save(jobPost);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Job post created or updated successfully")
                .build();
    }
}
