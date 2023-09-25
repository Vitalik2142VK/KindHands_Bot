package tg.kindhands_bot.kindhands.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.kindhands_bot.kindhands.components.CheckMethods;
import tg.kindhands_bot.kindhands.entities.ReportAnimal;
import tg.kindhands_bot.kindhands.entities.Volunteer;
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


    private final Logger log = LoggerFactory.getLogger(VolunteerService.class);

    public VolunteerService(VolunteersRepository volunteersRepository, ReportAnimalRepository reportAnimalRepository) {
        this.volunteersRepository = volunteersRepository;
        this.reportAnimalRepository = reportAnimalRepository;
    }

    /**
     * Выводит не проверенные доклады пользователей о животных.
     * -----||-----
     * Displays unverified user reports about animals.
     */
    public List<ReportAnimal> getReports() {
        List<ReportAnimal> reports = reportAnimalRepository.findByReportStatus(ReportStatus.ON_INSPECTION);
        if (reports.isEmpty()) throw new NullPointerException("Не проверенных отчетов нет.");
        return reports;
    }

    /**
     * Метод создания и сохранения волонтера
     * -----||-----
     * Сreate and save a volunteer method
     */
    public Volunteer createVolunteer(Volunteer volunteer) {
        log.info("Влонтер '" + volunteer.getFirstName() + "' добавлен.");

        return volunteersRepository.save(volunteer);
    }

    /**
     * Метод сохраняет пользователя в БД Volunteer и отправляет строку, о том что волонтер принят
     * -----||-----
     * Add a volunteer method
     */
    public String addVolunteer(Update update, String phone) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(update.getMessage().getChatId());
        volunteer.setFirstName(update.getMessage().getChat().getFirstName());
        volunteer.setAdopted(true);
      
        try {
            volunteer.setPhone(CheckMethods.checkNumberPhone(phone));
        } catch (RuntimeException e) {
            return e.getMessage();
        }

        volunteersRepository.save(volunteer);
        log.info("Влонтер '" + volunteer.getFirstName() + "' добавлен.");

        return "Ваша кандидатура на рассмотрении, с Вами свяжутся";
    }

    /**
     * Метод удаления волонтера
     * -----||-----
     * Delete a volunteer method
     */

    public String deleteVolunteer(long id) {
        Volunteer volunteer = volunteersRepository.findById(id).orElse(null);
        if (volunteer != null) {
            volunteersRepository.delete(volunteer);
          
            log.info("Влонтер '" + volunteer.getFirstName() + "' удален.");
          
            return "Вы удалены из волонтеров!";
        } else {
            return "Волонтер не найден";
        }
    }
// НА ПОТОМ
    /**
     * Метод находит список принятых волонтеров и конвертирует в SendMessage
     * сообщение о том что пользователю нужна помощь
     * -----||-----
     * list of free volunteers method
     */
//    public List<SendMessage> getAdoptedVolunteers(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            Long chatId = update.getMessage().getChatId();
//
//            if (messageText.contains("CALL_VOLUNTEER")) {
//                var textToVolunteers = "Пользователь " + chatId + " запросил помощь волонтера";
//                var adoptedVolunteers = volunteersRepository.findByAdoptedTrue();
//                List<SendMessage> messages = new ArrayList<>();
//                for (Volunteer adoptedVolunteer : adoptedVolunteers) {
//                    SendMessage message = new SendMessage(adoptedVolunteer.getChatId(), textToVolunteers);
//                    messages.add(message);
//                }


    /**
     * Метод получения всех волонтеров
     * -----||-----
     * Get all volunteers method
     */
    public List<Volunteer> getAllVolunteers() {
        return volunteersRepository.findAll();
    }
}
