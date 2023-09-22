package tg.kindhands_bot.kindhands.components;

import tg.kindhands_bot.kindhands.enums.CheckStatus;

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
}
