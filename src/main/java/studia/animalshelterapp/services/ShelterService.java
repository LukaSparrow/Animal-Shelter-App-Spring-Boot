package studia.animalshelterapp.services;

import org.springframework.stereotype.Service;
import studia.animalshelterapp.DTO.AnimalShelterDTO;
import studia.animalshelterapp.exceptions.ShelterAlreadyExistsException;
import studia.animalshelterapp.exceptions.ShelterNotFoundException;
import studia.animalshelterapp.models.Animal;
import studia.animalshelterapp.models.AnimalShelter;
import studia.animalshelterapp.repositories.AnimalRepository;
import studia.animalshelterapp.repositories.ShelterRepository;

import java.util.List;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final AnimalRepository animalRepository;

    public ShelterService(ShelterRepository shelterRepository, AnimalRepository animalRepository) {
        this.shelterRepository = shelterRepository;
        this.animalRepository = animalRepository;
    }

    public String generateShelterCSV() throws ShelterNotFoundException {
        List<AnimalShelter> shelters = shelterRepository.findAll();

        if(shelters.isEmpty()) {
            throw new ShelterNotFoundException("There are no shelters.");
        }

        StringBuilder builder = new StringBuilder();

        for(AnimalShelter shelter : shelters) {
            builder.append(shelter.getShelterName());
            builder.append(',');

            builder.append(shelter.getAnimalList().size());
            builder.append(',');

            builder.append(shelter.getMaxCapacity());
            builder.append(',');

            builder.append(shelter.getAvgRating());
            builder.append('\n');
        }

        return builder.toString();
    }

    public List<AnimalShelter> getAllShelters() {
        return shelterRepository.findAll();
    }

    public AnimalShelter addShelter(AnimalShelterDTO animalShelter) throws ShelterAlreadyExistsException {
        AnimalShelter shelter = new AnimalShelter(animalShelter.name(), animalShelter.maxCapacity());
        if (shelterRepository.existsByShelterName(shelter.getShelterName())) {
            throw new ShelterAlreadyExistsException("Shelter of provided name already exists.");
        }
        return shelterRepository.save(shelter);
    }

    public void deleteShelter(Long id) throws ShelterNotFoundException {
        if(!shelterRepository.existsById(id)) {
            throw new ShelterNotFoundException("Shelter with id " + id + " not found.");
        }
        shelterRepository.deleteById(id);
    }

    public List<Animal> getAnimalsInShelter(Long id) throws ShelterNotFoundException {
        if(!shelterRepository.existsById(id)) {
            throw new ShelterNotFoundException("Shelter with id " + id + " not found.");
        }
        return animalRepository.findAllByShelterId(id);
    }

    public String getShelterFill(Long id) throws ShelterNotFoundException {
        if(!shelterRepository.existsById(id)) {
            throw new ShelterNotFoundException("Shelter with id " + id + " not found.");
        }
        return shelterRepository.getReferenceById(id).getFillString();
    }
}
