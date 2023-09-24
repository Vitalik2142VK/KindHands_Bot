package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;

import java.time.LocalDate;

public interface ReportAnimalRepository extends JpaRepository<ReportAnimal, Long> {
    ReportAnimal findByDateAndTamedAnimal_Id(LocalDate date, long id);

    ReportAnimal findByDateAndTamedAnimal_User_ChatId(LocalDate date, long chatId);


}
