package com.refuge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisiteMedicale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de visite est obligatoire")
    private LocalDate dateVisite;

    @NotBlank(message = "Le motif de la visite est obligatoire")
    private String motif;

    @Column(length = 1000)
    private String observations;

    private String traitement;

    @NotBlank(message = "Le nom du vétérinaire est obligatoire")
    private String veterinaire;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
