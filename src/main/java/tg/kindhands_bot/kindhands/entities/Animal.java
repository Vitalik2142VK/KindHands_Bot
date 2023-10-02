package tg.kindhands_bot.kindhands.entities;

import tg.kindhands_bot.kindhands.entities.photo.AnimalPhoto;
import tg.kindhands_bot.kindhands.enums.TypeAnimal;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeAnimal typeAnimal;
    private String name;
    private String ration;
    private String recommendation;
    @OneToOne
    @JoinColumn(name = "animal_photo_id")
    private AnimalPhoto animalPhoto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnimalPhoto getAnimalPhoto() {
        return animalPhoto;
    }

    public void setAnimalPhoto(AnimalPhoto animalPhoto) {
        this.animalPhoto = animalPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(name, animal.name) && Objects.equals(ration, animal.ration) && Objects.equals(recommendation, animal.recommendation) && Objects.equals(animalPhoto, animal.animalPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ration, recommendation, animalPhoto);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name +
                ", ration='" + ration +
                ", recommendation='" + recommendation;
    }

    public TypeAnimal getTypeAnimal() {
        return typeAnimal;
    }

    public void setTypeAnimal(TypeAnimal typeAnimal) {
        this.typeAnimal = typeAnimal;
    }
}
