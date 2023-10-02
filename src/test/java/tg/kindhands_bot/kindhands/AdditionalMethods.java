package tg.kindhands_bot.kindhands;

import com.pengrad.telegrambot.BotUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.Animal;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.entities.tamed.TamedCat;
import tg.kindhands_bot.kindhands.entities.tamed.TamedDog;
import tg.kindhands_bot.kindhands.enums.BotState;

import java.time.LocalDate;
import java.util.List;

public class AdditionalMethods {
    // Дополнительые методы

    public static Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%text%", replaced), Update.class);
    }

    public static Update getUpdateButton(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%buttonCommand%", replaced), Update.class);
    }

    public static User createUser(Long id, Long chatId, String name, Boolean blocked,
                            String denialReason, BotState botState) {
        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setFirstName(name);
        user.setBlocked(blocked);
        user.setDenialReason(denialReason);
        user.setBotState(botState);
        return user;
    }

    public static User createUser(Long id, Long chatId, String lastName, String firstName, String phone, Boolean blocked,
                                  String denialReason, BotState botState) {
        User user = new User();
        user.setId(id);
        user.setChatId(chatId);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPhone(phone);
        user.setBlocked(blocked);
        user.setDenialReason(denialReason);
        user.setBotState(botState);
        return user;
    }

    public static User findUserByChatId(List<User> users, Long chatId) {
        for (var user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        throw new NullPointerException();
    }

    public static User addUser(List<User> users, User user) {
        users.add(user);
        return user;
    }

    public static TamedDog createTamedDog(Long id, User user, Animal animal, LocalDate dateAdoption,
                                          LocalDate dateLastReport) {
        TamedDog tamedDog = new TamedDog();
        tamedDog.setId(id);
        tamedDog.setUser(user);
        tamedDog.setAnimal(animal);
        tamedDog.setDateAdoption(dateAdoption);
        tamedDog.setDateLastReport(dateLastReport);
        return tamedDog;
    }

    public static TamedCat createTamedCat(Long id, User user, Animal animal, LocalDate dateAdoption,
                                          LocalDate dateLastReport) {
        TamedCat tamedCat = new TamedCat();
        tamedCat.setId(id);
        tamedCat.setUser(user);
        tamedCat.setAnimal(animal);
        tamedCat.setDateAdoption(dateAdoption);
        tamedCat.setDateLastReport(dateLastReport);
        return tamedCat;
    }

    public static ReportAnimal createReportAnimal(Long id, TamedAnimal tamedAnimal, String description,
                                                  LocalDate date, int reportNumber) {
        ReportAnimal reportAnimal = new ReportAnimal();
        reportAnimal.setId(id);
        reportAnimal.setPhoto(null);
        reportAnimal.setTamedAnimal(tamedAnimal);
        reportAnimal.setDescription(description);
        reportAnimal.setDate(date);
        reportAnimal.setReportNumber(reportNumber);
        return reportAnimal;
    }

    public static ReportAnimal addReportAnimal(List<ReportAnimal> reports, ReportAnimal report) {
        reports.add(report);
        return report;
    }
}
