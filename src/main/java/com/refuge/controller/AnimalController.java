package com.refuge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.refuge.model.Animal;
import com.refuge.model.EspeceAnimal;
import com.refuge.model.StatutAnimal;
import com.refuge.service.AnimalService;
import com.refuge.service.FileStorageService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/animaux")
public class AnimalController {

    private final AnimalService animalService;
    private final FileStorageService fileStorageService;

    @Autowired
    public AnimalController(AnimalService animalService, FileStorageService fileStorageService) {
        this.animalService = animalService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<Animal> getAllAnimaux() {
        return animalService.getAllAnimals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Animal animal = animalService.getAnimalById(id);
        return ResponseEntity.ok(animal);
    }

    @PostMapping
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody Animal animal, @RequestParam Long refugeId) {
        Animal newAnimal = animalService.createAnimal(animal, refugeId);
        return new ResponseEntity<>(newAnimal, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable Long id, @Valid @RequestBody Animal animal) {
        Animal updatedAnimal = animalService.updateAnimal(id, animal);
        return ResponseEntity.ok(updatedAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Animal> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Animal animal = animalService.getAnimalById(id);

        // Stocker la photo
        String fileName = fileStorageService.storeFile(file);

        // Mettre à jour l'animal avec l'URL de la photo
        animal.setPhotoUrl(fileName);
        Animal updatedAnimal = animalService.updateAnimal(id, animal);

        return ResponseEntity.ok(updatedAnimal);
    }

    @GetMapping("/espece/{espece}")
    public List<Animal> getAnimalsByEspece(@PathVariable EspeceAnimal espece) {
        return animalService.getAnimalsByEspece(espece);
    }

    @GetMapping("/statut/{statut}")
    public List<Animal> getAnimalsByStatut(@PathVariable StatutAnimal statut) {
        return animalService.getAnimalsByStatut(statut);
    }

    @GetMapping("/disponibles/espece/{espece}")
    public List<Animal> getAnimauxDisponiblesByEspece(@PathVariable EspeceAnimal espece) {
        return animalService.getAnimauxDisponiblesByEspece(espece);
    }

    @GetMapping("/refuge/{refugeId}")
    public List<Animal> getAnimauxByRefuge(@PathVariable Long refugeId) {
        return animalService.getAnimalsByRefuge(refugeId);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Void> updateStatut(@PathVariable Long id, @RequestParam StatutAnimal statut) {
        animalService.updateAnimalStatut(id, statut);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Animal> searchAnimaux(@RequestParam String nom) {
        return animalService.searchAnimalsByNom(nom);
    }
}