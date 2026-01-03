package be.rayanhaddou.equipmentrental.service;

import be.rayanhaddou.equipmentrental.model.*;
import be.rayanhaddou.equipmentrental.repository.AppUserRepository;
import be.rayanhaddou.equipmentrental.repository.ProductRepository;
import be.rayanhaddou.equipmentrental.repository.ReservationRepository;
import be.rayanhaddou.equipmentrental.dto.ReservationResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ProductRepository productRepository,
                              AppUserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private AppUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }

        Object principal = auth.getPrincipal();
        String usernameOrEmail;

        if (principal instanceof UserDetails ud) {
            usernameOrEmail = ud.getUsername();
        } else {
            usernameOrEmail = String.valueOf(principal);
        }

        if ("anonymousUser".equalsIgnoreCase(usernameOrEmail)) {
            throw new AccessDeniedException("Not authenticated");
        }

        return userRepository.findByEmailIgnoreCase(usernameOrEmail)
                .orElseThrow(() -> new IllegalStateException("User not found: " + usernameOrEmail));
    }

    @Transactional
    public void checkout(List<CartItem> cartItems) {
        AppUser user = currentUser();

        // 1) stock check eerst (alles of niets)
        for (CartItem item : cartItems) {
            Product p = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

            if (p.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for: " + p.getName());
            }
        }

        // 2) stock verminderen + reservations maken
        for (CartItem item : cartItems) {
            Product p = productRepository.findById(item.getProductId()).orElseThrow();

            p.setQuantity(p.getQuantity() - item.getQuantity());
            productRepository.save(p);

            Reservation r = Reservation.builder()
                    .user(user)
                    .product(p)
                    .quantity(item.getQuantity())
                    .fromDate(item.getFromDate())
                    .toDate(item.getToDate())
                    .status(ReservationStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .build();

            reservationRepository.save(r);
        }
    }

    public List<ReservationResponse> myReservations() {
        AppUser user = currentUser();

        List<Reservation> reservations =
                reservationRepository.findByUserAndStatusOrderByCreatedAtDesc(
                        user,
                        ReservationStatus.ACTIVE
                );

        return reservations.stream()
                .map(r -> ReservationResponse.builder()
                        .id(r.getId())
                        .productName(r.getProduct().getName())
                        .quantity(r.getQuantity())
                        .fromDate(r.getFromDate())
                        .toDate(r.getToDate())
                        .status(r.getStatus())
                        .build()
                )
                .toList();
    }


    @Transactional
    public void cancel(Long reservationId) {
        AppUser user = currentUser();

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!r.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not your reservation");
        }

        if (r.getStatus() == ReservationStatus.CANCELED) {
            return;
        }

        r.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(r);

        Product p = r.getProduct();
        p.setQuantity(p.getQuantity() + r.getQuantity());
        productRepository.save(p);
    }


}