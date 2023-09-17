package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.AnimalPhoto;

public interface AnimalPhotoRepository extends JpaRepository<AnimalPhoto, Long> {
}
