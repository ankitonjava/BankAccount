package in.services.accounting.personalbanking.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

/**
 * This class represents end-customer.
 */
public final class Beneficiary
{
    private final String beneficiaryId;

    private final String beneficiaryName;

    // one beneficiary can have more than one account associated with them.
    private final List<Account> accounts;

    /**
     * Returns list of all account associated with beneficiary.
     *
     * @return
     */
    public List<Account> getAccounts()
    {
        return Collections.unmodifiableList(accounts);
    }

    /**
     * Returns account based on account id (passed in argument).
     *
     * @param pAccountId
     * @return
     * @throws AccountingOperationException
     */
    public Account getAccountById(String pAccountId) throws AccountingOperationException
    {
        for (Account account : accounts)
        {
            if (account.getAccountId().equals(pAccountId))
            {
                return account;
            }
        }
        throw new AccountingOperationException("Account not found.");
    }

    public boolean linkAccount(Account pAccount)
    {
        return this.accounts.add(pAccount);
    }

    private Beneficiary(BeneficiaryBuilder pBuilder)
    {
        this.beneficiaryId = pBuilder.beneficiaryId;
        this.beneficiaryName = pBuilder.beneficiaryName;
        this.accounts = new ArrayList<>();
    }

    public static BeneficiaryBuilder newBuilder()
    {
        return new BeneficiaryBuilder();
    }

    /**
     * Return beneficiary id
     *
     * @return
     */
    public String getBeneficiaryId()
    {
        return beneficiaryId;
    }

    /**
     * Returns beneficiary name.
     *
     * @return
     */
    public String getBeneficiaryName()
    {
        return beneficiaryName;
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
        Beneficiary that = (Beneficiary) o;
        return Objects.equals(beneficiaryId, that.beneficiaryId) &&
                Objects.equals(beneficiaryName, that.beneficiaryName) &&
                Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(beneficiaryId, beneficiaryName, accounts);
    }

    public static class BeneficiaryBuilder
    {
        private String beneficiaryId;

        private String beneficiaryName;

        public BeneficiaryBuilder setBeneficiaryId(String beneficiaryId)
        {
            this.beneficiaryId = beneficiaryId;
            return this;
        }

        public BeneficiaryBuilder setBeneficiaryName(String beneficiaryName)
        {
            this.beneficiaryName = beneficiaryName;
            return this;
        }

        public Beneficiary build()
        {
            return new Beneficiary(this);
        }

    }
}
