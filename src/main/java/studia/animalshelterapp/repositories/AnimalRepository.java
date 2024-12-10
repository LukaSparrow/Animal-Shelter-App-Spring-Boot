package studia.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studia.animalshelterapp.models.Animal;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByAnimalName(String animalName);
    boolean existsByAnimalNameAndAnimalSpeciesAndShelterId(String animalName, String animalSpecies, Long shelterId);
    List<Animal> findAllByShelterId(Long id);
}
