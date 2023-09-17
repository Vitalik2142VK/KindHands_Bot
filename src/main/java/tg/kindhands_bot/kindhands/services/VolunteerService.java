package tg.kindhands_bot.kindhands.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(VolunteerService.class);

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
        log.info("Влонтер '" + volunteer.getName() + "' добавлен.");

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
        volunteer.setName(update.getMessage().getChat().getFirstName());
        volunteer.setAdopted(true);
        volunteer.setPhone(printPhone(phone));//добавила проверку на приведение номера телефона к единому формату +7(ххх)ххх-хх-хх
        volunteersRepository.save(volunteer);
        log.info("Влонтер '" + volunteer.getName() + "' добавлен.");
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
            log.info("Влонтер '" + volunteer.getName() + "' удален.");
            return "Вы удалены из волонтеров!";
        } else {
            return "Волонтер не найден";
        }
    }
//// НА ПОТОМ
//    /**
//     * Метод находит список свободных волонтеров и конвертирует в SendMessage
//     * сообщение о том что пользователю нужна помощь
//     * -----||-----
//     * list of free volunteers method
//     */
//    public List<SendMessage> getFreeVolunteers(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            Long chatId=update.getMessage().getChatId();
//
//            if (messageText.contains("CALL_VOLUNTEER")) {
//                var textToVolunteers=messageText("Пользователь запросил помощь волонтера");
//                        var freeVolunteers=volunteersRepository.getVolunteersByIsFreeTrue();
//                for (Volunteer volunteer : volunteers) {
//                    sendMessage(volunteer.getChatId(), textToVolunteers);
//
//
//                }
//            }
//        }
//      return (List<SendMessage>) volunteersRepository.getVolunteersByIsFreeTrue().stream().findAny()
//                .orElseThrow(() -> new RuntimeException("Все волонтеры заняты."));
//    }

    /**
     * Метод получения всех волонтеров
     * -----||-----
     * Get all volunteers method
     */

    public List<Volunteer> getAllVolunteers() {
        return volunteersRepository.findAll();
    }

    /**
     * Метод приведения телефонного номера к формату +7(ххх)ххх-хх-хх
     * -----||-----
     * Phone format +7(ххх)ххх-хх-хх method
     */
    public String printPhone(String phone) {
        if (phone == null || "".equalsIgnoreCase(phone)) {
            return "Введите номер телефона";
        } else {
            if (phone.length() < 10 || phone.length() > 16) {
                return "Это не похоже на номер телефона. Исправьте или введите заново";
            }
            String number = phone.replaceAll("[^0-9]", "");
            if (number.length() > 10) {
                number = number.substring(number.length() - 10);
            }
            number = "+7" + number;
            number = number.replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})",
                    "$1($2)$3-$4-$5");
            return "Ваш номер телефона записан: " + number;
        }
    }
}
