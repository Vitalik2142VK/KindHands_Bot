package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.ReportAnimalPhoto;

public interface ReportAnimalPhotoRepository extends JpaRepository<ReportAnimalPhoto, Long> {
}
