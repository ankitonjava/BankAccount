
package in.services.accounting.personalbanking;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
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

    private static Beneficiary ankit;

    private static Beneficiary john;

    private Printer printer = new PrinterImpl();

    @BeforeClass
    public static void oneTimeSetupAndConfiguration()
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
     * After every test case, reset net amount to zero, so that testcases can be executed in-parallel in any order.
     *
     * @throws AccountingOperationException
     */
    @Before
    public void setUp() throws AccountingOperationException, NoSuchFieldException, IllegalAccessException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");
        Amount netAmount = ankitAccount.getNetAmount();
        if (netAmount.getValue().doubleValue() > 0)
        {
            ankitAccount.withDrawAmount(netAmount);
            Field f = ankitAccount.getClass().getDeclaredField("activities");
            f.setAccessible(true);
            List<Activity> list = (List) f.get(ankitAccount);
            list.clear();
        }
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
        org.junit.Assert.assertTrue("Net amount should be 300 inr.", ankitAccount.getNetAmount().equals(getAmountOfValue(300)));

        Amount transactionAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();

        // Transfer amount from Ankit [accountId = ankitAccountId] to John [accountId = johnAccountId]
        TransactionMediatorPlatform.getInstance().transferAmount(ankit, john, "ankitAccountId", "johnAccountId", transactionAmount);

        org.junit.Assert.assertTrue("Expected 200 left in ankit's account after transaction", ankitAccount.getNetAmount().equals(getAmountOfValue(200)));
        org.junit.Assert.assertTrue("Expected 100 in John's account after transaction", john.getAccountById("johnAccountId").getNetAmount().equals(getAmountOfValue(100)));

    }

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
        org.junit.Assert.assertTrue("Net amount should be 50 inr.", ankitAccount.getNetAmount().equals(getAmountOfValue(50)));
    }

    /**
     * Test case will do the following:
     * a) Deposit one amount in account
     * b) Verify statement record. Expected behaviour is, there should be one record in statement list
     * c) Verify transaction type as Deposit , transaction amount , transaction date and net balance amount in statement.
     *
     * @throws AccountingOperationException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void verifyStatementListIsGettingPopulated() throws AccountingOperationException, NoSuchFieldException, IllegalAccessException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        Date currentDate = new Date();
        // Ankit deposit 100 inr to his account id ("ankitAccountId")
        ankitAccount.depositAmount(getAmountOfValue(100));

        Field f = ankitAccount.getClass().getDeclaredField("activities");
        f.setAccessible(true);
        List<Activity> list = (List<Activity>) f.get(ankitAccount);
        org.junit.Assert.assertTrue("Statement list should have only one record.", list.size() == 1);
        org.junit.Assert.assertEquals(AccountingOperationType.DEPOSIT, list.get(0).getAccountingOperationType());
        org.junit.Assert.assertEquals(getAmountOfValue(100), list.get(0).getTransactionAmount());
        org.junit.Assert.assertEquals(getAmountOfValue(100), list.get(0).getBalanceAmount());
        org.junit.Assert.assertTrue(list.get(0).getTransactionDate().after(currentDate) || list.get(0).getTransactionDate().equals(currentDate));

    }

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
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void verifyFilteringOfStatement() throws AccountingOperationException, NoSuchFieldException, IllegalAccessException
    {
        Account ankitAccount = ankit.getAccountById("ankitAccountId");

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        ankitAccount.depositAmount(getAmountOfValue(100));
        ankitAccount.depositAmount(getAmountOfValue(50));
        ankitAccount.depositAmount(getAmountOfValue(20));
        ankitAccount.withDrawAmount(getAmountOfValue(100));

        long initialCounterValue = printer.getCounter().get();

        ankitAccount.printFilteredStatement(printer, AccountingOperationType.DEPOSIT, today, tomorrow);

        // print counter should return 3 as there were 3 deposits.
        org.junit.Assert.assertEquals(3, printer.getCounter().get() - initialCounterValue);

        initialCounterValue = printer.getCounter().get();
        ankitAccount.printFilteredStatement(printer, AccountingOperationType.WITHDRAW, today, tomorrow);

        //print counter should return 1 as there were only 1 withdraw.
        org.junit.Assert.assertEquals(1, printer.getCounter().get() - initialCounterValue);
    }

}

