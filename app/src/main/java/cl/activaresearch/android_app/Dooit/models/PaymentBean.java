package cl.activaresearch.android_app.Dooit.models;

import java.io.Serializable;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 21 Jul,2018
 */
public class PaymentBean implements Serializable{

    /**
     * id : 28
     * requestedOn : 2018-07-21
     * validatedOn : null
     * paidOn : null
     * total : 5000
     * status : 1
     * account : {"accountHolderName":"Pradeep","accountHolderRUT":"ssdsd","accountNumber":"12312124124","accountBank":1,"accountType":"sdfsdf","accountBankName":"Banco BBVA"}
     */

    private int id;
    private String requestedOn;
    private Object validatedOn;
    private Object paidOn;
    private int total;
    private int status;
    private AccountBean account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public Object getValidatedOn() {
        return validatedOn;
    }

    public void setValidatedOn(Object validatedOn) {
        this.validatedOn = validatedOn;
    }

    public Object getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(Object paidOn) {
        this.paidOn = paidOn;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

}
