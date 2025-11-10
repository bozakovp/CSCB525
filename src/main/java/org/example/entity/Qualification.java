package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Qualifications")
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QualificationID")
    private Long id;

    @Column(name = "Name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "Description")
    private String description;

    @ManyToMany(mappedBy = "qualifications")
    private Set<TransportEmployee> employees = new HashSet<>();

    @ManyToMany(mappedBy = "requiredQualifications")
    private Set<TransportType> transportTypes = new HashSet<>();

    public Qualification() {}
}