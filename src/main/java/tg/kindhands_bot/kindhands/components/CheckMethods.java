package tg.kindhands_bot.kindhands.components;

import tg.kindhands_bot.kindhands.enums.CheckStatus;
import tg.kindhands_bot.kindhands.exceptions.IncorrectDataException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для методов, проверяющие полученные данные
 * -----||-----
 * A class for methods that verify the received data
 */
public class CheckMethods {

    /**
     * Метод проверки и приведения телефонного номера к формату +7(ххх)ххх-хх-хх
     * -----||-----
     * Method of checking and converting a phone number to the format +7(xxx)xxx-xx-xx
     */
    public static CheckStatus checkNumberPhone(String phone) {
        if (phone == null || "".equalsIgnoreCase(phone)) {
            return CheckStatus.NULL;
        } else {
            if (phone.length() < 10 || phone.length() > 16) {
                return CheckStatus.INCORRECT_DATA;
            }
            String number = phone.replaceAll("[^0-9]", "");
            if (number.length() > 10) {
                number = number.substring(number.length() - 10);
            }
            number = "+7" + number;
            number = number.replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})",
                    "$1($2)$3-$4-$5");
            return CheckStatus.TRUE;
        }
    }

    /**
     * Метод проверки и приведения телефонного номера к формату +7(ххх)ххх-хх-хх
     * -----||-----
     * Method of checking and converting a phone number to the format +7(xxx)xxx-xx-xx
     */
    public static List<String> checkFullName(String fullName) {
        if (fullName == null || "".equalsIgnoreCase(fullName)) {
            throw new NullPointerException("Была отправленная пустая трока. Повторите попытку.\n(Подходящий формат: Иванов Иван Иванович)");
        }
        List<String> arrFullName = new ArrayList<>(List.of(fullName.split(" ")));
        arrFullName.removeIf(String::isEmpty);

        if (arrFullName.size() < 2 || arrFullName.size() > 3) {
            throw new IncorrectDataException("Было отправлено менее 2 или более 3 слов. Повторите попытку." +
                    "\nЕсли у Вас двойная фамилия, имя или отчество, то укажите через символ '-'." +
                    "\nНапример: Иванова-Петровна Дарья Александровна.");
        }

        return arrFullName;
    }
}
