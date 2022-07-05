public class BankAccountThreadRunner {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        final double AMOUNT = 100;
        final int REPETITIONS = 100;
        final int THREADS = 100;

        for (int i = 1; i <= THREADS; i++) {
            DepositRunnable d = new DepositRunnable(account, AMOUNT, REPETITIONS);
            WithdrawRunnable w = new WithdrawRunnable(account, AMOUNT, REPETITIONS);
            new Thread(d).start();
            new Thread(w).start();
        }
    }
}
