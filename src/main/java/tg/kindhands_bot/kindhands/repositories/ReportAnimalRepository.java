package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;

import java.time.LocalDate;

public interface ReportAnimalRepository extends JpaRepository<ReportAnimal, Long> {
    //ReportAnimal findByUserChatIdAndDate(Long chatId, LocalDate date);

    //Заменить после создания TamedAnimal
    ReportAnimal findByDateAndChatId(LocalDate date, Long chatId);
}
