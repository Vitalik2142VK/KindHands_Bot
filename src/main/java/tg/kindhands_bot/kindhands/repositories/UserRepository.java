package tg.kindhands_bot.kindhands.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.kindhands_bot.kindhands.entities.User;

import java.time.LocalDate;
import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    @Query(value = "select * from user where chat_id is not null", nativeQuery = true)
    Collection<User> findAllWithExistingChatId();

    User findByBlocked(Long chatId);

    Collection<User> findByNeedHelpTrue();

    @Query(value = "SELECT u.* FROM users u, (\n" +
            "\tSELECT tc.user_id FROM tamed_cat tc\n" +
            "\t\tWHERE tc.date_last_report < :date\n" +
            "\tUNION\n" +
            "\tSELECT td.user_id FROM tamed_dog td\n" +
            "\t\tWHERE td.date_last_report < :date\n" +
            ") AS id_users\n" +
            "WHERE id_users.user_id = u.id", nativeQuery = true)
    Collection<User> findUsersNotSendReport(@Param("date") LocalDate date);

    @Query(value = "SELECT u.* FROM users u, (\n" +
            "\tSELECT tc.user_id FROM tamed_cat tc\n" +
            "\t\tWHERE ('2023-10-14' - tc.date_last_report) > 2\n" +
            "\tUNION\n" +
            "\tSELECT td.user_id FROM tamed_dog td\n" +
            "\t\tWHERE ('2023-10-14' - td.date_last_report) > 2\n" +
            ") AS id_users\n" +
            "WHERE id_users.user_id = u.id;", nativeQuery = true)
    Collection<User> findUsersNotSendReportMoreTwoDays(@Param("date") LocalDate date);

    @Query(value = "SELECT u.needHelp FROM User u WHERE u.needHelp = true")
    boolean userNeedsHelp();
}
