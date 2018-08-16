package cl.activaresearch.android_app.Dooit.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.HomeActivity;
import cl.activaresearch.android_app.Dooit.adapter.PaymentTaskAdapter;
import cl.activaresearch.android_app.Dooit.models.PaymentBean;
import cl.activaresearch.android_app.Dooit.models.PaymentDetails;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 21 Jul,2018
 */
public class PaymentDetailFragment extends Fragment implements View.OnClickListener, HomeActivity.BackPressed {
    private Activity mContext;
    private TextView tvDate, tvTaskName, tvBalance, tvName, tvRTU, tvBank, tvType, tvBal, tvAccount;
    private String TAG = PaymentDetailFragment.class.getName();
    private ImageView ivBack;
    private PaymentBean paymentBean;
    private ListView lvPaymentDetails;
    private PaymentTaskAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_details, container, false);
        // Inflate the layout for this fragment
        initUI(view);
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            paymentBean = (PaymentBean) bundle.getSerializable(Constants.PAYMENT);
        }
        String token = SharedPreferenceUtility.getInstance(mContext).getString(Constants.TOKEN);
        HomeActivity.setOnBackPressed(this);
        ((HomeActivity) mContext).showProgress();
        ApiHelper.getInstance().paymentDetails(token, paymentBean.getId() + "", new ApiCallback.PaymentDetailsListener() {
            @Override
            public void onSuccess(PaymentDetails details) {
                ((HomeActivity) mContext).dismissProgress();
                tvBalance.setText("$" + details.getTotal());
                tvAccount.setText(details.getAccount().getAccountNumber());
                tvName.setText(details.getAccount().getAccountHolderName());
                tvBank.setText(details.getAccount().getAccountBankName());
                tvType.setText(details.getAccount().getAccountType());
                tvRTU.setText(details.getAccount().getAccountHolderRUT());
                mAdapter = new PaymentTaskAdapter(mContext, details.getTasks());
                lvPaymentDetails.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(String error) {
                ((HomeActivity) mContext).dismissProgress();

            }
        });


        return view;
    }

    private void initUI(View view) {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvName = (TextView) view.findViewById(R.id.tv_acc_name);
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        lvPaymentDetails = (ListView) view.findViewById(R.id.lv_payment_details);
        tvRTU = (TextView) view.findViewById(R.id.tv_rut);
        tvBank = (TextView) view.findViewById(R.id.tv_bank);
        tvType = (TextView) view.findViewById(R.id.tv_acc_type);
        tvAccount = (TextView) view.findViewById(R.id.tv_acc_number);
        tvTaskName = (TextView) view.findViewById(R.id.tv_task_name);
        tvDate = (TextView) view.findViewById(R.id.tv_date);

        final View footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.payment_detail_footer, null, false);
        tvBalance = (TextView) footerView.findViewById(R.id.tv_balance); // How you reference the textview in the footer
        lvPaymentDetails.addFooterView(footerView);
        ivBack.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Fragment fragment = new BankFragment();
                ((HomeActivity) mContext).loadFragment(fragment);
                break;
        }
    }

    @Override
    public void onBackPress() {
        Fragment fragment = new BankFragment();
        ((HomeActivity) mContext).loadFragment(fragment);
    }
}
