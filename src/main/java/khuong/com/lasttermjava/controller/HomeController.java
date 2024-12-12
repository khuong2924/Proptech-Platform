package khuong.com.lasttermjava.controller;

import khuong.com.lasttermjava.dto.ProfileDTO;
import khuong.com.lasttermjava.entity.*;
import khuong.com.lasttermjava.repository.*;
import khuong.com.lasttermjava.service.ImageUploadService;
import khuong.com.lasttermjava.service.JobPostService;
import khuong.com.lasttermjava.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import khuong.com.lasttermjava.utils.SessionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        User user = userRepository.findById(jobPost.getUser().getId()).orElse(null);
        Profile profile = profileRepository.findByUserId(user.getId()).get();
        model.addAttribute("jobPost", jobPost);
        model.addAttribute("profile", profile);
        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        return "postDetail";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        Long userId = SessionUtils.getCurrentUserId();
        boolean checkUser = userId != null;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        jobPosts.sort((jobPost1, jobPost2) -> jobPost2.getThoiGianDang().compareTo(jobPost1.getThoiGianDang()));
        model.addAttribute("jobPosts", jobPosts);
        model.addAttribute("checkUser", checkUser);


        return "homePage";
    }

    @GetMapping("/pay-confirm")
    public String payConfirm(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);

        return "post-confirm";
    }

    @GetMapping("/account")
    public String account(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);
        boolean checkUser = userId != null;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
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

        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);

        List<Transaction> trans = transactionRepository.findByUserIdAndTrangThaiGiaoDich(userId, true);
        model.addAttribute("trans", trans);
        return "personalPage";
        }


    @GetMapping("/transaction-page")
    public String transactionPage(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        if (userId == null) {
            model.addAttribute("errors", "Bạn cần đăng nhập để trang quản lý giao dịch");
            return "error";
        }
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);
        List<Transaction> transactions = transactionRepository.findAll();
        model.addAttribute("transactions", transactions);
        List<Transaction> saleTransactions = transactionRepository.findByLoaiHoSo("Bán");
        model.addAttribute("saleTransactions", saleTransactions);

        List<Transaction> rentTransactions = transactionRepository.findByLoaiHoSo("Cho thuê");
        model.addAttribute("rentTransactions", rentTransactions);

        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        return "transaction-detail";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

    @GetMapping("upload-post")
    public String uploadPost(Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        if (userId == null) {
            model.addAttribute("errors", "Bạn cần đăng nhập để thực hiện đăng bài");
            return "error";
        }
        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        return "postListing";
    }

    @PostMapping("/create")
    public String createJobPost(
            @RequestParam("loaiBaiDang") String loaiBaiDang,
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("detailAddress") String detailAddress,
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
            String diaChi = detailAddress + ", " + district + ", " + province;
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
            // Thiết lập thời gian đăng
            jobPost.setThoiGianDang(LocalDateTime.now());
            jobPost.setCongKhai(congKhai);

            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    String imagePath = imageUploadService.uploadImage(image);
                    jobPost.getImageUrls().add(imagePath); // Thêm imagePath vào HashSet
                }
            }
            jobPostRepository.save(jobPost);

            redirectAttributes.addFlashAttribute("message", "Job post created successfully!");
            return "redirect:/pay-confirm";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error creating job post.");
            return "redirect:/pay-confirm";
        }
    }

    @GetMapping("transaction-create/{id}")
    public String transactionCreate(@PathVariable Long id, Model model) {
        JobPost jobPost = jobPostRepository.findById(id).get();
        model.addAttribute("jobPost", jobPost);
        model.addAttribute("jobPostId", id);
        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findAll();
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);

        return "transaction-create";
    }

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
            @RequestParam("tienThue") String tienThueStr,  // Thay vì BigDecimal, nhận dưới dạng String
            @RequestParam("ngayTraDinhKy") String ngayTraDinhKy,
            @RequestParam("trangThaiDatCoc") Boolean trangThaiDatCoc) throws IOException {

        // Lấy JobPost
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        // Tạo hoặc cập nhật Transaction
        Transaction transaction = new Transaction();
        transaction.setJobPost(jobPost);
        transaction.setNoiDung(noiDung);
        transaction.setSdtKhachHang(sdtKhachHang);
        transaction.setLoaiHoSo(loaiHoSo);
        transaction.setTrangThaiGiaoDich(false);
        transaction.setTrangThaiDatCoc(trangThaiDatCoc);
        Long userId = SessionUtils.getCurrentUserId();
        transaction.setUser(userRepository.findById(userId).get());

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

        // Xử lý tienThue
        BigDecimal tienThue = null;
        try {
            if (tienThueStr != null && !tienThueStr.isEmpty()) {
                tienThue = new BigDecimal(tienThueStr); // Chuyển đổi String thành BigDecimal
            }
        } catch (NumberFormatException e) {
            // Nếu chuyển đổi không thành công, tienThue sẽ giữ giá trị null
        }
        transaction.setTrangThaiThanhToan(trangThaiThanhToan);
        transaction.setTienThue(tienThue);
        transaction.setNgayTraDinhKy(ngayTraDinhKy);

        transactionRepository.save(transaction);

        // Thêm Notification
        Notification noti = new Notification();
        noti.setJobPost(jobPost);
        Profile profile = profileRepository.findByPhoneNumber(sdtKhachHang).get();
        noti.setUser(profile.getUser());
        noti.setFlagged(false);
        noti.setContent(noiDung);
        noti.setTransaction(transaction);
        notificationRepository.save(noti);

        return "redirect:/home";
    }


    @PostMapping("updateTrans/{id}")
    public String updateTrans(@PathVariable Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id).get();
        transaction.setTrangThaiGiaoDich(true);

        Notification noti = notificationRepository.findByTransaction(transaction).get();
        notificationRepository.delete(noti);
        return "redirect:/home";

    }

    @PostMapping("refuseTrans/{id}")
    public String refuseTrans(@PathVariable Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id).get();

        Notification noti = notificationRepository.findByTransaction(transaction).get();
        notificationRepository.delete(noti);
        return "redirect:/home";

    }

    @GetMapping("/transaction/{id}")
    public String transaction(@PathVariable Long id, Model model) {
        Long userId = SessionUtils.getCurrentUserId();
        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);


        Transaction transaction = transactionRepository.findById(id).get();
        model.addAttribute("transaction", transaction);
        return "transaction";
    }

    @GetMapping("/payment-detail")
    public String paymentDetail(Model model) {
        return "premium-register";
    }

    @PostMapping("/v1/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("sdt") String sdt,
            RedirectAttributes redirectAttributes) {

        try {
            // Kiểm tra nếu tên đăng nhập đã tồn tại
            if (userRepository.findByUsername(username).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Username already exists.");
                return "redirect:/register";
            }

            // Tạo đối tượng User và lưu thông tin
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);// Mã hóa mật khẩu
            user.setRole(role);
            user.setPremium(false);

            // Tạo đối tượng Profile nếu có ảnh đại diện (nếu người dùng upload ảnh)
                Profile profile = new Profile();
                profile.setUser(user);
                profile.setFullName(fullname);
                profile.setEmail(email);
                profile.setPhoneNumber(sdt);
                profileRepository.save(profile);
                user.setProfile(profile);

            // Lưu người dùng vào cơ sở dữ liệu
            userRepository.save(user);

            // Thêm thông báo và chuyển hướng đến trang login hoặc trang khác
            redirectAttributes.addFlashAttribute("message", "Account created successfully!");
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error creating account.");
            return "redirect:/register"; // Quay lại trang đăng ký nếu có lỗi
        }
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signUp";
    }

    @PostMapping("/jobPost/chinhsua")
    public String createNewJobPost(@RequestParam("id") Long id,
                                   Model model) {

        Long userId = SessionUtils.getCurrentUserId();

        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        // Tạo đối tượng jobPost từ dữ liệu gửi tới
        JobPost jobPost = jobPostRepository.findById(id).get();


        // Thêm vào mô hình để hiển thị trên trang
        model.addAttribute("jobPost", jobPost);

        return "post-chinhsua"; // trả về trang create-new
    }

    @PostMapping("jobPost/delete/{id}")
    public String deleteJobPost(@PathVariable Long id, Model model) {
        JobPost jp = jobPostRepository.findById(id).get();
        jobPostRepository.delete(jp);

        Long userId = SessionUtils.getCurrentUserId();
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);

        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        return "redirect:/personal-page";
    }

    @PostMapping("transaction/delete/{id}")
    public String deleteTran(@PathVariable Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id).get();
        transactionRepository.delete(transaction);

        Long userId = SessionUtils.getCurrentUserId();
        ProfileDTO profileDTO = profileService.getByUserId(userId);
        model.addAttribute("profile", profileDTO);
        List<JobPost> jobPosts = jobPostRepository.findByUserId(userId);
        model.addAttribute("jobPosts", jobPosts);

        boolean checkUser = true;
        List<Notification> listNotis = notificationRepository.findByUser(userRepository.findById(userId).get());
        model.addAttribute("listNotis", listNotis);
        model.addAttribute("checkUser", checkUser);
        return "redirect:/transaction-page";


    }



}

