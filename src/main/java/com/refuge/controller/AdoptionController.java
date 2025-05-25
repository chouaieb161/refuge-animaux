package com.refuge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.refuge.model.Adoption;
import com.refuge.service.AdoptionService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Autowired
    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public List<Adoption> getAllAdoptions() {
        return adoptionService.getAllAdoptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoptionById(@PathVariable Long id) {
        Adoption adoption = adoptionService.getAdoptionById(id);
        return ResponseEntity.ok(adoption);
    }

    @PostMapping
    public ResponseEntity<Adoption> createAdoption(@Valid @RequestBody Adoption adoption,
                                                   @RequestParam Long animalId,
                                                   @RequestParam Long familleId) {
        Adoption newAdoption = adoptionService.createAdoption(adoption, animalId, familleId);
        return new ResponseEntity<>(newAdoption, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adoption> updateAdoption(@PathVariable Long id, @Valid @RequestBody Adoption adoption) {
        Adoption updatedAdoption = adoptionService.updateAdoption(id, adoption);
        return ResponseEntity.ok(updatedAdoption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/famille/{familleId}")
    public List<Adoption> getAdoptionsByFamille(@PathVariable Long familleId) {
        return adoptionService.getAdoptionsByFamille(familleId);
    }

    @GetMapping("/periode")
    public List<Adoption> getAdoptionsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return adoptionService.getAdoptionsBetweenDates(debut, fin);
    }

    @PostMapping("/{id}/suivi-post")
    public ResponseEntity<Void> enregistrerSuiviPost(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSuivi) {
        adoptionService.enregistrerSuiviPost(id, dateSuivi);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sans-suivi")
    public List<Adoption> getAdoptionsSansSuivi() {
        return adoptionService.getAdoptionsSansSuivi();
    }
}
