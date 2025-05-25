package com.refuge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.refuge.model.Famille;
import com.refuge.service.FamilleService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/familles")
public class FamilleController {

    private final FamilleService familleService;

    @Autowired
    public FamilleController(FamilleService familleService) {
        this.familleService = familleService;
    }

    @GetMapping
    public List<Famille> getAllFamilles() {
        return familleService.getAllFamilles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Famille> getFamilleById(@PathVariable Long id) {
        Famille famille = familleService.getFamilleById(id);
        return ResponseEntity.ok(famille);
    }

    @PostMapping
    public ResponseEntity<Famille> createFamille(@Valid @RequestBody Famille famille) {
        Famille newFamille = familleService.createFamille(famille);
        return new ResponseEntity<>(newFamille, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Famille> updateFamille(@PathVariable Long id, @Valid @RequestBody Famille famille) {
        Famille updatedFamille = familleService.updateFamille(id, famille);
        return ResponseEntity.ok(updatedFamille);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamille(@PathVariable Long id) {
        familleService.deleteFamille(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/nom")
    public List<Famille> searchFamillesByNom(@RequestParam String nom) {
        return familleService.searchFamillesByNom(nom);
    }

    @GetMapping("/search/email")
    public List<Famille> searchFamillesByEmail(@RequestParam String email) {
        return familleService.searchFamillesByEmail(email);
    }

    @GetMapping("/{id}/nombre-adoptions")
    public ResponseEntity<Integer> getNombreAdoptionsByFamille(@PathVariable Long id) {
        int nombreAdoptions = familleService.getNombreAdoptionsByFamille(id);
        return ResponseEntity.ok(nombreAdoptions);
    }

    @GetMapping("/sans-adoptions")
    public List<Famille> getFamillesWithoutAdoptions() {
        return familleService.getFamillesWithoutAdoptions();
    }

    @GetMapping("/multiple-adoptions")
    public List<Famille> getFamillesMultipleAdoptions() {
        return familleService.getFamillesMultipleAdoptions();
    }

    @PatchMapping("/{id}/notes")
    public ResponseEntity<Famille> updateFamilleNotes(@PathVariable Long id, @RequestParam String notes) {
        Famille updatedFamille = familleService.updateFamilleNotes(id, notes);
        return ResponseEntity.ok(updatedFamille);
    }
}