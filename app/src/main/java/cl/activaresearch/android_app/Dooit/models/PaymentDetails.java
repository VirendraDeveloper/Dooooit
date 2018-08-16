package cl.activaresearch.android_app.Dooit.models;

import java.util.List;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 21 Jul,2018
 */
public class PaymentDetails {
    /**
     * id : 29
     * requestedOn : 2018-07-21
     * validatedOn : null
     * paidOn : null
     * total : 5000
     * status : 1
     * account : {"accountHolderName":"Pradeep","accountHolderRUT":"ssdsd","accountNumber":"12312124124","accountBank":1,"accountType":"sdfsdf","accountBankName":"Banco BBVA"}
     * tasks : [{"name":"Test 1 Android","payment":"5000","validatedOn":"2018-07-15"}]
     */

    private int id;
    private String requestedOn;
    private Object validatedOn;
    private Object paidOn;
    private int total;
    private int status;
    private AccountBean account;
    private List<TasksBean> tasks;

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

    public List<TasksBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksBean> tasks) {
        this.tasks = tasks;
    }

    public static class TasksBean {
        /**
         * name : Test 1 Android
         * payment : 5000
         * validatedOn : 2018-07-15
         */

        private String name;
        private String payment;
        private String validatedOn;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getValidatedOn() {
            return validatedOn;
        }

        public void setValidatedOn(String validatedOn) {
            this.validatedOn = validatedOn;
        }
    }
}
