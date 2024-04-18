import Synchronization.org.Account;

public static void main(String[] args) {
    Account account1 = new Account("A1", 1000);
    Account account2 = new Account("A2", 2000);

    // Create and start threads to transfer money between accounts
    Thread thread1 = new Thread(() -> {
        boolean success = account1.transfer(account2, 50);
        if (success) {
            System.out.println("Transfer from account1 to account2 successful");
        } else {
            System.out.println("Transfer from account1 to account2 failed");
        }
    });

    Thread thread2 = new Thread(() -> {
        boolean success = account2.transfer(account1, 30);
        if (success) {
            System.out.println("Transfer from account2 to account1 successful");
        } else {
            System.out.println("Transfer from account2 to account1 failed");
        }
    });

    // Start
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
    System.out.println(STR."Final balance of account1: \{account1.getBalance()}");
    System.out.println(STR."Final balance of account2: \{account2.getBalance()}");
}

