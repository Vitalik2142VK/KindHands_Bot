package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;

public interface TamedCatRepository extends JpaRepository<TamedCat, Long> {
}
