package khuong.com.lasttermjava;

import khuong.com.lasttermjava.dto.ProfileDTO;
import khuong.com.lasttermjava.entity.Profile;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.ProfileRepository;
import khuong.com.lasttermjava.repository.UserRepository;
import khuong.com.lasttermjava.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile profile;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profile = new Profile();
        profile.setId(1L);
        profile.setFullName("Nguyen Van A");
        profile.setEmail("email@example.com");
        profile.setBirthDate("1990-01-01");
        profile.setGender("Male");
        profile.setBio("Software Developer");
        profile.setAddress("Hanoi");
        profile.setAvtPhoto("avatar.jpg");
        profile.setPhoneNumber("0123456789");
        profile.setUser_id(1L);

        profileDTO = new ProfileDTO(
                1L, "Nguyen Van A", "email@example.com", "1990-01-01",
                "Male", "Software Developer", "Hanoi", "avatar.jpg", 1L, "0123456789"
        );
    }

    @Test
    void createProfileTest() {
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        profileService.create(profileDTO);

        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfileTest() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        profileDTO.setFullName("Updated Name");
        profileService.update(1L, profileDTO);

        assertEquals("Updated Name", profile.getFullName());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void getProfileByUserIdTest() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Optional.of(profile));

        Profile foundProfile = profileService.getProfileByUserId(1L);

        assertEquals(profile.getFullName(), foundProfile.getFullName());
        verify(profileRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void getProfileByUserIdNotFoundTest() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> profileService.getProfileByUserId(1L));
        assertEquals("Profile not found for user ID: 1", thrown.getMessage());
    }

    @Test
    void deleteProfileTest() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));

        profileService.delete(1L);

        verify(profileRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProfileNotFoundTest() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> profileService.delete(1L));
        assertEquals("Profile not found", thrown.getMessage());
    }

    @Test
    void updateProfileFromSessionTest() {
        when(profileRepository.findByUserId(anyLong())).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDTO updatedProfileDTO = new ProfileDTO(
                1L, "Updated Name", "newemail@example.com", "1990-01-01",
                "Male", "Updated Bio", "New Address", "newavatar.jpg", 1L, "0987654321"
        );

        String result = profileService.updateProfile(updatedProfileDTO, null, null);  // Use null for session and model for simplicity
        assertEquals("redirect:/profile", result);
    }
}
