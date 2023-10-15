package tg.kindhands_bot.kindhands.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.repositories.UserRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;


import java.time.LocalDate;
import java.util.Collection;

@Component
public class ActionOnTime {
    private final Logger log = LoggerFactory.getLogger(ActionOnTime.class);

    private final UserRepository userRepository;
    private final KindHandsBot bot;

    public ActionOnTime(UserRepository userRepository,
                        KindHandsBot bot) {
        this.userRepository = userRepository;
        this.bot = bot;
    }

    /**
     * Метод для отправки напоминания усыновителю об отправке отчета.
     * -----||-----
     * A method for sending a reminder to the user about sending a report.
     */
    @Scheduled(cron = "0 00 18 * * *") // каждый день в 18 всем усыновителям
    public void sendDailyReportReminder() {
        Collection<User> users = userRepository.findUsersNotSendReport(LocalDate.now()); //getAllUser();
        if (users.isEmpty()) {
            log.info("На 18:00 все пользователи отчеты отравили.");
            return;
        }
        users.stream().map(user -> ProcessingBotMessages.returnMessageUser(user,
                "Добрый день! Напоминаем, что до 21:00 необходимо отправить " +
                        "отчёт по питомцу. Спасибо!")).forEach(bot::sendMessage);

        log.info("Напоминание об отправке отчета в 18:00 пользователям отправлено.");
    }

    /**
     * Метод для отправки сообщения усыновителю "Нет отчета".
     * -----||-----
     * A method for sending to the user about sending a report.
     */
    @Scheduled(cron = "0 00 21 * * *") // каждый день в 21 всем усыновителям, не приславшим отчет
    public void sendDailyReportNotReceived() {
        Collection<User> users = userRepository.findUsersNotSendReport(LocalDate.now());
        if (users.isEmpty()) {
            log.info("На 21:00 все пользователи отчеты отравили.");
            return;
        }
        users.stream().map(user -> ProcessingBotMessages.returnMessageUser(user,
                "Сегодня мы не получили от Вас отчет! Напоминаем, что, если отчеты не будут отправляться, то мы будем вынуждены забрать " +
                        "питомца обратно.\n\nЕсли у вас возникают проблемы с отправкой отчета, то необходимо обратиться к волонтерам. " +
                        "\n\nСпасибо за понимание!")).forEach(bot::sendMessage);

        log.info("Предупреждение не отправившим пользователям в 21:00 отправлено.");
    }

    /**
     * Метод выявления тех, кто не отправлял отчет уже больше 2 дней.
     * -----||-----
     * A method for identifying people who haven't send report more than 2 days
     */
    @Scheduled(cron = "0 30 21 * * *") // каждый день в 21:30
    public void checkDailyReportReceived() {
        Collection<User> users = userRepository.findUsersNotSendReportMoreTwoDays(LocalDate.now());
        users.stream().map(user -> ProcessingBotMessages.returnMessageUser(user,
                "Вы не отправляли отчеты более 2-х дней. Пожалуйста, отправьте отчет в ближайшее время, " +
                                "иначе мы будем вынуждены забрать питомца обратно!" +
                                "\n\nВ скором времени с Вами свяжутся для уточнения.")).forEach(bot::sendMessage);
    }
}


