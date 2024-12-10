package studia.animalshelterapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studia.animalshelterapp.DTO.AnimalDTO;
import studia.animalshelterapp.exceptions.AnimalAlreadyExistsException;
import studia.animalshelterapp.exceptions.AnimalNotFoundException;
import studia.animalshelterapp.exceptions.NotEnoughCapacityException;
import studia.animalshelterapp.exceptions.ShelterNotFoundException;
import studia.animalshelterapp.models.Animal;
import studia.animalshelterapp.services.AnimalService;

import java.util.Map;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // 1. POST /api/animal - dodaje zwierzę do schroniska
    @PostMapping
    public ResponseEntity<?> addAnimal(@RequestBody AnimalDTO animal) {
        try {
            Animal createdAnimal = animalService.addAnimal(animal);
            return new ResponseEntity<>(createdAnimal, HttpStatus.CREATED);
        } catch (ShelterNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelter not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (NotEnoughCapacityException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Shelter does not have enough capacity.",
                    "details", e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        } catch (AnimalAlreadyExistsException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Animal with the same name already exists in this shelter.",
                    "details", e.getMessage()
            ), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. DELETE /api/animal/{id} - usuwa zwierzę ze schroniska
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        try {
            animalService.deleteAnimal(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AnimalNotFoundException e) {
            // Obsługuje wyjątek AnimalNotFoundException
            return new ResponseEntity<>(Map.of(
                    "message", "Animal not found.",
                    "details", e.getMessage()
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Ogólna obsługa nieoczekiwanych błędów
            return new ResponseEntity<>(Map.of(
                    "message", "An unexpected error occurred.",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. GET /api/animal/{id} - zwraca informacje o zwierzęciu
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimal(@PathVariable Long id) {
        try {
            Animal animal = animalService.getAnimalById(id);
            return new ResponseEntity<>(animal, HttpStatus.OK);
        } catch (AnimalNotFoundException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Animal not found.",
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
