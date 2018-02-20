package in.services.accounting.personalbanking.views;

import java.math.BigDecimal;
import java.util.Objects;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

/**
 * Amount described in terms of value and unit of measurement.
 * Default unit of measurement is inr and default amount value will be set to zero.
 *
 */
public final class Amount
{
    // Unit of measurement Ex: USD, INR
    private final String unitOfMeasurement;

    // Amount value
    private BigDecimal value;

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
    public void addAmount(Amount pAmount) throws AccountingOperationException
    {
        if (this.unitOfMeasurement.equals(pAmount.getUnitOfMeasurement()))
        {
            this.value = this.value.add(pAmount.getValue());
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
    public void subtractAmount(Amount pAmount) throws AccountingOperationException
    {

        if (this.getValue().doubleValue() <= 0)
        {
            throw new AccountingOperationException("Insufficient Balance for withdrawal.");
        }

        if (this.unitOfMeasurement.equals(pAmount.getUnitOfMeasurement()))
        {
            this.value = this.value.subtract(pAmount.getValue());
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
    public String toString()
    {
        return "Amount{" +
                "unitOfMeasurement='" + unitOfMeasurement + '\'' +
                ", value=" + value +
                '}';
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
        return Objects.equals(unitOfMeasurement, amount.unitOfMeasurement) &&
                Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(unitOfMeasurement, value);
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
