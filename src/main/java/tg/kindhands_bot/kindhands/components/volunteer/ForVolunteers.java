package tg.kindhands_bot.kindhands.components.volunteer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.components.ProcessingBotMessages;
import tg.kindhands_bot.kindhands.entities.Volunteers;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;

import java.util.List;

/**
 * Работа с БД волонтеров. Принимает желающих стать волонтерами, а так же удаляет из БД.
 * Отправляет уведомление волонтерам.
 * -----||-----
 * Working with the database of volunteers. Accepts those who want to become volunteers,
 * and also removes them from the database. Sends a notification to the volunteers.
 */

@Component
public class ForVolunteers {
    private final VolunteersRepository volunteersRepository;

    public ForVolunteers(VolunteersRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    /**
     * Метод приглашения пользователем волонтера в чат
     * -----||-----
     * Method the user calls a volunteer
     */
    public String inviteVolunteer() {
        return "Мы ищем волонтера";
    }

    /**
     * Метод создания и сохранения волонтера
     * -----||-----
     * Сreate and save a volunteer method
     */
    public Volunteers createVolunteer(Volunteers volunteer) {
        return volunteersRepository.save(volunteer);
    }

    /**
     * Метод добавления и сохранения волонтера
     * -----||-----
     * Add and save a volunteer method
     */
    public String addVolunteer(Update update) {
        Volunteers volunteer = new Volunteers();
        volunteer.setChatId(update.getMessage().getChatId());
        volunteer.setName(update.getMessage().getChat().getFirstName());
        volunteer.setFree(true);
        volunteersRepository.save(volunteer);
        return "Добро пожаловать в волонтеры!";
    }

    /**
     * Метод удаления волонтера
     * -----||-----
     * Delete a volunteer method
     */
    public String deleteVolunteer(long chatId) {
        Volunteers volunteer = volunteersRepository.findByChatId(chatId);
        volunteersRepository.delete(volunteer);
        return "Вы удалены из волонтеров!";
    }

    /**
     * Метод находит список свободных волонтеров и конвертирует в SendMessage
     * сообщение о том что пользователю нужна помощь
     * -----||-----
     * list of free volunteers method
     */
    public List<SendMessage> getFreeVolunteers(Update update) {
        return null;
    }
}
