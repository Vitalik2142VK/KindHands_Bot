package tg.kindhands_bot.kindhands.repositories;
import liquibase.pro.packaged.V;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.kindhands_bot.kindhands.entities.Volunteer;

import java.util.List;

@Repository
public interface VolunteersRepository extends JpaRepository<Volunteer, Long> {
    Volunteer findByChatId(Long chatId);
    List<Volunteer> findByAdoptedTrue();
}