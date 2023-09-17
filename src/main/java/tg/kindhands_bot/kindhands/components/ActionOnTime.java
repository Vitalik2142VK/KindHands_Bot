package tg.kindhands_bot.kindhands.components;

import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Timer;

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
     * Метод для отправки напоминания пользователю об отправке отчета.
     * -----||-----
     * A method for sending a reminder to the user about sending a report.
     */
    @Scheduled(cron = "0 00 18 * * *") // каждый день в 18
    public void sendDailyReportReminder() {

        Collection<User> allUsers = userRepository.findAllWithExistingChatId();
        for (User user : allUsers) {//в дальнейшем реализовать через стримы
            bot.sendMessage(ProcessingBotMessages.returnMessageUser(
                    "Добрый день! Напоминаем, что до 21:00 необходимо отправить " +
                            "отчёт по питомцу. Спасибо!", user));

        }
    }

    /**
     * Метод для отправки сообщения пользователю "Нет отчета".
     * -----||-----
     * A method for sending to the user about sending a report.
     */
    @Scheduled(cron = "0 00 21 * * *") // каждый день в 21
    public void sendDailyReportNotReceived() {
        Collection<User> allUsers = userRepository.findAllWithExistingChatId();
        for (User user : allUsers) {//в дальнейшем реализовать через стримы
            bot.sendMessage(ProcessingBotMessages.returnMessageUser(
                    "Сегодня мы не получили от Вас отчет! Напоминаем, что до 21:00 необходимо отправить " +
                            "отчёт по питомцу. Спасибо!", user));
        }
    }
}

