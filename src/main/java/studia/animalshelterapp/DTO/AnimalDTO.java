package studia.animalshelterapp.DTO;

import studia.animalshelterapp.models.AnimalCondition;

public record AnimalDTO(
        String name,
        String species,
        int age,
        double price,
        AnimalCondition condition,
        Long shelterId
        ) {}
