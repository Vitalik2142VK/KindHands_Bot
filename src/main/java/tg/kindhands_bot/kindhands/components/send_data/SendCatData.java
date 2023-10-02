package tg.kindhands_bot.kindhands.components.send_data;

public class SendCatData implements SendAnimalData{
    @Override
    public String PrintTheAnimalIntroductionRules() {
        return "правила знакомства с кошкой до того, как забрать из приюта.";
    }

    @Override
    public String printListOfDocuments() {
        return "список документов, необходимых для того, чтобы взять кошку из приюта.";
    }

    @Override
    public String printRecommendationsForTransporting() {
        return "список рекомендаций по транспортировке кошки.";
    }

    @Override
    public String printRecommendationsForHouseSmallAnimal() {
        return "список рекомендаций по обустройству дома для котёнка";
    }

    @Override
    public String printRecommendationsForHouseAdultAnimal() {
        return "список рекомендаций по обустройству дома для взрослой кошки";
    }

    @Override
    public String printRecommendationsForHouseDisabledAnimal() {
        return "список рекомендаций по обустройству дома для кошки с ограниченными возможностями";
    }

    @Override
    public String printRejectionReason() {
        return "Выдать список причин, почему могут отказать и не дать забрать кошку из приюта.";
    }
}
