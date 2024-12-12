package khuong.com.lasttermjava.controller;

import khuong.com.lasttermjava.dto.ProfileDTO;
import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.entity.Profile;
import khuong.com.lasttermjava.repository.ProfileRepository;
import khuong.com.lasttermjava.repository.UserRepository;
import khuong.com.lasttermjava.service.ImageUploadService;
import khuong.com.lasttermjava.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseDTO<List<ProfileDTO>> getAllProfiles() {
        ResponseDTO<List<ProfileDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(profileService.getAll());
        responseDTO.setStatus(200);

        return responseDTO;
    }




    @GetMapping("/all")
    public ResponseDTO<List<ProfileDTO>> getAll() {
        ResponseDTO<List<ProfileDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(profileService.getAll());
        responseDTO.setStatus(200);
        return responseDTO;

    }


    @PutMapping("{id}")
    public ResponseDTO<Void> update(@PathVariable("id") Long id, @RequestBody ProfileDTO profileDTO) {
        profileService.update(id, profileDTO);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Sua thanh cong profile")
                .build();
    }

    @DeleteMapping("{id}")
    public ResponseDTO<Void> delete(@PathVariable("id") Long id) {
        profileService.delete(id);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Xoa thanh cong nguoi dung")
                .build();
    }

//    @GetMapping("/profiles")
//    public String getProfiles(Model model) {
//        List<ProfileDTO> profiles= profileService.getAll();
//        model.addAttribute("users", profiles);
//        return "profiles";
//    }

    //upload image:
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/{profileId}/updateAvt")
    public ResponseEntity<String> updateCoverPhoto(@PathVariable("profileId") Long profileId, @RequestParam("file") MultipartFile file) {
        try {
            // Upload ảnh và lấy URL
            String img_url = imageUploadService.uploadImage(file);

            // Tìm Profile, ném ngoại lệ nếu không tìm thấy
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + profileId));

            // Cập nhật ảnh đại diện
            profile.setAvtPhoto(img_url);
            profileRepository.save(profile);

            return ResponseEntity.ok("Cover photo updated successfully");
        } catch (RuntimeException e) {
            // Xử lý lỗi nếu Profile không tồn tại hoặc các lỗi khác
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            // Xử lý lỗi khi upload ảnh
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the image: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }




    // Endpoint để cập nhật profile
    @PostMapping("/upload")
    public ResponseEntity<ResponseDTO<Void>> createOrUpdateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("gender") String gender,
            @RequestParam("bio") String bio,
            @RequestParam("address") String address,
            @RequestParam(value = "avtPhoto", required = false) MultipartFile avtPhoto,
            @RequestParam("user_id") Long userId,
            @RequestParam("phoneNumber") String phoneNumber ) throws IOException {

        String avtPhotoUrl = null;

        if (avtPhoto != null && !avtPhoto.isEmpty()) {
            avtPhotoUrl = imageUploadService.uploadImage(avtPhoto);
        }
        // Tìm profile theo userId
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        Profile profile;

        if (profileOptional.isEmpty()) {
            // Nếu không tìm thấy profile, tạo mới
            profile = new Profile();
            profile.setUser(userRepository.findById(userId).get()); // Gán userId cho profile mới
        } else {
            // Nếu tìm thấy profile, sử dụng profile cũ
            profile = profileOptional.get();
        }


        System.out.println("Before P: " + profile.getAvtPhoto());

        String afterP = profile.getAvtPhoto();


        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setBirthDate(birthDate);
        profile.setGender(gender);
        profile.setBio(bio);
        profile.setAddress(address);
        profile.setPhoneNumber(phoneNumber);

        if(avtPhotoUrl != null) {
            profile.setAvtPhoto(avtPhotoUrl);
        } else {
            profile.setAvtPhoto(afterP);
        }

        System.out.println("After P: " + profile.getAvtPhoto());
        profileRepository.save(profile);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(200);
        responseDTO.setMessage("Profile updated successfully");

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/profiles")
                .body(responseDTO);
    }

}
