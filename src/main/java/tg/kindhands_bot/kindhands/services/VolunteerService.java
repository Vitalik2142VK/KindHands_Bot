package tg.kindhands_bot.kindhands.services;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.repositories.VolunteersRepository;

import java.util.List;

/**
 * Работа с БД волонтеров. Принимает желающих стать волонтерами, а так же удаляет из БД.
 * Отправляет уведомление волонтерам. Дает доступ к БД животных, проверка отсчетов.
 * -----||-----
 * Working with the database of volunteers. Accepts those who want to become volunteers, and also removes them from the database.
 * Sends a notification to volunteers. Gives access to the database of animals, checking reports.
 */

@Service
public class VolunteerService {
    private final VolunteersRepository volunteersRepository;

    public VolunteerService(VolunteersRepository volunteersRepository) {
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
    public Volunteer createVolunteer(Volunteer volunteer) {
        return volunteersRepository.save(volunteer);
    }

    /**
     * Метод добавления и сохранения волонтера
     * -----||-----
     * Add and save a volunteer method
     */
    public String addVolunteer(Update update) {
        Volunteer volunteer = new Volunteer();
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
        Volunteer volunteer = volunteersRepository.findByChatId(chatId);
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
