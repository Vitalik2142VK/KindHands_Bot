package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static tg.kindhands_bot.kindhands.utils.MessageConstants.*;

/**
 * Класс для отображения кнопок пользователю и возвращения результата вывода.
 * -----||-----
 * A class for displaying buttons to the user and returning the output result.
 */
public class NavigationMenu {

    /**
     * Метод, выводящий на экран кнопки выбора приюта.
     * -----||-----
     * The method that displays the shelter selection buttons on the screen.
     */
    public static SendMessage choosingShelter(long chatId) {
        SendMessage message = new SendMessage(String.valueOf(chatId), "Выберите нужный вам приют.");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var butDog = createButton("Собачий", DOG_BUTTON);
        var butCat = createButton("Кошачий", CAT_BUTTON);

        rowInLine.add(butDog);
        rowInLine.add(butCat);

        rowsInLine.add(rowInLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;
    }

    /**
     * Метод, выводящий на экран кнопки "меню" приюта.
     * -----||-----
     * The method that displays the "menu" buttons of the shelter.
     */
    public static EditMessageText menuShelter(Update update, String shelter) {
        EditMessageText message = new EditMessageText(".");
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        String animalText;

        switch (shelter) {
            case DOG_BUTTON:
                message.setText("Вы выбрали собачий приют.");
                animalText = "_D";
                break;
            case CAT_BUTTON:
                message.setText("Вы выбрали кошачий приют.");
                animalText = "_C";
                break;
            default:
                return null; // заменить на Exception
        }

        var infoButton = createButton("Узнать информацию о приюте", SCHEMA_INFO + animalText);
        var howGetButton = createButton("Как взять животное из приюта", SCHEMA_TAKE_INFO + animalText);
        var sendReportButton = createButton("Прислать отчёт о питомце", SCHEMA_SEND_REPORT + animalText);
        var callVolunteerButton = createButton("Позвать волонтёра", CALL_VOLUNTEER);
        var assistanceToShelterButton = createButton("Помощь приюту", ASSISTANCE_SHELTER);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInThreeLine = new ArrayList<>();

        rowInLine.add(infoButton);
        rowInLine.add(howGetButton);
        rowInTwoLine.add(sendReportButton);
        rowInTwoLine.add(callVolunteerButton);
        rowInThreeLine.add(assistanceToShelterButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);
        rowsInLine.add(rowInThreeLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;
    }

    /**
     * Метод, выводящий на экран кнопки меню "Помощь приюту".
     * -----//-----
     * A method that displays the "Help Shelter" menu buttons on the screen.
     */
    public static EditMessageText menuAssistShelter(Update update) {

        EditMessageText message = new EditMessageText("Выберите пункт:");
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        var requisitesButton = createButton("Реквизиты", REQUISITES);
        var necessaryButton = createButton("Необходимое", NECESSARY);
        var becomeVolunteer = createButton("Стать волонтёром", BECOME_VOLUNTEER);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInThreeLine = new ArrayList<>();

        rowInLine.add(requisitesButton);
        rowInTwoLine.add(necessaryButton);
        rowInThreeLine.add(becomeVolunteer);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);
        rowsInLine.add(rowInThreeLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;

    }

    /**
     * Метод, выводящий на экран кнопки меню "Узнать информацию о приюте".
     * -----//-----
     * A method that displays the menu buttons "Find out information about the shelter".
     */
    public static EditMessageText menuShelterInfo(Update update, String shelter) {
        EditMessageText message = new EditMessageText();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        String animalText;

        switch (shelter) {
            case DOG_INFO:
                message.setText("Информация о собачем приюте:");
                animalText = "_D";
                break;
            case CAT_INFO:
                message.setText("Информация о кошачем приюте:");
                animalText = "_C";
                break;
            default:
                return null; // заменить на Exception
        }

        var aboutShelterButton = createButton("Подробнее о приюте", ABOUT_SHELTER + animalText);
        var scheduleButton = createButton("Расписание приюта", SCHEDULE + animalText);
        var securityContactButton = createButton("Контактные данные охраны", SECURITY_CONTACT + animalText);
        var safetyRecommendationButton = createButton("Рекомендации по технике безопасности", SAFETY_RECOMMENDATION);
        var callContactButton = createButton("Записать контактные данные для связи", USER_CALL_CONTACT);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInThreeLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInFourLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInFiveLine = new ArrayList<>();

        rowInLine.add(aboutShelterButton);
        rowInTwoLine.add(scheduleButton);
        rowInThreeLine.add(securityContactButton);
        rowInFourLine.add(safetyRecommendationButton);
        rowInFiveLine.add(callContactButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);
        rowsInLine.add(rowInThreeLine);
        rowsInLine.add(rowInFourLine);
        rowsInLine.add(rowInFiveLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;

    }

    /**
     * Метод, для создания кнопки.
     * -----//-----
     * Method for creating a button.
     */
    private static InlineKeyboardButton createButton(String text, String callbackData) {

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        return button;
    }
}
