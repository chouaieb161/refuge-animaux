package com.refuge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.refuge.exception.ResourceNotFoundException;
import com.refuge.model.Animal;
import com.refuge.model.EspeceAnimal;
import com.refuge.model.Refuge;
import com.refuge.model.StatutAnimal;
import com.refuge.repository.AnimalRepository;
import com.refuge.repository.RefugeRepository;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final RefugeRepository refugeRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, RefugeRepository refugeRepository,
                         FileStorageService fileStorageService) {
        this.animalRepository = animalRepository;
        this.refugeRepository = refugeRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal non trouvé avec l'id: " + id));
    }

    @Transactional
    public Animal createAnimal(Animal animal, Long refugeId) {
        Refuge refuge = refugeRepository.findById(refugeId)
                .orElseThrow(() -> new ResourceNotFoundException("Refuge non trouvé avec l'id: " + refugeId));

        animal.setRefuge(refuge);
        return animalRepository.save(animal);
    }

    @Transactional
    public Animal updateAnimal(Long id, Animal animalDetails) {
        Animal animal = getAnimalById(id);

        animal.setNom(animalDetails.getNom());
        animal.setEspece(animalDetails.getEspece());
        animal.setAge(animalDetails.getAge());
        animal.setDescription(animalDetails.getDescription());
        animal.setEtatSante(animalDetails.getEtatSante());
        animal.setStatut(animalDetails.getStatut());

        // Ne pas écraser l'URL de la photo si non fournie
        if (animalDetails.getPhotoUrl() != null && !animalDetails.getPhotoUrl().isEmpty()) {
            animal.setPhotoUrl(animalDetails.getPhotoUrl());
        }

        return animalRepository.save(animal);
    }

    @Transactional
    public void deleteAnimal(Long id) {
        Animal animal = getAnimalById(id);

        // Supprimer la photo si elle existe
        if (animal.getPhotoUrl() != null && !animal.getPhotoUrl().isEmpty()) {
            fileStorageService.deleteFile(animal.getPhotoUrl());
        }

        animalRepository.delete(animal);
    }

    public List<Animal> getAnimalsByEspece(EspeceAnimal espece) {
        return animalRepository.findByEspece(espece);
    }

    public List<Animal> getAnimalsByStatut(StatutAnimal statut) {
        return animalRepository.findByStatut(statut);
    }

    public List<Animal> getAnimauxDisponiblesByEspece(EspeceAnimal espece) {
        return animalRepository.findByEspeceAndStatut(espece, StatutAnimal.DISPONIBLE);
    }

    public List<Animal> getAnimalsByRefuge(Long refugeId) {
        return animalRepository.findByRefugeId(refugeId);
    }

    @Transactional
    public void updateAnimalStatut(Long animalId, StatutAnimal statut) {
        Animal animal = getAnimalById(animalId);
        animal.setStatut(statut);
        animalRepository.save(animal);
    }

    public List<Animal> searchAnimalsByNom(String nom) {
        return animalRepository.findByNomContainingIgnoreCase(nom);
    }
}
