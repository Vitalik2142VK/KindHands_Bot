package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;

public interface TamedAnimalRepository extends JpaRepository<TamedAnimal, Long> {
    TamedAnimal findByUser_ChatId(Long chatId);
}
