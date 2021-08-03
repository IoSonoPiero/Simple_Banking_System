package banking;

public class Account {
    private String accountNumber;
    private String pinNumber;
    private long balance;

    @Override
    public String toString() {
        return "Your card number:\n" + getAccountNumber() +
                "\nYour card PIN:\n" + getPinNumber() + "\n";
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Account(String accountNumber, String pinNumber, long balance) {
        this.accountNumber = accountNumber;
        this.pinNumber = pinNumber;
        this.balance = balance;
    }
}