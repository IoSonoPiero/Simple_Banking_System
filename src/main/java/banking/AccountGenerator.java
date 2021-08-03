package banking;

import java.util.concurrent.ThreadLocalRandom;

public class AccountGenerator {

    public static Account generateAccount() {
        return new Account(generateAccountNumber(), generatePin(), 0);
    }

    private static String generatePin() {
        int number = ThreadLocalRandom.current().nextInt(9999);
        return String.format("%04d", number);
    }

    public static boolean validateCard(String cardNumber) {
        String tempCard = cardNumber.substring(0, cardNumber.length() - 1);
        tempCard = tempCard + generateChecksum(tempCard);

        return cardNumber.equals(tempCard);
    }

    private static int generateChecksum(String accountNumber) {
        int sum = 0;
        for (int i = 0; i < accountNumber.length(); i += 2) {
            int value = Integer.parseInt(String.valueOf(accountNumber.charAt(i))) * 2;
            sum += value > 9 ? value - 9 : value;
        }
        for (int i = 1; i < accountNumber.length(); i += 2) {
            sum += Integer.parseInt(String.valueOf(accountNumber.charAt(i)));
        }
        return ((10 - sum % 10) % 10);
    }

    private static String generateAccountNumber() {
        final String BIN = "400000";
        int account = ThreadLocalRandom.current().nextInt(999999999);
        String accountNumber = BIN + String.format("%09d", account);
        return accountNumber + generateChecksum(accountNumber);
    }
}