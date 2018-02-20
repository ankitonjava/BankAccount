package in.services.accounting.personalbanking.views;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;
import in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.Printer;

/**
 *
 */
public class Account
{
    // Unique account id
    private final String accountId;

    /**
     * Function will return current net amount for an account.
     *
     * @return
     */
    public Amount getNetAmount()
    {
        return netAmount;
    }

    private final Amount netAmount;

    public List<Activity> getActivities()
    {
        return Collections.unmodifiableList(activities);
    }

    // Used for maitaining activities (account statement)
    private final List<Activity> activities;

    public Account(String accountId)
    {
        this.accountId = accountId;
        activities = new LinkedList<>();
        netAmount = Amount.newBuilder().build();
    }

    /**
     * Deposit amount.
     *
     * @param pAmount
     * @throws AccountingOperationException
     */
    public synchronized void depositAmount(Amount pAmount) throws AccountingOperationException
    {
        netAmount.addAmount(pAmount);
        activities.add(Activity.newBuilder().setAccountingOperationType(AccountingOperationType.DEPOSIT).setTransactionAmount(pAmount).setBalanceAmount(netAmount).build());
    }

    public synchronized void withDrawAmount(Amount pAmount) throws AccountingOperationException
    {

        netAmount.subtractAmount(pAmount);
        activities.add(Activity.newBuilder().setAccountingOperationType(AccountingOperationType.WITHDRAW).setTransactionAmount(pAmount).setBalanceAmount(netAmount).build());

    }

    /**
     * Print entire statement
     *
     * @param printer
     */
    public void printFullStatement(Printer printer)
    {
        if (printer != null)
        {
            printer.print("\n------------------------------------ ");
            printer.print(" Account Id: " + this.accountId);
            printer.print("\n------------------------------------");
            StringBuilder stringBuilder;
            for (Activity activity : activities)
            {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Transaction Date :").append(activity.getTransactionDate()).append(", Type: ").append(activity.getAccountingOperationType()).append(", transaction amount: ").append(activity.getTransactionAmount()).append(", Balance :").append(activity.getBalanceAmount());
                printer.print(stringBuilder.toString());
            }
            printer.print("------------------------------------\n");
        }

    }

    /**
     * Print filtered statement. Filterable fields are transaction type, and date
     *
     * @param printer
     * @param accountingOperationType
     * @param startDate
     * @param endDate
     */
    public void printFilteredStatement(Printer printer, AccountingOperationType accountingOperationType, Instant startDate, Instant endDate)
    {
        if (printer != null)
        {
            StringBuilder stringBuilder;

            for (Activity activity : activities)
            {
                if (
                        (AccountingOperationType.BOTH == (accountingOperationType) || activity.getAccountingOperationType() == (accountingOperationType))
                                &&
                                (activity.getTransactionDate().toEpochMilli() >= startDate.toEpochMilli())
                                &&
                                activity.getTransactionDate().isBefore(endDate)
                        )
                {
                    stringBuilder = new StringBuilder();

                    stringBuilder.append("Transaction Date :").append(activity.getTransactionDate()).append(", Type: ").append(activity.getAccountingOperationType()).append(", transaction amount: ").append(activity.getTransactionAmount()).append(", Balance :").append(activity.getBalanceAmount());
                    printer.print(stringBuilder.toString());
                }
            }
        }

    }

    public String getAccountId()
    {
        return accountId;
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
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) &&
                Objects.equals(netAmount, account.netAmount) &&
                Objects.equals(activities, account.activities);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(accountId, netAmount, activities);
    }
}
