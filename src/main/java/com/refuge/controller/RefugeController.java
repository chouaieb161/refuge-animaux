package com.refuge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.refuge.model.Refuge;
import com.refuge.service.RefugeService;
import com.refuge.service.FileStorageService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/refuges")
public class RefugeController {

    private final RefugeService refugeService;
    private final FileStorageService fileStorageService;

    @Autowired
    public RefugeController(RefugeService refugeService, FileStorageService fileStorageService) {
        this.refugeService = refugeService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<Refuge> getAllRefuges() {
        return refugeService.getAllRefuges();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Refuge> getRefugeById(@PathVariable Long id) {
        Refuge refuge = refugeService.getRefugeById(id);
        return ResponseEntity.ok(refuge);
    }

    @PostMapping
    public ResponseEntity<Refuge> createRefuge(@Valid @RequestBody Refuge refuge) {
        Refuge newRefuge = refugeService.createRefuge(refuge);
        return new ResponseEntity<>(newRefuge, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Refuge> updateRefuge(@PathVariable Long id, @Valid @RequestBody Refuge refuge) {
        Refuge updatedRefuge = refugeService.updateRefuge(id, refuge);
        return ResponseEntity.ok(updatedRefuge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefuge(@PathVariable Long id) {
        refugeService.deleteRefuge(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/logo")
    public ResponseEntity<Refuge> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Refuge refuge = refugeService.getRefugeById(id);

        // Stocker le logo
        String fileName = fileStorageService.storeFile(file);

        // Mettre à jour le refuge avec l'URL du logo
        refuge.setLogoUrl(fileName);
        Refuge updatedRefuge = refugeService.updateRefuge(id, refuge);

        return ResponseEntity.ok(updatedRefuge);
    }

    @GetMapping("/search")
    public List<Refuge> searchRefuges(@RequestParam String nom) {
        return refugeService.searchRefugesByNom(nom);
    }

    @GetMapping("/{id}/nombre-animaux")
    public ResponseEntity<Integer> getNombreAnimauxByRefuge(@PathVariable Long id) {
        int nombreAnimaux = refugeService.getNombreAnimauxByRefuge(id);
        return ResponseEntity.ok(nombreAnimaux);
    }

    @GetMapping("/{id}/capacite-disponible")
    public ResponseEntity<Boolean> checkCapaciteDisponible(@PathVariable Long id) {
        boolean hasCapacity = refugeService.hasCapacityForMoreAnimals(id);
        return ResponseEntity.ok(hasCapacity);
    }
}