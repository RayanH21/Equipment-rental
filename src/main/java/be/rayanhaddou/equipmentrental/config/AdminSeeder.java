package be.rayanhaddou.equipmentrental.config;

import be.rayanhaddou.equipmentrental.model.AppUser;
import be.rayanhaddou.equipmentrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@school.be}")
    private String adminEmail;

    // leeg default => seed niks als je het niet instelt
    @Value("${admin.password:}")
    private String adminPassword;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        String email = adminEmail.trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }

        String hash = passwordEncoder.encode(adminPassword);
        userRepository.save(new AppUser(email, hash, "ADMIN"));
    }
}