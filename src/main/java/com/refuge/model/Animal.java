package com.refuge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'animal est obligatoire")
    private String nom;

    @NotNull(message = "L'espèce de l'animal est obligatoire")
    @Enumerated(EnumType.STRING)
    private EspeceAnimal espece;

    @Min(value = 0, message = "L'âge ne peut pas être négatif")
    private int age;

    @Column(length = 500)
    private String description;

    private String etatSante;

    @Enumerated(EnumType.STRING)
    private StatutAnimal statut = StatutAnimal.DISPONIBLE;

    @Column(length = 255)
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "refuge_id")
    private Refuge refuge;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisiteMedicale> visitesMedicales = new ArrayList<>();

    @OneToOne(mappedBy = "animal")
    private Adoption adoption;

    private LocalDate dateArrivee = LocalDate.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public EspeceAnimal getEspece() {
        return espece;
    }

    public void setEspece(EspeceAnimal espece) {
        this.espece = espece;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtatSante() {
        return etatSante;
    }

    public void setEtatSante(String etatSante) {
        this.etatSante = etatSante;
    }

    public StatutAnimal getStatut() {
        return statut;
    }

    public void setStatut(StatutAnimal statut) {
        this.statut = statut;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Refuge getRefuge() {
        return refuge;
    }

    public void setRefuge(Refuge refuge) {
        this.refuge = refuge;
    }

    public List<VisiteMedicale> getVisitesMedicales() {
        return visitesMedicales;
    }

    public void setVisitesMedicales(List<VisiteMedicale> visitesMedicales) {
        this.visitesMedicales = visitesMedicales;
    }

    public Adoption getAdoption() {
        return adoption;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }
}