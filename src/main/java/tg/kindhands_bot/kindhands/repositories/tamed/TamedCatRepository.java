package tg.kindhands_bot.kindhands.repositories.tamed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;

import java.time.LocalDate;
import java.util.List;

public interface TamedCatRepository extends JpaRepository<TamedCat, Long> {
    @Query(value = "SELECT tc.user FROM TamedCat tc WHERE tc.dateLastReport < :date")
    List<User> findAllUsersDateLastReportBefore(@Param("date")LocalDate dateLastReport);
}
