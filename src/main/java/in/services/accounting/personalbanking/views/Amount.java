package in.services.accounting.personalbanking.views;

import java.math.BigDecimal;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

/**
 * Amount described in terms of value and unit of measurement.
 */
public final class Amount
{
    // Unit of measurement Ex: USD, INR
    private final String unitOfMeasurement;

    // Amount value
    private final BigDecimal value;

    /**
     * Amount class Constructor
     *
     * @param pBuilder
     */
    private Amount(AmountBuilder pBuilder)
    {
        this.unitOfMeasurement = pBuilder.unitOfMeasurement;
        this.value = pBuilder.value;
    }

    public String getUnitOfMeasurement()
    {
        return unitOfMeasurement;
    }

    /**
     * Function used for amount addition. Function will sum-up amount having same unit of measurement and raise exception in case of different unit of measurement.
     * Scope of amount addition is limited up to amount of same unit of measurement.
     *
     * @param pAmount
     * @return
     * @throws AccountingOperationException
     */
    public Amount addAmount(Amount pAmount) throws AccountingOperationException
    {
        if (this.unitOfMeasurement.equals(pAmount.getUnitOfMeasurement()))
        {
            return new AmountBuilder().setUnitOfMeasurement(this.unitOfMeasurement).setValue(this.getValue().add(pAmount.getValue())).build();
        }
        else
        {
            throw new AccountingOperationException("Operation can't be performed either due to different unit of measurement.");
        }
    }

    /**
     * Function used for amount subtraction. Function will subtract amount having same unit of measurement and raise exception in case of different unit of measurement.
     *
     * @param pAmount
     * @return
     * @throws AccountingOperationException
     */
    public Amount subtractAmount(Amount pAmount) throws AccountingOperationException
    {

        if (this.getValue().doubleValue() <= 0)
        {
            throw new AccountingOperationException("Insufficient Balance for withdrawal.");
        }

        if (this.unitOfMeasurement.equals(pAmount.getUnitOfMeasurement()))
        {
            return new AmountBuilder().setUnitOfMeasurement(this.unitOfMeasurement).setValue(this.getValue().subtract(pAmount.getValue())).build();
        }
        else
        {
            throw new AccountingOperationException("Operation can't be performed either due to different unit of measurement.");
        }
    }

    /**
     * Return amount value
     *
     * @return
     */
    public BigDecimal getValue()
    {
        return value;
    }

    /**
     * Builder object used for creation Amount ({@link Amount}) object.
     *
     * @return
     */
    public static AmountBuilder newBuilder()
    {
        return new AmountBuilder();
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

        Amount amount = (Amount) o;

        if (unitOfMeasurement != null ? !unitOfMeasurement.equals(amount.unitOfMeasurement) : amount.unitOfMeasurement != null)
        {
            return false;
        }
        return value != null ? value.equals(amount.value) : amount.value == null;
    }

    @Override
    public int hashCode()
    {
        int result = unitOfMeasurement != null ? unitOfMeasurement.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Amount{" +
                "unitOfMeasurement='" + unitOfMeasurement + '\'' +
                ", value=" + value +
                '}';
    }

    public static class AmountBuilder
    {
        private String unitOfMeasurement;

        private BigDecimal value;

        public AmountBuilder()
        {
            this.unitOfMeasurement = "inr";
            this.value = new BigDecimal(0);
        }

        public AmountBuilder setUnitOfMeasurement(String pUnitOfMeasurement)
        {
            if (unitOfMeasurement != null)
            {
                this.unitOfMeasurement = pUnitOfMeasurement;
            }
            return this;
        }

        public AmountBuilder setValue(BigDecimal pValue)
        {
            if (value != null)
            {
                this.value = pValue;
            }
            return this;
        }

        public Amount build()
        {
            return new Amount(this);
        }

    }
}
