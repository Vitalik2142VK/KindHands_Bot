package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;

import java.time.LocalDate;
import java.util.List;

public interface TamedAnimalRepository extends JpaRepository<TamedAnimal, Long> {
    TamedAnimal findByUser_ChatId(Long chatId);

    TamedAnimal findByUser_Id(Long id);

    List<TamedAnimal> findByDateLastReportBefore(LocalDate dateLastReport);

    TamedAnimal findByAnimal_Id(Long id);



//    @Query(value = "SELECT u.* FROM tamed_cat tc, users u\n" +
//            "WHERE tc.user_id = u.id\n" +
//            "AND tc.date_last_report < :date\n" +
//            "UNION\n" +
//            "SELECT u.* FROM tamed_dog td, users u\n" +
//            "WHERE td.user_id = u.id\n" +
//            "AND td.date_last_report < :date", nativeQuery = true)
//    List<User> findAllUsers(@Param("date") LocalDate dateLastReport);

//    @Query(value = "SELECT tc.user FROM TamedCat tc WHERE tc.dateLastReport < :date\n" +
//            " UNION" +
//            " SELECT td.user FROM TamedDog td WHERE td.dateLastReport < :date")
//    List<User> findAllUsers(@Param("date") LocalDate dateLastReport);

//    @Query(value = "SELECT u.* FROM tamed_cat tc, users u\n" +
//            "WHERE tc.user_id = u.id\n" +
//            "AND DATEIFF(tc.date_last_report, :date) >= 2")
//    List<User> findBySentReportsMoreTwoDays(@Param("date") LocalDate date);
}
