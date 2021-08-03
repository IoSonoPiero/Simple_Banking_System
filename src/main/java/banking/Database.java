package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Database {
    String databaseName;

    public void getBalance(Account account) {
        String getBalanceSQL = "SELECT balance FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = this.connect();) {

            try (PreparedStatement getBalance = connection.prepareStatement(getBalanceSQL)) {
                getBalance.setString(1, account.getAccountNumber());
                getBalance.setString(2, account.getPinNumber());

                try (ResultSet cards = getBalance.executeQuery()) {
                    if (cards.next()) {
                        account.setBalance(cards.getLong("balance"));
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Account userLogin(String accountNumber, String pinNumber) {

        String loginSQL = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = this.connect();) {

            try (PreparedStatement getAccount = connection.prepareStatement(loginSQL)) {
                getAccount.setString(1, accountNumber);
                getAccount.setString(2, pinNumber);

                try (ResultSet cards = getAccount.executeQuery()) {
                    if (cards.next()) {
                        return new Account(cards.getString("number"), cards.getString("pin"), cards.getLong("balance"));
                        //return true;
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private Connection connect() {
        String url = "jdbc:sqlite:" + databaseName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return connection;
    }


    public void createDatabase() {
        try (Connection connection = this.connect();) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createAccount(Account account) {

        String insertCardSQL = "INSERT INTO \"card\" " +
                "(number, pin) VALUES (?, ?)";

        try (Connection connection = this.connect();) {
            // Disable auto-commit mode
            connection.setAutoCommit(false);

            try (PreparedStatement insertAccount = connection.prepareStatement(insertCardSQL)) {

                // Insert the account
                insertAccount.setString(1, account.getAccountNumber());
                insertAccount.setString(2, account.getPinNumber());
                insertAccount.executeUpdate();

                connection.commit();

            } catch (SQLException exception) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void setFileName(String databaseFileName) {
        this.databaseName = databaseFileName;
    }

    public void addIncome(Account account, long income) {
        String updateBalanceSQL = "UPDATE card SET balance = balance + ? WHERE number = ? AND pin = ?";

        try (Connection connection = this.connect();) {
            // Disable auto-commit mode
            connection.setAutoCommit(false);

            try (PreparedStatement updateBalance = connection.prepareStatement(updateBalanceSQL)) {

                // Insert the account
                updateBalance.setLong(1, income);
                updateBalance.setString(2, account.getAccountNumber());
                updateBalance.setString(3, account.getPinNumber());
                updateBalance.executeUpdate();

                connection.commit();

            } catch (SQLException exception) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        account.setBalance(account.getBalance() + income);
    }

    public void closeAccount(Account account) {
        String closeAccountSQL = "DELETE FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = this.connect();) {
            // Disable auto-commit mode
            connection.setAutoCommit(false);

            try (PreparedStatement deleteAccount = connection.prepareStatement(closeAccountSQL)) {

                // Insert the account
                deleteAccount.setString(1, account.getAccountNumber());
                deleteAccount.setString(2, account.getPinNumber());
                deleteAccount.executeUpdate();

                connection.commit();

            } catch (SQLException exception) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean checkCardExistent(String destinationCard) {
        String loginSQL = "SELECT number FROM card WHERE number = ?";

        try (Connection connection = this.connect();) {

            try (PreparedStatement getAccount = connection.prepareStatement(loginSQL)) {
                getAccount.setString(1, destinationCard);

                try (ResultSet cards = getAccount.executeQuery()) {
                    if (cards.next()) {
                        return true;
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void transferMoney(String destinationCard, long moneyToTransfer) {
        String moneyTransferSQL = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection connection = this.connect();) {
            // Disable auto-commit mode
            connection.setAutoCommit(false);

            try (PreparedStatement updateBalance = connection.prepareStatement(moneyTransferSQL)) {

                // Insert the account
                updateBalance.setLong(1, moneyToTransfer);
                updateBalance.setString(2, destinationCard);
                updateBalance.executeUpdate();

                connection.commit();

            } catch (SQLException exception) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}