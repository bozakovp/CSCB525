package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Employees")
public class TransportEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CompanyID", nullable = false)
    private TransportCompany company;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "EmployeeQualifications",
        joinColumns = @JoinColumn(name = "EmployeeID"),
        inverseJoinColumns = @JoinColumn(name = "QualificationID")
    )
    private Set<Qualification> qualifications = new HashSet<>();

    @OneToMany(mappedBy = "driver")
    private Set<Transport> transports = new HashSet<>();

    public TransportEmployee() {}
}