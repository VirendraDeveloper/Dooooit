package cl.activaresearch.android_app.Dooit.servercommunication;


import java.util.List;

import cl.activaresearch.android_app.Dooit.models.BankBean;
import cl.activaresearch.android_app.Dooit.models.AccountBean;
import cl.activaresearch.android_app.Dooit.models.CategoryBean;
import cl.activaresearch.android_app.Dooit.models.PaymentBean;
import cl.activaresearch.android_app.Dooit.models.PaymentDetails;
import cl.activaresearch.android_app.Dooit.models.RegionBean;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.models.UserBean;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 17 Jul,2018
 */
public class ApiCallback {
    public interface CategoryListener {
        void onSuccess(List<CategoryBean> categories);

        void onFailure(String error);
    }

    public interface SurveyListener {
        void onSuccess(List<SurveyBean> surveyBeans);

        void onFailure(String error);
    }

    public interface UserListener {
        void onSuccess(UserBean userBean);

        void onFailure(String error);
    }

    public interface TasksListener {
        void onSuccess(List<TaskBean> taskBeans);

        void onFailure(String error);
    }

    public interface TaskListener {
        void onSuccess(TaskBean taskBean);

        void onFailure(String error);
    }

    public interface BanksListener {
        void onSuccess(List<BankBean> task);

        void onFailure(String error);
    }

    public interface Listener {
        void onSuccess(String result);

        void onFailure(String error);
    }

    public interface AccountListener {
        void onSuccess(AccountBean account);

        void onFailure(String error);
    }

    public interface PaymentDetailsListener {
        void onSuccess(PaymentDetails details);

        void onFailure(String error);
    }

    public interface RegionsListener {
        void onSuccess(List<RegionBean> regionBeanList);

        void onFailure(String error);
    }

    public interface PaymentListener {
        void onSuccess(List<PaymentBean> regionBeanList);

        void onFailure(String error);
    }

}
