package com.refuge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.refuge.model.VisiteMedicale;
import com.refuge.service.VisiteMedicaleService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visites-medicales")
public class VisiteMedicaleController {

    private final VisiteMedicaleService visiteMedicaleService;

    @Autowired
    public VisiteMedicaleController(VisiteMedicaleService visiteMedicaleService) {
        this.visiteMedicaleService = visiteMedicaleService;
    }

    @GetMapping
    public List<VisiteMedicale> getAllVisitesMedicales() {
        return visiteMedicaleService.getAllVisitesMedicales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisiteMedicale> getVisiteMedicaleById(@PathVariable Long id) {
        VisiteMedicale visiteMedicale = visiteMedicaleService.getVisiteMedicaleById(id);
        return ResponseEntity.ok(visiteMedicale);
    }

    @PostMapping
    public ResponseEntity<VisiteMedicale> createVisiteMedicale(@Valid @RequestBody VisiteMedicale visiteMedicale,
                                                               @RequestParam Long animalId) {
        VisiteMedicale newVisiteMedicale = visiteMedicaleService.createVisiteMedicale(visiteMedicale, animalId);
        return new ResponseEntity<>(newVisiteMedicale, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisiteMedicale> updateVisiteMedicale(@PathVariable Long id,
                                                               @Valid @RequestBody VisiteMedicale visiteMedicale) {
        VisiteMedicale updatedVisiteMedicale = visiteMedicaleService.updateVisiteMedicale(id, visiteMedicale);
        return ResponseEntity.ok(updatedVisiteMedicale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisiteMedicale(@PathVariable Long id) {
        visiteMedicaleService.deleteVisiteMedicale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/animal/{animalId}")
    public List<VisiteMedicale> getVisitesMedicalesByAnimal(@PathVariable Long animalId) {
        return visiteMedicaleService.getVisiteMedicalesByAnimal(animalId);
    }

    @GetMapping("/periode")
    public List<VisiteMedicale> getVisitesMedicalesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return visiteMedicaleService.getVisiteMedicalesBetweenDates(debut, fin);
    }

    @PostMapping("/animal/{animalId}/guerison")
    public ResponseEntity<Void> declarerAnimalGueri(@PathVariable Long animalId) {
        visiteMedicaleService.declarerAnimalGueri(animalId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/veterinaire")
    public List<VisiteMedicale> getVisitesMedicalesByVeterinaire(@RequestParam String veterinaire) {
        return visiteMedicaleService.getVisitesMedicalesByVeterinaire(veterinaire);
    }

    @GetMapping("/motif")
    public List<VisiteMedicale> getVisitesMedicalesByMotif(@RequestParam String motif) {
        return visiteMedicaleService.getVisitesMedicalesByMotif(motif);
    }
}
