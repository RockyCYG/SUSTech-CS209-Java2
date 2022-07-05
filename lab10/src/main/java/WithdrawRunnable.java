public class WithdrawRunnable implements Runnable {

    private final int DELAY = 1;
    private BankAccount account;
    private double amount;
    private int count;

    public WithdrawRunnable(BankAccount account, double amount, int count) {
        this.account = account;
        this.amount = amount;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= count; i++) {
                account.withdraw(amount);
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
