package khuong.com.lasttermjava.controller;

import khuong.com.lasttermjava.dto.ProfileDTO;
import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.entity.Profile;
import khuong.com.lasttermjava.repository.ProfileRepository;
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

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;


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
    public String updateCoverPhoto(@PathVariable("profileId") Long profileId, @RequestParam("file") MultipartFile file) throws IOException {
        String img_url = imageUploadService.uploadImage(file);
        Profile profile = profileRepository.findById(profileId).orElseThrow(RuntimeException::new);
        profile.setAvtPhoto(img_url);
        profileRepository.save(profile);
        return "Cover photo updated successfully";
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
            @RequestParam("user_id") Long userId) throws IOException {

        String avtPhotoUrl = null;

        if (avtPhoto != null && !avtPhoto.isEmpty()) {
            avtPhotoUrl = imageUploadService.uploadImage(avtPhoto);
        }


        Profile profile = profileRepository.findByUserId(userId).get();
        System.out.println("Before P: " + profile.getAvtPhoto());

        String afterP = profile.getAvtPhoto();


        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setBirthDate(birthDate);
        profile.setGender(gender);
        profile.setBio(bio);
        profile.setAddress(address);

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
