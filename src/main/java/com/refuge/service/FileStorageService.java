package com.refuge.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.refuge.config.FileStorageProperties;
import com.refuge.exception.FileStorageException;
import com.refuge.exception.ResourceNotFoundException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Impossible de créer le répertoire de stockage des fichiers.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normaliser le nom du fichier
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";

        try {
            // Vérifier si le nom de fichier contient des caractères invalides
            if (originalFilename.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide: " + originalFilename);
            }

            // Générer un nom de fichier unique
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + fileExtension;

            // Copier le fichier vers l'emplacement de stockage
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + originalFilename, ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Fichier non trouvé: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Fichier non trouvé: " + fileName);
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de supprimer le fichier: " + fileName, ex);
        }
        return false;
    }
}
