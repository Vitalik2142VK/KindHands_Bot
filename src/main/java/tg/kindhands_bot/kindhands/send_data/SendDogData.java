package tg.kindhands_bot.kindhands.send_data;

public class SendDogData implements SendAnimalData{
    @Override
    public void PrintTheAnimalIntroductionRules() {
        System.out.println("правила знакомства с собакой до того, как забрать из приюта.");
    }

    @Override
    public void printListOfDocuments() {
        System.out.println("список документов, необходимых для того, чтобы взять собаку из приюта.");
    }

    @Override
    public void printRecommendationsForTransporting() {
        System.out.println("список рекомендаций по транспортировке собаки.");
    }

    @Override
    public void printRecommendationsForHouseSmallAnimal() {
        System.out.println("список рекомендаций по обустройству дома для щенка");
    }

    @Override
    public void printRecommendationsForHouseAdultAnimal() {
        System.out.println("список рекомендаций по обустройству дома для взрослой собаки");
    }

    @Override
    public void printRecommendationsForHouseDisabledAnimal() {
        System.out.println("список рекомендаций по обустройству дома для собаки с ограниченными возможностями");
    }

    @Override
    public void printRejectionReason() {
        System.out.println("Выдать список причин, почему могут отказать и не дать забрать собаку из приюта.");
    }

    /**
     * Метод выдает рекомендации по проверенным кинологам для дальнейшего обращения к ним
     * -----||-----
     * The method gives recommendations on proven cynologists for further referral to them
     */
    public void printInformationToVerifiedDogHandlers(){
        System.out.println("рекомендации по проверенным кинологам для дальнейшего обращения к ним (для собак)");
    }

    /**
     * Метод выдает советы кинолога по первичному общению с собакой
     * -----||-----
     * The method gives the cynologist's advice on the initial communication with the dog
     */

    public void printInitialCommunicationCynologistAdvices(){
        System.out.println("советы кинолога по первичному общению с собакой ");
    }

}

