package studia.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studia.animalshelterapp.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
