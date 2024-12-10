package studia.animalshelterapp.services;

import org.springframework.stereotype.Service;
import studia.animalshelterapp.DTO.RatingDTO;
import studia.animalshelterapp.exceptions.ShelterNotFoundException;
import studia.animalshelterapp.models.AnimalShelter;
import studia.animalshelterapp.models.Rating;
import studia.animalshelterapp.repositories.RatingRepository;
import studia.animalshelterapp.repositories.ShelterRepository;

import java.time.LocalDate;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ShelterRepository shelterRepository;

    public RatingService(RatingRepository ratingRepository, ShelterRepository shelterRepository) {
        this.ratingRepository = ratingRepository;
        this.shelterRepository = shelterRepository;
    }

    public void addRating(RatingDTO rating) throws IllegalArgumentException, ShelterNotFoundException {
        if(rating.rating() < 1 || rating.rating() > 5) {
            throw new IllegalArgumentException("Rating value must be in range of 1-5.");
        }
        if(!shelterRepository.existsById(rating.shelterId())) {
            throw new ShelterNotFoundException("Shelter with id " + rating.shelterId() + " not found.");
        }
        AnimalShelter shelter = shelterRepository.getReferenceById(rating.shelterId());
        Rating newRating = new Rating(rating.rating(), rating.comment(), shelter, LocalDate.now());
        ratingRepository.save(newRating);
    }
}
