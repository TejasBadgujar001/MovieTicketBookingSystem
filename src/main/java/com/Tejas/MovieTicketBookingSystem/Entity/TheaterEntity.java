package com.Tejas.MovieTicketBookingSystem.Entity;

import com.Tejas.MovieTicketBookingSystem.Enum.TheaterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_theater")
public class TheaterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String state;

    private String address;

    @Enumerated(EnumType.STRING)
    private TheaterStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity userEntity;

    @PrePersist
    public void setDefaultStatus() {
        if (status == null) {
            status = TheaterStatus.PENDING;
        }
    }

}
