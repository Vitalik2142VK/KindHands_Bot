package tg.kindhands_bot.kindhands.components.send_data;

public class SendDogData implements SendAnimalData{
    @Override
    public String PrintTheAnimalIntroductionRules() {
        return "правила знакомства с собакой до того, как забрать из приюта.";
    }

    @Override
    public String printListOfDocuments() {
        return "список документов, необходимых для того, чтобы взять собаку из приюта.";
    }

    @Override
    public String printRecommendationsForTransporting() {
        return "список рекомендаций по транспортировке собаки.";
    }

    @Override
    public String printRecommendationsForHouseSmallAnimal() {
        return "список рекомендаций по обустройству дома для щенка";
    }

    @Override
    public String printRecommendationsForHouseAdultAnimal() {
        return "список рекомендаций по обустройству дома для взрослой собаки";
    }

    @Override
    public String printRecommendationsForHouseDisabledAnimal() {
        return "список рекомендаций по обустройству дома для собаки с ограниченными возможностями";
    }

    @Override
    public String printRejectionReason() {
        return "Выдать список причин, почему могут отказать и не дать забрать собаку из приюта.";
    }

    /**
     * Метод выдает рекомендации по проверенным кинологам для дальнейшего обращения к ним
     * -----||-----
     * The method gives recommendations on proven cynologists for further referral to them
     */
    public String printInformationToVerifiedDogHandlers(){
        return "рекомендации по проверенным кинологам для дальнейшего обращения к ним (для собак)";
    }

    /**
     * Метод выдает советы кинолога по первичному общению с собакой
     * -----||-----
     * The method gives the cynologist's advice on the initial communication with the dog
     */

    public String printInitialCommunicationCynologistAdvices(){
        return "советы кинолога по первичному общению с собакой ";
    }

}

