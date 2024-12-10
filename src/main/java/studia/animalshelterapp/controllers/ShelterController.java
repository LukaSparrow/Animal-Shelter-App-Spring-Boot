package studia.animalshelterapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studia.animalshelterapp.DTO.AnimalShelterDTO;
import studia.animalshelterapp.models.Animal;
import studia.animalshelterapp.models.AnimalShelter;
import studia.animalshelterapp.services.ShelterService;
import studia.animalshelterapp.exceptions.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animalshelter")
public class ShelterController {
    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    // 4. GET /api/animalshelter/csv - zwraca informacje o schronisku w formie pliku CSV
    @GetMapping("/csv")
    public ResponseEntity<?> getShelterCSV() {
        try {
            String csvData = shelterService.generateShelterCSV();
            return new ResponseEntity<>(csvData, HttpStatus.OK);
        } catch (ShelterNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelters not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5. GET /api/sheltermanager - zwraca wszystkie schroniska
    @GetMapping
    public ResponseEntity<?> getAllShelters() {
        try {
            List<AnimalShelter> shelters = shelterService.getAllShelters();
            return new ResponseEntity<>(shelters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 6. POST /api/animalshelter - dodaje nowe schronisko
    @PostMapping
    public ResponseEntity<?> addShelter(@RequestBody AnimalShelterDTO shelter) {
        try {
            AnimalShelter createdShelter = shelterService.addShelter(shelter);
            return new ResponseEntity<>(createdShelter, HttpStatus.CREATED);
        } catch (ShelterAlreadyExistsException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelter of said name already exists.",
                    "details", e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 7. DELETE /api/animalshelter/{id} - usuwa schronisko
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShelter(@PathVariable Long id) {
        try {
            shelterService.deleteShelter(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ShelterNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelters not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 8. GET /api/animalshelter/{id}/animal - zwraca wszystkie zwierzęta w schronisku
    @GetMapping("/{id}/animal")
    public ResponseEntity<?> getAnimalsInShelter(@PathVariable Long id) {
        try {
            List<Animal> animals = shelterService.getAnimalsInShelter(id);
            return new ResponseEntity<>(animals, HttpStatus.OK);
        } catch (ShelterNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelter not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 9. GET /api/animalshelter/{id}/fill - zwraca zapełnienie w schronisku
    @GetMapping("/{id}/fill")
    public ResponseEntity<?> getShelterFill(@PathVariable Long id) {
        try {
            String fillStatus = shelterService.getShelterFill(id);
            return new ResponseEntity<>(fillStatus, HttpStatus.OK);
        } catch (ShelterNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelter not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
