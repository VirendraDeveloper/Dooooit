package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.adapter.PaymentAdapter;
import cl.activaresearch.android_app.Dooit.models.PaymentBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 23 Jun,2018
 */
public class PaymentHistoryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Activity mContext;
    private PaymentAdapter mAdapter;
    private ListView lvPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        // Inflate the layout for this fragment
        lvPayment = (ListView) view.findViewById(R.id.lv_history);
        mAdapter = new PaymentAdapter(mContext);
        lvPayment.setAdapter(mAdapter);
        lvPayment.setEmptyView(view.findViewById(R.id.tv_empty));
        lvPayment.setOnItemClickListener(this);
        ((HomeActivity) mContext).showProgress();
        ApiHelper.getInstance().getPaymentHistory(SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN), new ApiCallback.PaymentListener() {
            @Override
            public void onSuccess(List<PaymentBean> paymentBeans) {
                ((HomeActivity) mContext).dismissProgress();
                mAdapter.addAllTask(paymentBeans);
            }

            @Override
            public void onFailure(String error) {
                ((HomeActivity) mContext).dismissProgress();
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PaymentBean paymentBean = (PaymentBean) mAdapter.getItem(position);
        Fragment fragment = new PaymentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PAYMENT, paymentBean);
        fragment.setArguments(bundle);
        HomeActivity.TAB_NO = 1;
        ((HomeActivity) mContext).loadFragment(fragment);
    }
}
