package khuong.com.lasttermjava.controller;
import khuong.com.lasttermjava.dto.ResponseDTO;
import khuong.com.lasttermjava.dto.UserDTO;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.UserRepository;
import khuong.com.lasttermjava.service.ImageUploadService;
import khuong.com.lasttermjava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseDTO<List<UserDTO>> getAll() {
        ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(userService.getAll());
        responseDTO.setStatus(200);
        return responseDTO;
    }

    @PostMapping("/create")
    public ResponseDTO<Void> create(@RequestBody UserDTO userDTO) {
        userService.create(userDTO);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .status(201)
                .message("Sua thanh cong nguoi dung")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response).getBody();
    }

    @PutMapping("{id}")
    public ResponseDTO<Void> update(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Sua thanh cong nguoi dung")
                .build();
    }

    @DeleteMapping("{id}")
    public ResponseDTO<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseDTO.<Void>builder()
                .status(201)
                .message("Xoa thanh cong nguoi dung")
                .build();
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<UserDTO> users = userService.getAll();
        model.addAttribute("users", users);
        return "users";
    }
}
