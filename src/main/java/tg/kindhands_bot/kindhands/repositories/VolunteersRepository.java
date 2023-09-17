package tg.kindhands_bot.kindhands.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.kindhands_bot.kindhands.entities.Volunteer;


public interface VolunteersRepository extends JpaRepository<Volunteer, Long> {
    Volunteer findByChatId(Long chatId);
}