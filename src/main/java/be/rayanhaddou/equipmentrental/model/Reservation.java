        package be.rayanhaddou.equipmentrental.model;

        import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDate;
        import java.time.LocalDateTime;

        @Entity
        @Getter @Setter
        @NoArgsConstructor @AllArgsConstructor
        @Builder
        public class Reservation {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            @ManyToOne(optional = false)
            private AppUser user;

            @ManyToOne(optional = false)
            private Product product;

            @Column(nullable = false)
            private int quantity;

            @Column(nullable = false)
            private LocalDate fromDate;

            @Column(nullable = false)
            private LocalDate toDate;

            @Enumerated(EnumType.STRING)
            @Column(nullable = false)
            private ReservationStatus status;

            @Column(nullable = false)
            private LocalDateTime createdAt;
        }