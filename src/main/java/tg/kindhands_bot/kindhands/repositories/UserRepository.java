package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tg.kindhands_bot.kindhands.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);
}
