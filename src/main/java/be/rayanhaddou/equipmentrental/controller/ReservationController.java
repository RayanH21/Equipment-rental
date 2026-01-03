package be.rayanhaddou.equipmentrental.controller;

import be.rayanhaddou.equipmentrental.model.Reservation;
import be.rayanhaddou.equipmentrental.service.ReservationService;
import org.springframework.web.bind.annotation.*;
import be.rayanhaddou.equipmentrental.dto.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/me")
    public List<ReservationResponse> myReservations() {
        return reservationService.myReservations();
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        reservationService.cancel(id);
    }

}