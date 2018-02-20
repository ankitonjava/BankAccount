
package in.services.accounting.personalbanking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;
import in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.Printer;
import in.services.accounting.personalbanking.in.services.accounting.personalbanking.externalinterfaces.internal.PrinterImpl;
import in.services.accounting.personalbanking.views.Account;
import in.services.accounting.personalbanking.views.AccountingOperationType;
import in.services.accounting.personalbanking.views.Activity;
import in.services.accounting.personalbanking.views.Amount;
import in.services.accounting.personalbanking.views.Beneficiary;

/**
 * Test case will verify following use cases:
 * <p>
 * 1) Transaction between two beneficiaries.
 * 2) Deposit some amount in beneficiary account
 * 3) Withdraw some amount in beneficiary account.
 * 4) Filtering of Statement based on operation (withdraw or deposit), date etc
 */

public class EndToEndTestCase
{

    private Beneficiary ankit;

    private Beneficiary john;

    private Printer printer = new PrinterImpl();

    /**
     * Create beneficiary and their respective accounts.
     * Link account with beneficiary.
     */
    @Before
    public void setUp()
    {

        ankit = getBeneficiary("1", "Ankit");
        john = getBeneficiary("2", "John");

        Account ankitAccount = new Account("ankitAccountId");
        Account johnAccount = new Account("johnAccountId");

        ankit.linkAccount(ankitAccount);
        john.linkAccount(johnAccount);

    }

    private static Beneficiary getBeneficiary(String id, String name)
    {
        return Beneficiary.newBuilder().setBeneficiaryId(id).setBeneficiaryName(name).build();
    }

    private static Amount getAmountOfValue(double value)
    {
        return Amount.newBuilder().setValue(new BigDecimal(value)).build();
    }

    /**
     * Verify Transaction between two beneficiaries.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyTransactionBetweenTwoBeneficiaries() throws AccountingOperationException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");
        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));
        // Ankit deposit 200 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(Amount.newBuilder().setValue(new BigDecimal(200)).build());

        //Total amount in Ankit should be 300
        org.junit.Assert.assertThat(ankitAccount.getNetAmount(), org.hamcrest.core.Is.is(getAmountOfValue(300)));

        Amount transactionAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();

        // Transfer amount from Ankit [accountId = ankitAccountId] to John [accountId = johnAccountId]
        TransactionMediatorPlatform.getInstance().transferAmount(ankit, john, "ankitAccountId", "johnAccountId", transactionAmount);

        org.junit.Assert.assertThat(ankitAccount.getNetAmount(), org.hamcrest.core.Is.is(getAmountOfValue(200)));
        org.junit.Assert.assertThat(john.getAccountById("johnAccountId").getNetAmount(), org.hamcrest.core.Is.is(getAmountOfValue(100)));

    }

    /**
     * Negative test. Try to retrieve account based on invalid account id.
     * Try to access account not associate with beneficiary.
     *
     * @throws AccountingOperationException
     */
    @Test(expected = AccountingOperationException.class)
    public void verifyValidationForInvalidAccountId() throws AccountingOperationException
    {
        ankit.getAccountById("johnAccountId");
    }

    /**
     * Verify Deposit some amount in beneficiary account
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyDeposit() throws AccountingOperationException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));

        // Ankit deposit 200 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(200));

        //Total amount in Ankit should be 300
        org.junit.Assert.assertTrue("Net amount should be 300 inr.", ankitAccount.getNetAmount().equals(getAmountOfValue(300)));
    }

    /**
     * Verify Withdraw some amount in beneficiary account.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyWithdraw() throws AccountingOperationException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));
        // Ankit deposit 50 inr to his account id ("ankitAccountId")
        ankitAccount.withDrawAmount(getAmountOfValue(50));

        //Total amount in Ankit should be 100 - 50 = 50
        org.junit.Assert.assertThat("Net amount should be 50 inr.", ankitAccount.getNetAmount(), org.hamcrest.core.Is.is(getAmountOfValue(50)));
    }

    /**
     * Test case will do the following:
     * a) Deposit one amount in account
     * b) Verify statement record. Expected behaviour is, there should be one record in statement list
     * c) Verify transaction type as Deposit , transaction amount , transaction date and net balance amount in statement.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyStatementListIsGettingPopulated() throws AccountingOperationException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        Instant currentDateTime = Instant.now();
        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));

        List<Activity> list = ankitAccount.getActivities();
        org.junit.Assert.assertThat(1, org.hamcrest.core.Is.is(list.size()));
        org.junit.Assert.assertEquals(AccountingOperationType.DEPOSIT, list.get(0).getAccountingOperationType());
        org.junit.Assert.assertEquals(getAmountOfValue(100), list.get(0).getTransactionAmount());
        org.junit.Assert.assertEquals(getAmountOfValue(100), list.get(0).getBalanceAmount());
        org.junit.Assert.assertThat(true, org.hamcrest.core.Is.is(list.get(0).getTransactionDate().isAfter(currentDateTime) || list.get(0).getTransactionDate().equals(currentDateTime)));

    }

    /**
     * Verify Statement printing.
     * Test case will gather initial counter for print function (function used to print statement).
     * Perform some operation on account (DEPOSIT or WITHDRAW) , then, execute print statement.
     * Test case verifies number of records getting printed using counter on printer services.
     *
     * @throws AccountingOperationException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void verifyFullStatement() throws AccountingOperationException, NoSuchFieldException, IllegalAccessException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        long initialCounterValue = printer.getCounter().get();

        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));

        ankitAccount.printFullStatement(printer);
        org.junit.Assert.assertTrue("Counter should be updated properly.", (printer.getCounter().get() - initialCounterValue) > 0);
    }

    /**
     * Verify filtering of account statement based on transaction type.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyFilteringOfStatement() throws AccountingOperationException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        Instant today = Instant.now();
        Instant tomorrow = Instant.now().plus(1, ChronoUnit.DAYS);
        ankitAccount.depositAmount(getAmountOfValue(100));
        ankitAccount.depositAmount(getAmountOfValue(50));
        ankitAccount.depositAmount(getAmountOfValue(20));
        ankitAccount.withDrawAmount(getAmountOfValue(100));

        long initialCounterValue = printer.getCounter().get();

        ankitAccount.printFilteredStatement(printer, AccountingOperationType.DEPOSIT, today, tomorrow);

        // print counter should return 3 as there were 3 deposits.
        org.junit.Assert.assertThat(3L, org.hamcrest.core.Is.is(printer.getCounter().get() - initialCounterValue));

        initialCounterValue = printer.getCounter().get();
        ankitAccount.printFilteredStatement(printer, AccountingOperationType.WITHDRAW, today, tomorrow);

        //print counter should return 1 as there were only 1 withdraw.
        org.junit.Assert.assertThat(1L, org.hamcrest.core.Is.is(printer.getCounter().get() - initialCounterValue));

    }

}

