package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tg.kindhands_bot.kindhands.entities.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    @Query(value = "select * from user where chat_id is not null", nativeQuery = true)
    Collection<User> findAllWithExistingChatId();

    User findByBlocked(Long chatId);
}
