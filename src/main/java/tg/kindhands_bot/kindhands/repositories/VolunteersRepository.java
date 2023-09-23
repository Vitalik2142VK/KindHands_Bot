package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.Volunteer;

import java.util.List;

public interface VolunteersRepository extends JpaRepository<Volunteer, Long> {
    Volunteer findByChatId(Long chatId);
    List<Volunteer> findByAdoptedTrue();
}