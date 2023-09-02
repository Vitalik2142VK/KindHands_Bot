package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteers;

@Repository
public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {
    Volunteers findByChatId(Long chatId);
}