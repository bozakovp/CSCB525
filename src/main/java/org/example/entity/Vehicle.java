package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VehicleID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CompanyID", nullable = false)
    private TransportCompany company;

    @Column(name = "Type", nullable = false, length = 255)
    private String type;

    @OneToMany(mappedBy = "vehicle")
    private Set<Transport> transports = new HashSet<>();

    public Vehicle() {}
}