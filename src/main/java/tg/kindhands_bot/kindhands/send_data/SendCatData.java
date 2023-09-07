package tg.kindhands_bot.kindhands.send_data;

public class SendCatData implements SendAnimalData{
    @Override
    public void PrintTheAnimalIntroductionRules() {
        System.out.println("правила знакомства с кошкой до того, как забрать из приюта.");
    }

    @Override
    public void printListOfDocuments() {
        System.out.println("список документов, необходимых для того, чтобы взять кошку из приюта.");
    }

    @Override
    public void printRecommendationsForTransporting() {
        System.out.println("список рекомендаций по транспортировке кошки.");
    }

    @Override
    public void printRecommendationsForHouseSmallAnimal() {
        System.out.println("список рекомендаций по обустройству дома для котенка");
    }

    @Override
    public void printRecommendationsForHouseAdultAnimal() {
        System.out.println("список рекомендаций по обустройству дома для взрослой кошки");
    }

    @Override
    public void printRecommendationsForHouseDisabledAnimal() {
        System.out.println("список рекомендаций по обустройству дома для кошки с ограниченными возможностями");
    }

    @Override
    public void printRejectionReason() {
        System.out.println("Выдать список причин, почему могут отказать и не дать забрать кошку из приюта.");
    }
}
