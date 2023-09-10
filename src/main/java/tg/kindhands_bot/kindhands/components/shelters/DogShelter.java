package tg.kindhands_bot.kindhands.components.shelters;

public class DogShelter implements Shelter {
    @Override
    public String getWorkSchedule() {
        return "Расписание приюта собак";
    }

    @Override
    public String getAddress() {
        return "Адрес приюта собак";
    }

    @Override
    public String getDrivingDirections() {
        return "Проезд до приюта собак";
    }

    @Override
    public String getSecurityContactDetails() {
        return "8-800-555-35-35 контакты охраны для пропуска авто в приют собак";
    }

    @Override
    public String getInfoSafetyPrecautions() {
        return "Техника безопасности на территории приюта собак: 1. Не кормить с рук....";

    }


}
