package in.services.accounting.personalbanking.views;

import java.math.BigDecimal;

import org.junit.Test;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

public class AmountTest
{
    /**
     * Test case verifies objection creation along with setting of proper values.
     */
    @Test
    public void verifyAmountObjectCreation()
    {
        Amount amount = Amount.newBuilder().setValue(new BigDecimal(200)).build();
        org.junit.Assert.assertNotNull(amount);
        org.junit.Assert.assertTrue("Amount field is not set correctly.", amount.getValue().equals(new BigDecimal(200)));
        org.junit.Assert.assertTrue("UnitOfMeasurement field is not set correctly.", amount.getUnitOfMeasurement().equals("inr"));
    }

    /**
     * Test case verify addition of certain amount
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyAmountAddition() throws AccountingOperationException
    {
        Amount firstAmount = Amount.newBuilder().setValue(new BigDecimal(200)).build();
        Amount secondAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        secondAmount.addAmount(firstAmount);
        org.junit.Assert.assertNotNull(secondAmount);
        org.junit.Assert.assertEquals("Amount is not updated correctly.", new BigDecimal(300), secondAmount.getValue());
    }

    /**
     * Test case verifies subtraction of certain amount
     *
     * @throws AccountingOperationException
     */
    @Test
    public void verifyAmountSubtraction() throws AccountingOperationException
    {
        Amount firstAmount = Amount.newBuilder().setValue(new BigDecimal(200)).build();
        Amount secondAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        firstAmount.subtractAmount(secondAmount);
        org.junit.Assert.assertNotNull(firstAmount);
        org.junit.Assert.assertEquals("Amount is not updated correctly.", new BigDecimal(100), secondAmount.getValue());
    }

    /**
     * Test case verifies behaviour of raising exception when two different Amount object (with different unit of measurements) are used for addition.
     *
     * @throws AccountingOperationException
     */
    @Test(expected = AccountingOperationException.class)
    public void verifyInvalidOperationExceptionWhileAddition() throws AccountingOperationException
    {
        Amount firstAmount = Amount.newBuilder().setValue(new BigDecimal(200)).setUnitOfMeasurement("usd").build();
        Amount secondAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        firstAmount.addAmount(secondAmount);
    }

    /**
     * Test case verifies behaviour of raising exception when two different Amount object (with different unit of measurements) are used for subtraction.
     *
     * @throws AccountingOperationException
     */
    @Test(expected = AccountingOperationException.class)
    public void verifyInvalidOperationExceptionWhileSubtraction() throws AccountingOperationException
    {
        Amount firstAmount = Amount.newBuilder().setValue(new BigDecimal(200)).setUnitOfMeasurement("usd").build();
        Amount secondAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        firstAmount.subtractAmount(secondAmount);
    }

    /**
     * Verify equality of two amount objects
     */
    @Test
    public void verifyTwoAmountObjectAreEqual()
    {
        Amount firstAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        Amount secondAmount = Amount.newBuilder().setValue(new BigDecimal(100)).build();
        org.junit.Assert.assertTrue(firstAmount.equals(secondAmount));

        firstAmount = Amount.newBuilder().setValue(new BigDecimal(10)).build();
        org.junit.Assert.assertFalse(firstAmount.equals(secondAmount));

        firstAmount = Amount.newBuilder().setValue(new BigDecimal(100)).setUnitOfMeasurement("usd").build();
        org.junit.Assert.assertFalse(firstAmount.equals(secondAmount));

    }
}
