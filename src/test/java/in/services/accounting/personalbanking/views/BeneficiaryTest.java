package in.services.accounting.personalbanking.views;

import org.junit.Test;

public class BeneficiaryTest
{
    @Test
    public void verifyBeneficaryObjectCreation()
    {
        Beneficiary beneficiary = Beneficiary.newBuilder().setBeneficiaryId("1").setBeneficiaryName("Ankit").build();
        org.junit.Assert.assertTrue("Invalid ID", beneficiary.getBeneficiaryId().equals("1"));
        org.junit.Assert.assertTrue("Invalid Name", beneficiary.getBeneficiaryName().equals("Ankit"));
    }

    @Test
    public void verifyBeneficaryObjectEquality()
    {
        Beneficiary beneficiary = Beneficiary.newBuilder().setBeneficiaryId("1").setBeneficiaryName("Ankit").build();
        Beneficiary beneficiaryCopy = Beneficiary.newBuilder().setBeneficiaryId("1").setBeneficiaryName("Ankit").build();
        org.junit.Assert.assertTrue("Invalid Object Equality", beneficiary.equals(beneficiaryCopy));
        beneficiaryCopy = Beneficiary.newBuilder().setBeneficiaryId("2").setBeneficiaryName("Ankit1").build();
        org.junit.Assert.assertFalse("Invalid Object Equality", beneficiary.equals(beneficiaryCopy));
    }
}
