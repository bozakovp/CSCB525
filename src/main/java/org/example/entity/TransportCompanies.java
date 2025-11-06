package org.example.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TransportCompanies")
public class TransportCompanies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CompanyID")
    private Long id;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles = new HashSet<>();

    // JPA requires a no-arg constructor
    public TransportCompanies() {}
}
