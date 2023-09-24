package tg.kindhands_bot.kindhands.components;

import org.springframework.scheduling.annotation.Scheduled;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

import java.util.Collection;

public class ActionOnTime {
    private final UserRepository userRepository;
    private final ReportAnimal reportAnimal;
    private final ReportAnimalRepository reportAnimalRepository;
    private final KindHandsBot bot;

    public ActionOnTime(UserRepository userRepository, ReportAnimal reportAnimal, ReportAnimalRepository reportAnimalRepository, KindHandsBot bot) {
        this.userRepository = userRepository;
        this.reportAnimal = reportAnimal;
        this.reportAnimalRepository = reportAnimalRepository;
        this.bot = bot;
    }

    /**
     * Метод для отправки напоминания усыновителю об отправке отчета.
     * -----||-----
     * A method for sending a reminder to the user about sending a report.
     */
    @Scheduled(cron = "0 00 18 * * *") // каждый день в 18 всем усыновителям
    public void sendDailyReportReminder() {

        Collection<User> allUsers = userRepository.findAllWithExistingChatId();
        allUsers.stream().map(user -> ProcessingBotMessages.returnMessageUser(
                "Добрый день! Напоминаем, что до 21:00 необходимо отправить " +
                        "отчёт по питомцу. Спасибо!", user)).forEach(bot::sendMessage);
    }

    /**
     * Метод для отправки сообщения усыновителю "Нет отчета".
     * -----||-----
     * A method for sending to the user about sending a report.
     */
    @Scheduled(cron = "0 00 21 * * *") // каждый день в 21 всем усыновителям, не приславшим отчет
    public void sendDailyReportNotReceived() {
        Collection<User> allUsers = userRepository.findAllWithExistingChatId();
        allUsers.stream().map(user -> ProcessingBotMessages.returnMessageUser(
                "Сегодня мы не получили от Вас отчет! Напоминаем, что до 21:00 необходимо отправить " +
                        "отчёт по питомцу. Спасибо!", user)).forEach(bot::sendMessage);
    }
}


