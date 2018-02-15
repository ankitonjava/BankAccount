
package in.services.accounting.personalbanking.views;

import java.math.BigDecimal;

import org.junit.Test;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

public class AccountTest
{
    /**
     * Verify amount deposit functionality for an account.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyAccountDeposit() throws AccountingOperationException
    {
        Account ankitAccount = new Account("1");

        Amount t1Amount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        // Deposit 100 inr
        ankitAccount.depositAmount(t1Amount);
        org.junit.Assert.assertTrue("Deposit is unsuccessful.", t1Amount.equals(ankitAccount.getNetAmount()));

        Amount t2Amount = Amount.newBuilder().setValue(new BigDecimal(200)).build();
        //Deposit 200 inr
        ankitAccount.depositAmount(t2Amount);

        // Verify net balance should be 200+100 = 300
        org.junit.Assert.assertTrue("Deposit is unsuccessful.", Amount.newBuilder().setValue(new BigDecimal(300)).build().equals(ankitAccount.getNetAmount()));
    }

    /**
     * Verify amount withdraw operation from an account.
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyAccountWithdraw() throws AccountingOperationException
    {
        Account ankitAccount = new Account("1");

        Amount t1Amount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        // Deposit 100 inr
        ankitAccount.depositAmount(t1Amount);
        org.junit.Assert.assertTrue("Deposit is unsuccessful.", t1Amount.equals(ankitAccount.getNetAmount()));

        // Withdraw 50 inr
        Amount t2Amount = Amount.newBuilder().setValue(new BigDecimal(50)).build();
        ankitAccount.withDrawAmount(t2Amount);

        // Verify net balance should be 100-50 = 50
        org.junit.Assert.assertTrue(t2Amount.equals(ankitAccount.getNetAmount()));
    }

    /**
     * Negative case, when there is no balance, and withdraw operation is performed.
     * Expected behaviour is AccountingOperationException should be raised in case of withdraw with negative balances.
     *
     * @throws AccountingOperationException
     */
    @Test(expected = AccountingOperationException.class)
    public void verifyWithdrawWhenNegativeBalance() throws AccountingOperationException
    {
        Account ankitAccount = new Account("1");

        Amount t2Amount = Amount.newBuilder().setValue(new BigDecimal(50)).build();
        ankitAccount.withDrawAmount(t2Amount);
    }

}
