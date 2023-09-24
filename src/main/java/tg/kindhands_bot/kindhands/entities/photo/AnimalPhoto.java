package tg.kindhands_bot.kindhands.entities.photo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "animal_photos")
public class AnimalPhoto extends Photo {
    @OneToOne
    @JoinColumn(name = "animal_id")
    private AnimalPhoto animalPhoto;

    public AnimalPhoto getAnimalPhoto() {
        return animalPhoto;
    }

    public void setAnimalPhoto(AnimalPhoto animalPhoto) {
        this.animalPhoto = animalPhoto;
    }
}
