package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Transports")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransportID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VehicleID", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "DriverID", nullable = false)
    private TransportEmployee driver;

    @ManyToOne
    @JoinColumn(name = "TransportTypeID", nullable = false)
    private TransportType transportType;

    @Column(name = "StartPoint", nullable = false, length = 255)
    private String startPoint;

    @Column(name = "EndPoint", nullable = false, length = 255)
    private String endPoint;

    @Column(name = "DepartureDate", nullable = false)
    private LocalDateTime departureDate;

    @Column(name = "ArrivalDate")
    private LocalDateTime arrivalDate;

    @Column(name = "CargoWeight")
    private Float cargoWeight;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Transport() {}
}
