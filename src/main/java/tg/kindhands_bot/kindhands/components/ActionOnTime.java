package tg.kindhands_bot.kindhands.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedAnimalRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedCatRepository;
import tg.kindhands_bot.kindhands.repositories.tamed.TamedDogRepository;
import tg.kindhands_bot.kindhands.services.KindHandsBot;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;

@Component
public class ActionOnTime {
    private final Logger log = LoggerFactory.getLogger(ActionOnTime.class);

    private final TamedAnimalRepository tamedAnimalRepository;
    private final TamedCatRepository tamedCatRepository;
    private final TamedDogRepository tamedDogRepository;
    private final KindHandsBot bot;

    public ActionOnTime(TamedAnimalRepository tamedAnimalRepository,
                        TamedCatRepository tamedCatRepository,
                        TamedDogRepository tamedDogRepository,
                        KindHandsBot bot) {
        this.tamedAnimalRepository = tamedAnimalRepository;
        this.tamedCatRepository = tamedCatRepository;
        this.tamedDogRepository = tamedDogRepository;
        this.bot = bot;
    }

    /**
     * Метод для отправки напоминания усыновителю об отправке отчета.
     * -----||-----
     * A method for sending a reminder to the user about sending a report.
     */
    @Scheduled(cron = "0 00 18 * * *") // каждый день в 18 всем усыновителям
    public void sendDailyReportReminder() {
        Collection<User> users = getAllUser();
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
        Collection<User> users = getAllUser();
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
    @Scheduled(cron = "0 30 21 * * *") // каждый день в 12
    public void checkDailyReportReceived() {
        LocalDate date = LocalDate.now();
        List<TamedAnimal> tamedAnimals = tamedAnimalRepository.findByDateLastReportBefore(date);
        tamedAnimals.stream()
                .filter(tamedAnimal -> Period.between(tamedAnimal.getDateLastReport(), date).getDays() >= 2)
                .map(TamedAnimal::getUser)
                .forEach(user -> bot.sendMessage(ProcessingBotMessages.returnMessageUser(user,
                        "Вы не отправляли отчеты более 2-х дней. Пожалуйста, отправьте отчет в ближайшее время, " +
                                "иначе мы будем вынуждены забрать питомца обратно!" +
                                "\n\nВ скором времени с Вами свяжутся для уточнения.")));
    }

    private Collection<User> getAllUser() {
        LocalDate nowDate = LocalDate.now();
        Collection<User> users = tamedCatRepository.findAllUsersDateLastReportBefore(nowDate);
        users.addAll(tamedDogRepository.findAllUsersDateLastReportBefore(nowDate));
        return users;
    }
}


