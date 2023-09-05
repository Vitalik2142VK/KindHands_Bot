package tg.kindhands_bot.kindhands.entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ration;
    private String recommendation;
    @Lob
    private byte[] photo;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Animal(Long id, String name, String ration, String recommendation, byte[] photo) {
        this.id = id;
        this.name = name;
        this.ration = ration;
        this.recommendation = recommendation;
        this.photo = photo;
    }

    public Animal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(name, animal.name) && Objects.equals(ration, animal.ration) && Objects.equals(recommendation, animal.recommendation) && Arrays.equals(photo, animal.photo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, ration, recommendation);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name +
                ", ration='" + ration +
                ", recommendation='" + recommendation;
    }
}
