package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;

public interface TamedAnimalRepository extends JpaRepository<TamedAnimal, Long> {
}
