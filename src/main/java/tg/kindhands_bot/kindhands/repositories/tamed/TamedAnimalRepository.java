package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;

import java.time.LocalDate;
import java.util.List;

public interface TamedAnimalRepository extends JpaRepository<TamedAnimal, Long> {
    TamedAnimal findByUser_ChatId(Long chatId);

    List<TamedAnimal> findByDateLastReportBefore(LocalDate dateLastReport);
}
