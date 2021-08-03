package banking;

public class Main {
    public static void main(String[] args) {
        SimpleBankingSystem bankingSystem = new SimpleBankingSystem();
        bankingSystem.run(args[1]);
    }
}