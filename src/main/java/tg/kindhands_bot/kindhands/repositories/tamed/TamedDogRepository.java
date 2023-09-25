package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.tamed.TamedDog;

public interface TamedDogRepository extends JpaRepository<TamedDog, Long> {
}
