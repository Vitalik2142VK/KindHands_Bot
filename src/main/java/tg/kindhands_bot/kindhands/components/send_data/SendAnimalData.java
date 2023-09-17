package tg.kindhands_bot.kindhands.components.send_data;

public interface SendAnimalData {

    /**
     * Метод выдает правила знакомства с животным до того, как забрать его из приюта.
     * -----||-----
     * The method gives out the rules of acquaintance with the animal before taking it from the shelter.
     */
    String PrintTheAnimalIntroductionRules();

    /**
     * Метод выдает список документов, необходимых для того, чтобы взять животное из приюта.
     * -----||-----
     * The method returns a list of documents required to adopt an animal from a shelter.
     */
    String printListOfDocuments();

    /**
     * Метод выдает список рекомендаций по транспортировке животного.
     * -----||-----
     * The method gives a list of recommendations for transporting the animal.
     */

    String printRecommendationsForTransporting();

    /**
     * Метод выдает список рекомендаций по обустройству дома для щенка/котенка.
     * -----||-----
     * The method produces a list of home improvement recommendations for a puppy/kitten.
     */

    String printRecommendationsForHouseSmallAnimal();

    /**
     * Метод выдает список рекомендаций по обустройству дома для взрослого животного.
     * -----||-----
     * The method produces a list of recommendations for home improvement for an adult animal.
     */

    String printRecommendationsForHouseAdultAnimal();

    /**
     * Метод выдает список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение).
     * -----||-----
     * The method gives a list of recommendations for home improvement for an animal with disabilities (vision, movement).
     */

    String printRecommendationsForHouseDisabledAnimal();

    /**
     * Метод выдает список причин, почему могут отказать и не дать забрать собаку из приюта.
     * -----||-----
     * The method gives a list of reasons why they can refuse and not allow the dog to be taken from the shelter.
     */

    String printRejectionReason();

}
