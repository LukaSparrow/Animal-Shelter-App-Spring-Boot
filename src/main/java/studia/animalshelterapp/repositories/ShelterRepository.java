package studia.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studia.animalshelterapp.models.AnimalShelter;

@Repository
public interface ShelterRepository extends JpaRepository<AnimalShelter, Long> {
    boolean existsByShelterName(String shelterName);
}
