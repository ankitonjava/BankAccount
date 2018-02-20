package in.services.accounting.personalbanking.views;

import java.time.Instant;
import java.util.Objects;

/**
 * Activity class is used for statement printing.
 */
public final class Activity
{
    // Transaction date
    private final Instant transactionDate;

    // Accounting operation (DEPOSIT,WITHDRAW)
    private final AccountingOperationType accountingOperationType;

    // Transaction amount
    private final Amount transactionAmount;

    // Balance Amount
    private final Amount balanceAmount;

    /**
     * Function used to construct Activity object
     *
     * @return
     */
    public static ActivityBuilder newBuilder()
    {
        return new ActivityBuilder();
    }

    private Activity(ActivityBuilder pActivityBuilder)
    {
        this.transactionDate = pActivityBuilder.date;
        this.accountingOperationType = pActivityBuilder.accountingOperationType;
        this.balanceAmount = pActivityBuilder.balanceAmount;
        this.transactionAmount = pActivityBuilder.transactionAmount;
    }

    /**
     * Function will return transaction date.
     *
     * @return
     */
    public Instant getTransactionDate()
    {
        return transactionDate;
    }

    /**
     * Function will return transaction type
     *
     * @return
     */
    public AccountingOperationType getAccountingOperationType()
    {
        return accountingOperationType;
    }

    /**
     * Function will return transaction amount
     *
     * @return
     */
    public Amount getTransactionAmount()
    {
        return transactionAmount;
    }

    /**
     * Function will return balance amount.
     *
     * @return
     */
    public Amount getBalanceAmount()
    {
        return balanceAmount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Activity activity = (Activity) o;
        return Objects.equals(transactionDate, activity.transactionDate) &&
                accountingOperationType == activity.accountingOperationType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(transactionDate, accountingOperationType);
    }

    public static class ActivityBuilder
    {
        public ActivityBuilder()
        {
            this.date = Instant.now();
        }

        public ActivityBuilder setAccountingOperationType(AccountingOperationType accountingOperationType)
        {
            this.accountingOperationType = accountingOperationType;
            return this;
        }

        public ActivityBuilder setTransactionAmount(Amount transactionAmount)
        {
            this.transactionAmount = transactionAmount;
            return this;
        }

        public ActivityBuilder setBalanceAmount(Amount balanceAmount)
        {
            this.balanceAmount = balanceAmount;
            return this;
        }

        public Activity build()
        {
            return new Activity(this);
        }

        private final Instant date;

        private AccountingOperationType accountingOperationType;

        private Amount transactionAmount;

        private Amount balanceAmount;
    }
}
