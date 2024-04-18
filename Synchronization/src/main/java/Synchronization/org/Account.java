package Synchronization.org;

public class Account {
    private final String accountId;
    private double balance;

    public Account(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public synchronized boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // Insufficient funds
        }
        balance -= amount;
        return true;
    }

    public boolean transfer(Account toAccount, double amount) {
        // Ensure that both accounts are not null
        if (toAccount == null) {
            return false;
        }

        // Lock both accounts to ensure thread-safe transfer
        synchronized (this) {
            synchronized (toAccount) {
                // Check if there are sufficient funds
                if (withdraw(amount)) {
                    toAccount.deposit(amount);
                    return true; // Successful transfer
                }
            }
        }
        return false; // Transfer failed due to insufficient funds
    }

    public static void main(String[] args) {
        Account account1 = new Account("A1", 1000);
        Account account2 = new Account("A2", 2000);

        // Create and start threads to transfer money between accounts
        Thread thread1 = new Thread(() -> {
            boolean success = account1.transfer(account2, 500);
            if (success) {
                System.out.println("Transfer from account1 to account2 successful");
            } else {
                System.out.println("Transfer from account1 to account2 failed");
            }
        });

        Thread thread2 = new Thread(() -> {
            boolean success = account2.transfer(account1, 300);
            if (success) {
                System.out.println("Transfer from account2 to account1 successful");
            } else {
                System.out.println("Transfer from account2 to account1 failed");
            }
        });

        // Start the threads
        thread1.start();
        thread2.start();

        // Wait for the threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print the final balances
        System.out.println("Final balance of account1: " + account1.getBalance());
        System.out.println("Final balance of account2: " + account2.getBalance());
    }
}
