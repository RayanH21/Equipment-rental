package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.dto.RegisterRequest;
import be.rayanhaddou.equipmentrental.model.AppUser;
import be.rayanhaddou.equipmentrental.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hash = passwordEncoder.encode(request.getPassword());
        userRepository.save(new AppUser(email, hash, "USER"));
    }
}