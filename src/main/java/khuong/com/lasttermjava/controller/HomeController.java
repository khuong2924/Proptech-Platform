package khuong.com.lasttermjava.controller;

import jakarta.jws.WebParam;
import khuong.com.lasttermjava.dto.JobPostDTO;
import khuong.com.lasttermjava.dto.ProfileDTO;
import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.entity.Transaction;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.*;
import khuong.com.lasttermjava.service.ImageUploadService;
import khuong.com.lasttermjava.service.JobPostService;
import khuong.com.lasttermjava.service.ProfileService;
import khuong.com.lasttermjava.entity.JobPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import khuong.com.lasttermjava.utils.SessionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    private final ProfileService profileService;
    @Autowired
    private final JobPostRepository jobPostRepository;
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JobPostService jobPostService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private TransactionRepository transactionRepository;

    public HomeController(ProfileService profileService, JobPostRepository jobPostRepository, NotificationRepository notificationRepository, UserRepository userRepository, JobPostService jobPostService) {
        this.profileService = profileService;
        this.jobPostRepository = jobPostRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.jobPostService = jobPostService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        model.addAttribute("jobPosts", jobPosts);
        return "homePage";
    }



    @GetMapping("postDetail/{id}")
    public String showJobPostDetail(@PathVariable("id") Long id, Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        if (userId == null) {
            model.addAttribute("errors", "Bạn cần đăng nhập để truy cập thông tin cụ thể của bài đăng");
            return "error";
        }
        // Giả sử bạn lấy jobPost từ cơ sở dữ liệu
        JobPost jobPost = jobPostRepository.findById(id).get();
        model.addAttribute("jobPost", jobPost);
        return "postDetail";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        Long userId = SessionUtils.getCurrentUserId();

//        List<ApplyInfo> infos = applyInfoRepository.findAllByJobPostUserId(userId);
//        model.addAttribute("infos", infos);

        // Sort the list based on the createdAt field
//        jobPosts.sort((jobPost1, jobPost2) -> jobPost2.getCreatedAt().compareTo(jobPost1.getCreatedAt()));

        // Add the sorted list to the model
        model.addAttribute("jobPosts", jobPosts);
        return "homePage";
    }

    @GetMapping("/account")
    public String account(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);
        return "account";
    }

    @GetMapping("/personal-page")
        public String personalPage(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        if (userId == null) {
            model.addAttribute("errors", "Bạn cần đăng nhập để truy cập trang cá nhân");
            return "error";
        }
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);
        return "personalPage";
        }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("upload-post")
    public String uploadPost(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        if (userId == null) {
            model.addAttribute("errors", "Bạn cần đăng nhập để thực hiện đăng bài");
            return "error";
        }
        return "postListing";
    }

    @PostMapping("/create")
    public String createJobPost(
            @RequestParam("loaiBaiDang") String loaiBaiDang,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("loaiBatDongSan") String loaiBatDongSan,
            @RequestParam("dienTich") BigDecimal dienTich,
            @RequestParam("mucGia") BigDecimal mucGia,
            @RequestParam("donVi") String donVi,
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
            @RequestParam(value = "images", required = true) List<MultipartFile> images,
            RedirectAttributes redirectAttributes) {

        try {
            JobPost jobPost = new JobPost();

            Long userId = SessionUtils.getCurrentUserId();
            User user = userRepository.findById(userId).orElse(null);
            jobPost.setUser(user);
            jobPost.setLoaiBaiDang(loaiBaiDang);
            jobPost.setDiaChi(diaChi);
            jobPost.setLoaiBDS(loaiBatDongSan);
            jobPost.setDienTich(dienTich);
            jobPost.setMucGia(mucGia);
            jobPost.setDonVi(donVi);
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

            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    String imagePath = imageUploadService.uploadImage(image);
                    jobPost.getImageUrls().add(imagePath); // Thêm imagePath vào HashSet
                }
            }


            jobPostRepository.save(jobPost);

            redirectAttributes.addFlashAttribute("message", "Job post created successfully!");
            return "redirect:/home";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error creating job post.");
            return "redirect:/home";
        }
    }

    @GetMapping("transaction-create/{id}")
    public String transactionCreate(@PathVariable Long id, Model model) {
        JobPost jobPost = jobPostRepository.findById(id).get();
        model.addAttribute("jobPost", jobPost);
        model.addAttribute("jobPostId", id);

        return "transaction-create";
    }
//
//    @PostMapping("/create-new-transaction/{id}")
//    public String createOrUpdateTransaction(
//            @PathVariable("id") Long id,
//            @RequestParam("noiDung") String noiDung,
//            @RequestParam("sdtKhachHang") String sdtKhachHang,
//            @RequestParam("loaiHoSo") String loaiHoSo,
//            @RequestParam("giayToPhapLy") List<MultipartFile> giayToPhapLy,
//            @RequestParam("hopDongMuaBan") List<MultipartFile> hopDongMuaBan,
//            @RequestParam("trangThaiThanhToan") Boolean trangThaiThanhToan,
//            @RequestParam("hopDongThue") List<MultipartFile> hopDongThue,
//            @RequestParam("tienThue") BigDecimal tienThue,
//            @RequestParam("ngayTraDinhKy") String ngayTraDinhKy) throws IOException {
//
//        // Lấy JobPost
//        JobPost jobPost = jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Job post not found"));
//
//        // Tạo hoặc cập nhật Transaction
//        Transaction transaction = new Transaction();
//        transaction.setJobPost(jobPost);
//        transaction.setNoiDung(noiDung);
//        transaction.setSdtKhachHang(sdtKhachHang);
//        transaction.setLoaiHoSo(loaiHoSo);
//        transaction.setTrangThaiGiaoDich(false);
//
//        if (giayToPhapLy != null && !giayToPhapLy.isEmpty()) {
//            Set<String> imageUrls = new HashSet<>();
//            for (MultipartFile image : giayToPhapLy) {
//                String imageUrl = imageUploadService.uploadImage(image);
//                imageUrls.add(imageUrl);
//            }
//            transaction.setGiayToPhapLy(imageUrls);
//        }
//
//        if (hopDongMuaBan != null && !hopDongMuaBan.isEmpty()) {
//            Set<String> imageUrls = new HashSet<>();
//            for (MultipartFile image : hopDongMuaBan) {
//                String imageUrl = imageUploadService.uploadImage(image);
//                imageUrls.add(imageUrl);
//            }
//            transaction.setHopDongMuaBan(imageUrls);
//        }
//
//        transaction.setTrangThaiThanhToan(trangThaiThanhToan);
//        if (hopDongThue != null && !hopDongThue.isEmpty()) {
//            Set<String> imageUrls = new HashSet<>();
//            for (MultipartFile image : hopDongThue) {
//                String imageUrl = imageUploadService.uploadImage(image);
//                imageUrls.add(imageUrl);
//            }
//            transaction.setHopDongThue(imageUrls);
//        }
//        transaction.setTienThue(tienThue);
//        transaction.setNgayTraDinhKy(ngayTraDinhKy);
//
//        transactionRepository.save(transaction);
//
//        return "redirect:/home";
//    }

    @PostMapping("/create-new-transaction/{id}")
    public String createOrUpdateTransaction(
            @PathVariable("id") Long id,
            @RequestParam("noiDung") String noiDung,
            @RequestParam("sdtKhachHang") String sdtKhachHang,
            @RequestParam("loaiHoSo") String loaiHoSo,
            @RequestParam("giayToPhapLy") List<MultipartFile> giayToPhapLy,
            @RequestParam("hopDongMuaBan") List<MultipartFile> hopDongMuaBan,
            @RequestParam("trangThaiThanhToan") Boolean trangThaiThanhToan,
            @RequestParam("hopDongThue") List<MultipartFile> hopDongThue,
            @RequestParam("tienThue") BigDecimal tienThue,
            @RequestParam("ngayTraDinhKy") String ngayTraDinhKy) throws IOException {

        // Lấy JobPost
        JobPost jobPost = jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Job post not found"));

        // Tạo hoặc cập nhật Transaction
        Transaction transaction = new Transaction();
        transaction.setJobPost(jobPost);
        transaction.setNoiDung(noiDung);
        transaction.setSdtKhachHang(sdtKhachHang);
        transaction.setLoaiHoSo(loaiHoSo);
        transaction.setTrangThaiGiaoDich(false);

        // Kiểm tra giayToPhapLy
        if (giayToPhapLy != null && !giayToPhapLy.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : giayToPhapLy) {
                if (!image.isEmpty()) {
                    String imageUrl = imageUploadService.uploadImage(image);
                    imageUrls.add(imageUrl);
                }
            }
            transaction.setGiayToPhapLy(imageUrls);
        }

        // Kiểm tra hopDongMuaBan
        if (hopDongMuaBan != null && !hopDongMuaBan.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : hopDongMuaBan) {
                if (!image.isEmpty()) {
                    String imageUrl = imageUploadService.uploadImage(image);
                    imageUrls.add(imageUrl);
                }
            }
            transaction.setHopDongMuaBan(imageUrls);
        }

        // Kiểm tra hopDongThue
        if (hopDongThue != null && !hopDongThue.isEmpty()) {
            Set<String> imageUrls = new HashSet<>();
            for (MultipartFile image : hopDongThue) {
                if (!image.isEmpty()) {
                    String imageUrl = imageUploadService.uploadImage(image);
                    imageUrls.add(imageUrl);
                }
            }
            transaction.setHopDongThue(imageUrls);
        }

        transaction.setTrangThaiThanhToan(trangThaiThanhToan);
        transaction.setTienThue(tienThue);
        transaction.setNgayTraDinhKy(ngayTraDinhKy);

        transactionRepository.save(transaction);

        return "redirect:/home";
    }


}

