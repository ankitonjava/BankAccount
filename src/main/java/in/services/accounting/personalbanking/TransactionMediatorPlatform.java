
package in.services.accounting.personalbanking;

import in.services.accounting.personalbanking.exceptions.AccountingOperationException;
import in.services.accounting.personalbanking.views.Amount;
import in.services.accounting.personalbanking.views.Beneficiary;

public class TransactionMediatorPlatform
{
    private static TransactionMediatorPlatform transactionMediatorPlatform = new TransactionMediatorPlatform();

    private TransactionMediatorPlatform()
    {
    }

    public static TransactionMediatorPlatform getInstance()
    {
        return transactionMediatorPlatform;
    }

    /**
     * Transfer amount from one account to another
     *
     * @param pFromBeneficiary
     * @param pToBeneficiary
     * @param pFromBeneficiaryAccountId
     * @param pToBeneficiaryAccountId
     * @param pAmount
     * @throws AccountingOperationException
     */
    public void transferAmount(Beneficiary pFromBeneficiary, Beneficiary pToBeneficiary, String pFromBeneficiaryAccountId, String pToBeneficiaryAccountId, Amount pAmount) throws AccountingOperationException
    {
        makeTransaction(pFromBeneficiary, pToBeneficiary, pFromBeneficiaryAccountId, pToBeneficiaryAccountId, pAmount);
    }

    private void makeTransaction(Beneficiary pFromBeneficiary, Beneficiary pToBeneficiary, String pFromBeneficiaryAccountId, String pToBeneficiaryAccountId, Amount pAmount) throws AccountingOperationException
    {
        pFromBeneficiary.getAccountById(pFromBeneficiaryAccountId).withDrawAmount(pAmount);
        pToBeneficiary.getAccountById(pToBeneficiaryAccountId).depositAmount(pAmount);
    }

}
