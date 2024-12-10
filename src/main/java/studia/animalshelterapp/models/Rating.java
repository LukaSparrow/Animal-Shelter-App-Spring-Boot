package studia.animalshelterapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shelter_id", nullable = false)
    @JsonIgnore
    private AnimalShelter shelter;

    @Column(nullable = false)
    private LocalDate ratingDate;

    @Column(nullable = false)
    private String comment = "";

    public Rating(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
        this.ratingDate = LocalDate.now();
    }

    public Rating() {
        this.ratingDate = LocalDate.now();
    }

    public Rating(int value, String comment, AnimalShelter shelter) {
        this.rating = value;
        this.comment = comment;
        this.shelter = shelter;
        this.ratingDate = LocalDate.now();
    }

    public Rating(int value, String comment, AnimalShelter shelter, LocalDate date) {
        this.rating = value;
        this.comment = comment;
        this.shelter = shelter;
        this.ratingDate = date;
    }

    public Rating(int value, String comment, LocalDate date) {
        this.rating = value;
        this.comment = comment;
        this.ratingDate = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return rating;
    }

    public void setValue(int value) {
        if (value < 0 || value > 5) {
            throw new IllegalArgumentException("Ocena musi być w skali 0-5");
        }
        this.rating = value;
    }

    public AnimalShelter getShelter() {
        return shelter;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
    }

    public LocalDate getDate() {
        return ratingDate;
    }

    public void setDate(LocalDate date) {
        this.ratingDate = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Komentarz nie może być null. Możesz zostawić pusty ciąg.");
        }
        this.comment = comment;
    }
}
