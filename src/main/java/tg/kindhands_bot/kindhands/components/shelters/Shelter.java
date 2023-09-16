package tg.kindhands_bot.kindhands.components.shelters;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Shelter {
    /**
     * Метод для вывода расписания работы приюта
     * -----||-----
     * Method for displaying shelter timetable
     */
    String getWorkSchedule();

    /**
     * Метод для вывода адреса приюта
     * -----||-----
     * Method for displaying shelter address
     */
    String getAddress();

    /**
     * Метод для вывода схемы проезда
     * -----||-----
     * Method for displaying directions
     */
    String getDrivingDirections();

    /**
     * Метод для вывода контактных данных охраны для оформления пропуска на машину.
     * -----||-----
     * Method for displaying the contact details of the guard for issuing a pass for the car.
     *
     * @return
     */
    String getSecurityContactDetails();

    /**
     * Метод для вывода общих рекомендации о технике безопасности на территории приюта.
     * -----||-----
     * A method for deriving general safety advice in the shelter area.
     *
     * @return
     */
    String getInfoSafetyPrecautions();

    /**
     * Медод для вывода подробной информации о приюте.
     * -----//-----
     * A method for displaying detailed information about the shelter.
     */
    String getDetailedInfo();

}
