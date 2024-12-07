package khuong.com.lasttermjava.service;


import jakarta.transaction.Transactional;
import khuong.com.lasttermjava.dto.UserDTO;
import khuong.com.lasttermjava.entity.Profile;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.ProfileRepository;
import khuong.com.lasttermjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProfileRepository profileRepo; // Thêm ProfileRepository để quản lý profile

    @Transactional
    public User createUser(User user) {
        // Lưu user mới
        User savedUser = userRepo.save(user);

        // Tạo profile rỗng tương ứng với user
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setAvtPhoto("https://res.cloudinary.com/dhp7ylyvh/image/upload/v1731477640/jthwtgc7gir7vczbloch.png");
        profile.setFullName(user.getUsername());

        // Lưu profile vào database
        profileRepo.save(profile);

        return savedUser;
    }

    @Transactional
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    private List<UserDTO> mapToDto(List<User> listEntity) {
        List<UserDTO> list = new ArrayList<>();
        for (User user : listEntity) {
            UserDTO dto = new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isPremium());
            list.add(dto);
        }
        return list;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepo.findAll();
        return mapToDto(users);
    }

    public void create(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setPremium(userDTO.isPremium());
        userRepo.save(user);
        // Tạo User và Profile tương ứng
        createUser(user);
    }

    public void update(Long id, UserDTO userDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setPremium(userDTO.isPremium());
        userRepo.save(user);
    }

    public UserDTO getById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isPremium());
    }

    public void delete(Long id) {
        userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.deleteById(id);
    }

    public boolean validateUser(String username, String password) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User validateGetUser(String username, String password) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
