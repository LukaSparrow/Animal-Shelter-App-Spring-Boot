package studia.animalshelterapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studia.animalshelterapp.DTO.RatingDTO;
import studia.animalshelterapp.exceptions.ShelterNotFoundException;
import studia.animalshelterapp.services.RatingService;

import java.util.Map;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // 10. POST /api/rating - dodaje ocenę dla schroniska
    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody RatingDTO rating) {
        try {
            ratingService.addRating(rating);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Invalid rating value.",
                    "details", e.getMessage()
            ), HttpStatus.BAD_REQUEST);
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
