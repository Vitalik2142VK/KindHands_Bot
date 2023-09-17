package tg.kindhands_bot.kindhands.components;

import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;

public class ActionOnTime {
    Timer timer = new Timer();
    private final UserRepository userRepository;
    private final KindHandsBot bot;
  //  private final ProbationPeriod probationPeriod;

    public ActionOnTime(UserRepository userRepository, KindHandsBot bot) {
        this.userRepository = userRepository;
        this.bot = bot;
    }
//    @Scheduled(cron = "0 00 10 * * *") // каждый день в 10 утра
//    public void sendDailyReportReminder() {
//
//        Collection<User> allAdopters = userRepository.findAllWithExistingChatId();
//        for (User adopter : allAdopters) {
//            if (adopter.getProbationPeriod().stream().allMatch(
//                    probationPeriod -> probationPeriod.getEnds.isBefore(
//                            LocalDate.now()
//                    )
//            )) {
//                allAdopters.remove(adopter);
//            }
//        }
//        allAdopters.forEach(user -> {
//            bot.sendMessage(new SendMessage("Доброе утро! Напоминаем, что до 21:00 необходимо отправить " +
//                    "отчёт(ы) по питомцу(ам). Спасибо!"));
//
//        });
    }


