package be.rayanhaddou.equipmentrental.dto;

import be.rayanhaddou.equipmentrental.model.ReservationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;
    private String productName;
    private int quantity;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ReservationStatus status;
}