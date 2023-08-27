package tg.kindhands_bot.kindhands.components;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

        var butDog = new InlineKeyboardButton();
        butDog.setText("Собачий");
        butDog.setCallbackData("DOG_SH");

        var butCat = new InlineKeyboardButton();
        butCat.setText("Кошачий");
        butCat.setCallbackData("CAT_SH");

        rowInLine.add(butDog);
        rowInLine.add(butCat);

        rowsInLine.add(rowInLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        return message;
    }
}
