package tg.kindhands_bot.kindhands.shelters;

public class DogShelter implements Shelters{
    @Override
    public void getWorkSchedule() {
        System.out.println("Расписание приюта собак");
    }

    @Override
    public void getAddress() {
        System.out.println("Адрес приюта собак");
    }

    @Override
    public void getDrivingDirections() {
        System.out.println("Проезд до приюта собак");
    }

    @Override
    public void getSecurityContactDetails() {
        System.out.println("8-800-555-35-35 контакты охраны для пропуска авто в приют собак");
    }

    @Override
    public void getInfoSafetyPrecautions() {
        System.out.println("Техника безопасности на территории приюта собак: 1. Не кормить с рук....");
    }


}
