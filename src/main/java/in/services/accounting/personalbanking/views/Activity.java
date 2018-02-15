package in.services.accounting.personalbanking.views;

import java.util.Date;

/**
 * Activity class is used for statement printing.
 */
public final class Activity
{
    // Transaction date
    private final Date transactionDate;

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
    public Date getTransactionDate()
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

        if (!transactionDate.equals(activity.transactionDate))
        {
            return false;
        }
        return accountingOperationType == activity.accountingOperationType;
    }

    @Override
    public int hashCode()
    {
        int result = transactionDate.hashCode();
        result = 31 * result + accountingOperationType.hashCode();
        return result;
    }

    public static class ActivityBuilder
    {
        public ActivityBuilder()
        {
            this.date = new Date();
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

        private Date date;

        private AccountingOperationType accountingOperationType;

        private Amount transactionAmount;

        private Amount balanceAmount;
    }
}
