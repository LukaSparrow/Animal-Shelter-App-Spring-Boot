package studia.animalshelterapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "animals")
public class Animal implements Comparable<Animal>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonIgnore
    private AnimalShelter shelter;

    @Column(nullable = false)
    private String animalName;

    @Column(nullable = false)
    private String animalSpecies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalCondition animalCondition;

    @Column(nullable = false)
    private int animalAge;

    @Column(nullable = false)
    private double animalPrice;

    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.animalName = name;
        this.animalSpecies = species;
        this.animalCondition = condition;
        this.animalAge = age;
        this.animalPrice = price;
    }

    public Animal(Animal animal) {
        this.animalName = animal.getAnimalName();
        this.animalSpecies = animal.getAnimalSpecies();
        this.animalCondition = animal.getAnimalCondition();
        this.animalAge = animal.getAnimalAge();
        this.animalPrice = animal.getAnimalPrice();
        this.shelter = animal.getShelter();
    }

    public Animal() {}

    @Override
    public int compareTo(Animal otherAnimal) {
        if(this.animalName.compareTo(otherAnimal.animalName) != 0) {
            return this.animalName.compareTo(otherAnimal.animalName);
        }
        if(this.animalSpecies.compareTo(otherAnimal.animalSpecies) != 0) {
            return this.animalSpecies.compareTo(otherAnimal.animalSpecies);
        }
        return Integer.compare(this.animalAge, otherAnimal.animalAge);
    }

    public void setAnimalName(String name) {
        this.animalName = name;
    }

    public void setAnimalSpecies(String species) {
        this.animalSpecies = species;
    }

    public void setAnimalPrice(double price) {
        this.animalPrice = price;
    }

    public void setAnimalCondition(AnimalCondition animalCondition) {
        this.animalCondition = animalCondition;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
    }

    public AnimalCondition getAnimalCondition() {
        return this.animalCondition;
    }

    public String getAnimalName() {
        return this.animalName;
    }

    public String getAnimalSpecies() {
        return this.animalSpecies;
    }

    public int getAnimalAge() {return this.animalAge;}

    public double getAnimalPrice() {return this.animalPrice;}

    public void setShelter(AnimalShelter shelter){
        this.shelter = shelter;
    }

    public AnimalShelter getShelter() {
        return this.shelter;
    }

    public Long getId() {
        return this.id;
    }
}
