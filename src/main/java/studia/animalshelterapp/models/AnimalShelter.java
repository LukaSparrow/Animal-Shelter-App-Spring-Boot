package studia.animalshelterapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import studia.animalshelterapp.exceptions.InvalidCapacityException;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "shelters")
public class AnimalShelter implements Comparable<AnimalShelter>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false, unique = true)
    private String shelterName;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Animal> animalList = new ArrayList<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Rating> ratings = new ArrayList<>();

    @Column(nullable = false)
    private int maxCapacity;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.animalList = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public String getFillString() {
        return this.animalList.size()+"/"+this.maxCapacity;
    }

    public double getAvgRating() {
        if (ratings.isEmpty())
            return 0;

        return ratings.stream().map(Rating::getValue).reduce(0, Integer::sum) / (double)ratings.size();
    }

    public void addAnimal(Animal animal) {
        if(animalList.size() < maxCapacity) {
            animalList.add(animal);
            animal.setShelter(this);
        } else {
            throw new IllegalArgumentException("Shelter is already full");
        }
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setShelter(this);
    }

    public static Comparator<AnimalShelter> nameComparator = (s1, s2) -> s1.getShelterName().compareToIgnoreCase(s2.getShelterName());
    public static Comparator<AnimalShelter> capacityComparator = (s1, s2) -> Integer.compare(s1.getMaxCapacity(), s2.getMaxCapacity());

    @Override
    public int compareTo(AnimalShelter o) {
        return Integer.compare(maxCapacity, o.getMaxCapacity());
    }

    public AnimalShelter() {}

    public String getRatingsString() {
        if (ratings == null || ratings.isEmpty()) {
            return "No ratings";
        }
        OptionalDouble average = ratings.stream()
                .mapToInt(Rating::getValue)
                .average();
        int count = ratings.size();
        return String.format("%.1f (%d)", average.orElse(0.0), count);
    }

    public Animal getAnimal(String animalName) {
        Animal searchedAnimal = search(animalName);
        changeCondition(searchedAnimal, AnimalCondition.W_TRAKCIE_ADOPCJI);
        animalList.remove(searchedAnimal);
        return searchedAnimal;
    }

    public void changeCondition(Animal animal, AnimalCondition condition) {
        animal.setAnimalCondition(condition);
    }

    public void changeAge(Animal animal, int age) {
        animal.setAnimalAge(age);
    }

    public int countByCondition(AnimalCondition condition) {
        return (int) this.animalList.stream().filter(animal -> animal.getAnimalCondition() == condition).count();
    }

    public List<Animal> sortByName() {
        List<Animal> sortedAnimalList = new ArrayList<>(this.animalList);
        Collections.sort(sortedAnimalList);
        return sortedAnimalList;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedAnimalList = new ArrayList<>(this.animalList);
        sortedAnimalList.sort((a1, a2) -> Double.compare(a1.getAnimalPrice(), a2.getAnimalPrice()));
        return sortedAnimalList;
    }

    public Animal search(String animalName) {
        return animalList
                .stream()
                .filter(animal -> animal.getAnimalName().equalsIgnoreCase(animalName))
                .findFirst()
                .orElse(null);
    }

    public List<Animal> searchPartial(String textFragment) {
        String filter = textFragment.toLowerCase();
        return animalList
                .stream()
                .filter(
                        animal -> animal.getAnimalName().toLowerCase().contains(filter) ||
                        animal.getAnimalSpecies().toLowerCase().contains(filter))
                .toList();
    }

    public Animal max() {
        return Collections.max(this.animalList, Comparator.comparingDouble(Animal::getAnimalPrice));
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public void setShelterCapacity(int capacity) throws InvalidCapacityException {
        if(capacity <= 0) {
            throw new InvalidCapacityException("Pojemnosc schroniska musi byc wieksza od zera.");
        }
        this.maxCapacity = capacity;
    }

    public Long getId() {
        return this.id;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public String getShelterName() {
        return this.shelterName;
    }

    public List<Animal> getAnimalList() {return this.animalList;}

    public List<Rating> getRatings() {return this.ratings;}

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void removeAnimal(Animal animal) {
        this.animalList.remove(animal);
    }
}
