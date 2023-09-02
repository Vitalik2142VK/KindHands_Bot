package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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

        var butDog = createButton("Собачий", "DOG_SH");
        var butCat = createButton("Кошачий", "CAT_SH");

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
        EditMessageText message = new EditMessageText();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        String isDogText;

        switch (shelter) {
            case "DOG_SH":
                message.setText("Вы выбрали собачий приют.");
                isDogText = "_D";
                break;
            case "CAT_SH":
                message.setText("Вы выбрали кошачий приют.");
                isDogText = "_C";
                break;
            default:
                return null; // заменить на Exception
        }

        var infoButton = createButton("Узнать информацию о приюте", "INFO_GET" + isDogText);
        var howGetButton = createButton("Как взять животное из приюта", "HOW_GET" + isDogText);
        var sendReportButton = createButton("Прислать отчёт о питомце", "SEND_RP" + isDogText);
        var callVolunteerButton = createButton("Позвать волонтёра", "CALL_VL");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInTwoLine = new ArrayList<>();

        rowInLine.add(infoButton);
        rowInLine.add(howGetButton);
        rowInTwoLine.add(sendReportButton);
        rowInTwoLine.add(callVolunteerButton);

        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInTwoLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;
    }

    /**
     * Метод, для создания кнопки
     * -----//-----
     * Method for creating a button
     */

    private static InlineKeyboardButton createButton(String text, String callbackData) {

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        return button;
    }
}
