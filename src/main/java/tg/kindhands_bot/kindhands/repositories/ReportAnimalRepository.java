package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;

public interface ReportAnimalRepository extends JpaRepository<ReportAnimal, Long> {
}
