package Application.MBBS.service;

import Application.MBBS.entity.User;
import Application.MBBS.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User findByEmailOrPhone(String emailOrPhone) {
        if (emailOrPhone.contains("@")) {
            System.out.println("Foung email");
            return userRepository.findByEmail(emailOrPhone).orElse(null);
        } else {
            System.out.println("Foung phone");
            return userRepository.findByPhoneNo(emailOrPhone).orElse(null);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        if (userRepository.findByPhoneNo(user.getPhoneNo()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number already exists"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
