package tg.kindhands_bot.kindhands.components.shelters;

public interface Shelter {
    /**
     * Метод для вывода расписания работы приюта
     * -----||-----
     * Method for displaying shelter timetable
     */
    void getWorkSchedule();

    /**
     * Метод для вывода адреса приюта
     * -----||-----
     * Method for displaying shelter address
     */
    void getAddress();

    /**
     * Метод для вывода схемы проезда
     * -----||-----
     * Method for displaying directions
     */
    void getDrivingDirections();

    /**
     * Метод для вывода контактных данных охраны для оформления пропуска на машину.
     * -----||-----
     * Method for displaying the contact details of the guard for issuing a pass for the car.
     */
    void getSecurityContactDetails();

    /**
     * Метод для вывода общих рекомендации о технике безопасности на территории приюта.
     * -----||-----
     * A method for deriving general safety advice in the shelter area.
     */
    void getInfoSafetyPrecautions();


}
