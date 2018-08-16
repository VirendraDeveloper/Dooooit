package cl.activaresearch.android_app.Dooit.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.BankBean;
import cl.activaresearch.android_app.Dooit.models.AccountBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvCancel, tvBalance, tvName, tvRTU, tvBank, tvType, tvAccount,
            tvEditor, tvWithdraw;
    private String TAG = WithdrawActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvName = (TextView) findViewById(R.id.tv_acc_name);
        tvRTU = (TextView) findViewById(R.id.tv_rut);
        tvBank = (TextView) findViewById(R.id.tv_bank);
        tvType = (TextView) findViewById(R.id.tv_acc_type);
        tvAccount = (TextView) findViewById(R.id.tv_acc_number);
        tvEditor = (TextView) findViewById(R.id.tv_editor);
        tvWithdraw = (TextView) findViewById(R.id.tv_withdraw);
        tvEditor.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        String mBalance = getIntent().getStringExtra(Constants.BALANCE);
        if (!mBalance.equalsIgnoreCase("")) {
            tvBalance.setText("$"+mBalance);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
        showProgress();
        ApiHelper.getInstance().getBankAccount(token, new ApiCallback.AccountListener() {
            @Override
            public void onSuccess(final AccountBean result) {
                tvAccount.setText(result.getAccountNumber());
                tvName.setText(result.getAccountHolderName());
                tvRTU.setText(result.getAccountHolderRUT());
                tvType.setText(result.getAccountType());
                try {
                    if (result.getAccountBank() > 0) {
                        ApiHelper.getInstance().getBanks(token, new ApiCallback.BanksListener() {
                            @Override
                            public void onSuccess(List<BankBean> task) {
                                for (BankBean bank : task) {
                                    if (bank.getId() == result.getAccountBank()) {
                                        tvBank.setText(bank.getName());
                                        tvBank.setTextColor(getResources().getColor(R.color.colorBlack));
                                        tvWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tvWithdraw.setEnabled(true);
                                        break;
                                    }
                                }
                                dismissProgress();
                            }

                            @Override
                            public void onFailure(String error) {
                                dismissProgress();
                            }
                        });
                    } else {
                        dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissProgress();
                }

                Log.d(TAG, result + "");
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, error);
                dismissProgress();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_editor:
                Intent intent = new Intent(this, PaymentConfigurationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.tv_withdraw:
                withdrawDialog();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    private void withdrawDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        //alertDialog.setTitle(getString(R.string.sure_finish));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.sure_withdraw));

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.mipmap.ic_pref_logout);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getString(R.string.withdraw_fund), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String token = SharedPreferenceUtility.getInstance(WithdrawActivity.this).getString(Constants.TOKEN);
                ApiHelper.getInstance().withdrawFund(token, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("", "");
                        setMessageTitle(getString(R.string.withdraw_req));
                        setMessageDescription(getString(R.string.withdraw_time));
                        showMessageDialog();
                        setOnAcceptClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideMessageDialog();
                            }
                        });
                        setOnAcceptClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideMessageDialog();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                        Log.d("", "");
                    }
                });


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
