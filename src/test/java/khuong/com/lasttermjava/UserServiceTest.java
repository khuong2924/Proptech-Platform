package khuong.com.lasttermjava;


import khuong.com.lasttermjava.dto.UserDTO;
import khuong.com.lasttermjava.entity.User;
import khuong.com.lasttermjava.repository.UserRepository;
import khuong.com.lasttermjava.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole("USER");
        user.setPremium(false);
    }

    @Test
    void createUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void saveUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    void getAllTest() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> userDTOList = userService.getAll();

        assertNotNull(userDTOList);
        assertEquals(1, userDTOList.size());
    }

    @Test
    void createTest() {
        UserDTO userDTO = new UserDTO(1L, "testuser", "testpassword", "USER", false);

        doNothing().when(userRepository).save(any(User.class));
        userService.create(userDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateTest() {
        UserDTO userDTO = new UserDTO(1L, "updateduser", "updatedpassword", "USER", false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.update(1L, userDTO);

        assertEquals("updateduser", user.getUsername());
        assertEquals("updatedpassword", user.getPassword());
    }

    @Test
    void getByIdTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getById(1L);

        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    void deleteTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void validateUserTest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        boolean result = userService.validateUser("testuser", "testpassword");

        assertTrue(result);
    }

    @Test
    void validateUserTestFail() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        boolean result = userService.validateUser("testuser", "wrongpassword");

        assertFalse(result);
    }
}
