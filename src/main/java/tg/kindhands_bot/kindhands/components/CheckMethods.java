package tg.kindhands_bot.kindhands.components;

import tg.kindhands_bot.kindhands.exceptions.IncorrectDataExceptionAndSendMessage;
import tg.kindhands_bot.kindhands.exceptions.NullPointerExceptionAndSendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Класс для методов, проверяющие полученные данные
 * -----||-----
 * A class for methods that verify the received data
 */
public class CheckMethods {
    private CheckMethods() {
    }

    /**
     * Метод проверки и приведения телефонного номера к формату +7(ххх)ххх-хх-хх
     * -----||-----
     * Method of checking and converting a phone number to the format +7(xxx)xxx-xx-xx
     */
    public static String checkNumberPhone(String phone) {
        if (phone == null || "".equalsIgnoreCase(phone)) {
            throw new NullPointerExceptionAndSendMessage("При вводе номера телефона, была отправлена пустая строка." , "Поле с номером телефона не должно быть пустым.");
        } else {
            if (phone.length() < 10 || phone.length() > 30) {
                throw new IncorrectDataExceptionAndSendMessage("Не корректно введен номер телефона." ,"Номер телефона введен некорректно. Исправьте или введите заново." +
                        "\n(Подходящие форматы: +7(800)000-00-00, 88000000000).") ;
            }
            String number = phone.replaceAll("[\\D]", "");
            if (number.length() > 10) {
                number = number.substring(1 ,11);
            }
            number = "+7" + number;
            return number.replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d{2})",
                    "$1($2)$3-$4-$5");
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
            throw new IncorrectDataExceptionAndSendMessage("Не корректно ФИО пользователем." ,"Было отправлено менее 2 или более 3 слов. Повторите попытку." +
                    "\nЕсли у Вас двойная фамилия, имя или отчество, то укажите через символ '-'." +
                    "\nНапример: Иванова-Петровна Дарья Александровна.");
        }

        return arrFullName.stream()
                .map(s -> s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.toList());
    }
}
