package tg.kindhands_bot.kindhands.shelters;

public class CatShelter implements Shelters{
    @Override
    public void getWorkSchedule() {
        System.out.println("Расписание приюта кошек");
    }

    @Override
    public void getAddress() {
        System.out.println("Адрес приюта кошек");
    }

    @Override
    public void getDrivingDirections() {
        System.out.println("Проезд до приюта кошек");
    }

    @Override
    public void getSecurityContactDetails() {
        System.out.println("8-800-555-35-35 контакты охраны для пропуска авто в приют кошек");
    }

    @Override
    public void getInfoSafetyPrecautions() {
        System.out.println("Техника безопасности на территории приюта кошек: 1. Не кормить с рук....");
    }

}
