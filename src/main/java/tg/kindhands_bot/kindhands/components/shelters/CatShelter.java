package tg.kindhands_bot.kindhands.components.shelters;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CatShelter implements Shelter {
    @Override
    public String getWorkSchedule() {

        return "Расписание приюта кошек";
    }

    @Override
    public String getAddress() {
        return "Адрес приюта кошек";
    }

    @Override
    public String getDrivingDirections() {
        return "Проезд до приюта кошек";
    }

    @Override
    public String getSecurityContactDetails() {
        return "8-800-555-35-35 контакты охраны для пропуска авто в приют кошек";
    }

    @Override
    public String getInfoSafetyPrecautions() {
       return "Техника безопасности на территории приюта кошек: 1. Не кормить с рук....";
    }

    @Override
    public String getDetailedInfo() {
        return "Подробная информация кошачем о приюте: ";
    }

}
