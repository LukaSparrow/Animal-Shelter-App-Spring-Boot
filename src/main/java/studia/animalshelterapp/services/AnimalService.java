package studia.animalshelterapp.services;

import org.springframework.stereotype.Service;
import studia.animalshelterapp.exceptions.*;
import studia.animalshelterapp.models.Animal;
import studia.animalshelterapp.DTO.AnimalDTO;
import studia.animalshelterapp.models.AnimalShelter;
import studia.animalshelterapp.repositories.AnimalRepository;
import studia.animalshelterapp.repositories.ShelterRepository;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    public AnimalService(AnimalRepository animalRepository, ShelterRepository shelterRepository) {
        this.animalRepository = animalRepository;
        this.shelterRepository = shelterRepository;
    }

    public Animal addAnimal(AnimalDTO animal) throws ShelterNotFoundException, NotEnoughCapacityException, AnimalAlreadyExistsException {
        if(!shelterRepository.existsById(animal.shelterId())) {
            throw new ShelterNotFoundException("Shelter of id " + animal.shelterId() + " not found.");
        }
        AnimalShelter shelter = shelterRepository.getReferenceById(animal.shelterId());

        Animal newAnimal = new Animal(animal.name(), animal.species(), animal.condition(), animal.age(), animal.price());
        newAnimal.setShelter(shelter);

        if (shelter.getAnimalList().size() >= shelter.getMaxCapacity()) {
            throw new NotEnoughCapacityException("Shelter is full, cannot add more animals.");
        }

        if (animalRepository.existsByAnimalNameAndAnimalSpeciesAndShelterId(newAnimal.getAnimalName(), newAnimal.getAnimalSpecies(), animal.shelterId())) {
            throw new AnimalAlreadyExistsException("Animal with the same name and species already exists in this shelter.");
        }

        return animalRepository.save(newAnimal);
    }

    public void deleteAnimal(Long id) throws AnimalNotFoundException {
        if(!animalRepository.existsById(id)) {
            throw new AnimalNotFoundException("Animal with id " + id + " not found.");
        }
        Animal animal = animalRepository.getReferenceById(id);
        AnimalShelter shelter = shelterRepository.getReferenceById(animal.getShelter().getId());
        shelter.removeAnimal(animal);
        shelterRepository.save(shelter);
    }

    public Animal getAnimalById(Long id) throws AnimalNotFoundException {
        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with id " + id + " not found."));
    }
}
