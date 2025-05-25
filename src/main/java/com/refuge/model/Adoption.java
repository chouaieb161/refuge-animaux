package com.refuge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date d'adoption est obligatoire")
    private LocalDate dateAdoption;

    @Column(length = 500)
    private String commentaires;

    private Double fraisAdoption;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    @OneToOne
    @JoinColumn(name = "animal_id", unique = true)
    private Animal animal;

    private LocalDate dateContrat;

    private boolean suiviPost = false;

    private LocalDate dateSuivi;
}
