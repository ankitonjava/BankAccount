package in.services.accounting.personalbanking.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;

public final class Beneficiary
{
    private final String beneficiaryId;

    private final String beneficiaryName;

    private final List<Account> accounts;

    public List<Account> getAccounts()
    {
        return Collections.unmodifiableList(accounts);
    }

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
        this.accounts = new ArrayList<Account>();
    }

    public static BeneficiaryBuilder newBuilder()
    {
        return new BeneficiaryBuilder();
    }

    public String getBeneficiaryId()
    {
        return beneficiaryId;
    }

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

        if (beneficiaryId != null ? !beneficiaryId.equals(that.beneficiaryId) : that.beneficiaryId != null)
        {
            return false;
        }
        return beneficiaryName != null ? beneficiaryName.equals(that.beneficiaryName) : that.beneficiaryName == null;
    }

    @Override
    public int hashCode()
    {
        int result = beneficiaryId != null ? beneficiaryId.hashCode() : 0;
        result = 31 * result + (beneficiaryName != null ? beneficiaryName.hashCode() : 0);
        return result;
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
