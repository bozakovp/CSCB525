package org.example.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TransportTypes")
public class TransportType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransportTypeID")
    private Long id;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @Column(name = "Description")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "TransportTypeQualifications",
        joinColumns = @JoinColumn(name = "TransportTypeID"),
        inverseJoinColumns = @JoinColumn(name = "QualificationID")
    )
    private Set<Qualification> requiredQualifications = new HashSet<>();

    @OneToMany(mappedBy = "transportType")
    private Set<Transport> transports = new HashSet<>();

    public TransportType() {}
}
