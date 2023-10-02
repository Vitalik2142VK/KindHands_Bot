package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedDog;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface TamedDogRepository extends JpaRepository<TamedDog, Long> {
    @Query(value = "SELECT td.user FROM TamedDog td WHERE td.dateLastReport < :date")
    List<User> findAllUsersDateLastReportBefore(@Param("date")LocalDate date);

//    @Query(value = "SELECT td.user FROM TamedDog td WHERE DATEIFF(td.dateLastReport, :date) >= 2")
//    List<User> findByNoSentReportsMoreTwoDays(@Param("date") Date date);
}
