package tg.kindhands_bot.kindhands.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tg.kindhands_bot.kindhands.components.MessagesBotFromControllers;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.User;
import tg.kindhands_bot.kindhands.entities.Volunteer;
import tg.kindhands_bot.kindhands.entities.tamed.TamedAnimal;
import tg.kindhands_bot.kindhands.enums.ReportStatus;
import tg.kindhands_bot.kindhands.repositories.ReportAnimalRepository;
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
    private final ReportAnimalRepository reportAnimalRepository;

    private final MessagesBotFromControllers messagesBot;

    private final Logger log = LoggerFactory.getLogger(VolunteerService.class);

    public VolunteerService(VolunteersRepository volunteersRepository, ReportAnimalRepository reportAnimalRepository, MessagesBotFromControllers messagesBot) {
        this.volunteersRepository = volunteersRepository;
        this.reportAnimalRepository = reportAnimalRepository;
        this.messagesBot = messagesBot;
    }

    /**
     * Выводит не проверенные отчеты пользователей о животных.
     * -----||-----
     * Displays unverified user reports about animals.
     */
    public List<ReportAnimal> getReports() {
        List<ReportAnimal> reports = reportAnimalRepository.findByReportStatus(ReportStatus.ON_INSPECTION);
        if (reports.isEmpty()) {
            throw new NullPointerException("Не проверенных отчетов нет.");
        }

        return reports;
    }

    /**
     * Меняет статус отчета, указанный волонтером.
     * -----||-----
     * Changes the status of the report specified by the volunteer.
     */
    public String changeStatusReport(Long id, String messageUser, ReportStatus reportStatus) {
        ReportAnimal reportAnimal = reportAnimalRepository.findById(id).orElse(null);
        if (reportAnimal == null) {
            throw new NullPointerException("Отчет с id '" + id + "' не найден.");
        }
        User user = reportAnimal.getTamedAnimal().getUser();
        reportAnimal.setReportStatus(reportStatus);
        reportAnimalRepository.save(reportAnimal);

        if (messageUser == null || messageUser.isEmpty()) {
            messagesBot.sendMessageUser(user, "Ваш отчет, присланный " + reportAnimal.getDate() + " принят.\nСпасибо!");
        } else {
            messagesBot.sendMessageUser(user, "Ваш отчет, присланный " + reportAnimal.getDate() + " принят.\n" +
                    "Оставленный комментарий:\n" +
                    messageUser +
                    "\nСпасибо!");
        }

        return "Статус отчета с id '" + id + "' изменен на: " + reportStatus.name();
    }

    /**
     * Принимает кандидата в волонтеры
     * -----||-----
     * Accepts a volunteer candidate
     */
    public String addVolunteer(Long id) {
        Volunteer volunteer = volunteersRepository.findById(id).orElse(null);
        if (volunteer == null) {
            throw new NullPointerException("Волонтер с id '" + id + "' не найден.");
        }
        User user = volunteer.getUser();
        volunteer.setAdopted(true);

        volunteersRepository.save(volunteer);
        log.info("Влонтер '" + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic() + "' добавлен.");

        messagesBot.sendMessageUser(user, "Поздравляю! Ваша кандидатура в волонтеры принята.");

        return "Влонтер '" + user.getLastName() + " " + user.getFirstName() + " " + user.getPatronymic() + "' добавлен.";
    }

    /**
     * Метод удаления волонтера
     * -----||-----
     * Delete a volunteer method
     */
    public String deleteVolunteer(long id) {
        Volunteer volunteer = volunteersRepository.findById(id).orElse(null);
        if (volunteer == null) {
            throw new NullPointerException("Волонтер с id '" + id + "' не найден.");
        }
        User user = volunteer.getUser();
        volunteersRepository.delete(volunteer);

        log.info("Влонтер '" + user.getLastName() + "' удален.");

        return "Волонтер " + user.getLastName() + " " + user.getFirstName() + ", удален." ;
    }

    /**
     * Получение всех действующих волонтеров
     * -----||-----
     * Getting all active volunteers
     */
    public List<Volunteer> getAllVolunteers() {
        return volunteersRepository.findByAdoptedTrue();
    }

    /**
     * Выводит список всех желающих стать волонтером.
     * -----||-----
     * Displays a list of everyone who wants to become a volunteer.
     */
    public List<Volunteer> getAllBecomeVolunteers() {
        return volunteersRepository.findByAdoptedFalse();
    }
}
