package be.rayanhaddou.equipmentrental.repository;

import be.rayanhaddou.equipmentrental.model.AppUser;
import be.rayanhaddou.equipmentrental.model.Reservation;
import be.rayanhaddou.equipmentrental.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserAndStatusOrderByCreatedAtDesc(
            AppUser user,
            ReservationStatus status
    );
}
