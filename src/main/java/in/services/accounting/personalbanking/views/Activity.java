package in.services.accounting.personalbanking.views;

import java.util.Date;

public final class Activity
{
    private final Date transactionDate;

    private final AccountingOperationType accountingOperationType;

    private final Amount transactionAmount;

    private final Amount balanceAmount;

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

    public Date getTransactionDate()
    {
        return transactionDate;
    }

    public AccountingOperationType getAccountingOperationType()
    {
        return accountingOperationType;
    }

    public Amount getTransactionAmount()
    {
        return transactionAmount;
    }

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
