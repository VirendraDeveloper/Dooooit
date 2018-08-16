package cl.activaresearch.android_app.Dooit.models;

import java.io.Serializable;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 18 Jul,2018
 */
public class AccountBean implements Serializable{

    /**
     * accountHolderName : null
     * accountHolderRUT : null
     * accountNumber : null
     * accountBank : null
     * accountType : null
     */

    private String accountHolderName;
    private String accountHolderRUT;
    private String accountNumber;
    private int accountBank;
    private String accountType;
    private String accountBankName;

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountHolderRUT() {
        return accountHolderRUT;
    }

    public void setAccountHolderRUT(String accountHolderRUT) {
        this.accountHolderRUT = accountHolderRUT;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(int accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }
}
