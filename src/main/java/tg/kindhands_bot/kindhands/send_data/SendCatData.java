package tg.kindhands_bot.kindhands.send_data;

public class SendCatData implements SendAnimalData{
    @Override
    public String PrintTheAnimalIntroductionRules() {
        System.out.println("правила знакомства с кошкой до того, как забрать из приюта.");
    }

    @Override
    public String printListOfDocuments() {
        System.out.println("список документов, необходимых для того, чтобы взять кошку из приюта.");
    }

    @Override
    public String printRecommendationsForTransporting() {
        System.out.println("список рекомендаций по транспортировке кошки.");
    }

    @Override
    public String printRecommendationsForHouseSmallAnimal() {
        System.out.println("список рекомендаций по обустройству дома для котенка");
    }

    @Override
    public String printRecommendationsForHouseAdultAnimal() {
        System.out.println("список рекомендаций по обустройству дома для взрослой кошки");
    }

    @Override
    public String printRecommendationsForHouseDisabledAnimal() {
        System.out.println("список рекомендаций по обустройству дома для кошки с ограниченными возможностями");
    }

    @Override
    public String printRejectionReason() {
        System.out.println("Выдать список причин, почему могут отказать и не дать забрать кошку из приюта.");
    }
}
