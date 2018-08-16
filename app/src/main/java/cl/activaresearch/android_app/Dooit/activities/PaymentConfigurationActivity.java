package cl.activaresearch.android_app.Dooit.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.AccountBean;
import cl.activaresearch.android_app.Dooit.models.BankBean;
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
public class PaymentConfigurationActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private EditText edtAccNumber, edtName, edtRUT;
    private TextView tvAccType, tvCancel, tvBank, tvOk;
    private String strAccNumber, strAccType, strRUT, strName, strBank;
    private String TAG = PaymentConfigurationActivity.class.getName();
    private String token;
    private List<BankBean> banks = new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_configuration);
        initUI();
        token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);

        showProgress();
        ApiHelper.getInstance().getBanks(token, new ApiCallback.BanksListener() {
            @Override
            public void onSuccess(List<BankBean> result) {
                banks = result;
                ApiHelper.getInstance().getBankAccount(token, new ApiCallback.AccountListener() {
                    @Override
                    public void onSuccess(AccountBean account) {
                        edtAccNumber.setText(account.getAccountNumber());
                        edtName.setText(account.getAccountHolderName());
                        edtRUT.setText(account.getAccountHolderRUT());
                        tvAccType.setText(account.getAccountType());
                        tvAccType.setTextColor(getResources().getColor(R.color.colorBlack));
                        for (BankBean bank : banks) {
                            if (bank.getId() == account.getAccountBank()) {
                                tvBank.setText(bank.getName());
                                tvBank.setTextColor(getResources().getColor(R.color.colorBlack));
                                break;
                            }
                        }
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d(TAG, error);
                        dismissProgress();
                        showToast(error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                Log.d(TAG, error);
                showToast(error);
            }
        });
    }

    private void dialogAccountTypePicker(final String[] arrayString) {
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(arrayString.length - 1);
        picker.setDisplayedValues(arrayString);
        FrameLayout layout = new FrameLayout(this);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = picker.getValue();
                        tvAccType.setText(arrayString[position] + "");
                        tvAccType.setTextColor(getResources().getColor(R.color.colorBlack));
                        isOkEnable();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void dialogBankPicker(final String[] arrayString) {
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(arrayString.length - 1);
        picker.setDisplayedValues(arrayString);
        FrameLayout layout = new FrameLayout(this);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = picker.getValue();
                        tvBank.setText(arrayString[position] + "");
                        tvBank.setTextColor(getResources().getColor(R.color.colorBlack));
                        isOkEnable();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void initUI() {
        edtAccNumber = (EditText) findViewById(R.id.edt_acc_number);
        tvAccType = (TextView) findViewById(R.id.tv_acc_type);
        edtName = (EditText) findViewById(R.id.edt_acc_name);
        tvBank = (TextView) findViewById(R.id.tv_bank);
        edtRUT = (EditText) findViewById(R.id.edt_rut);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        edtAccNumber.addTextChangedListener(this);
        tvAccType.setOnClickListener(this);
        edtName.addTextChangedListener(this);
        edtRUT.addTextChangedListener(this);
        tvCancel.setOnClickListener(this);
        tvBank.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        isOkEnable();
    }

    private void isOkEnable() {
        strAccType = tvAccType.getText().toString().trim();
        strRUT = edtRUT.getText().toString().trim();
        strBank = tvBank.getText().toString().trim();
        strName = edtName.getText().toString().trim();
        strAccNumber = edtAccNumber.getText().toString().trim();
        if (!strAccType.equalsIgnoreCase("") && !strRUT.equalsIgnoreCase("") &&
                !strBank.equalsIgnoreCase("") && !strName.equalsIgnoreCase("")
                && !strAccNumber.equalsIgnoreCase("")) {
            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
            tvOk.setEnabled(true);
        } else {
            tvOk.setTextColor(getResources().getColor(R.color.colorLight));
            tvOk.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("accountHolderName", edtName.getText().toString().trim());
                jsonObject.addProperty("accountHolderRUT", edtRUT.getText().toString().trim());
                jsonObject.addProperty("accountNumber", edtAccNumber.getText().toString().trim());
                jsonObject.addProperty("accountBank", banks.get(position).getId());
                jsonObject.addProperty("accountType", tvAccType.getText().toString().trim());
                showProgress();
                ApiHelper.getInstance().setBankAccount(token, jsonObject, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        dismissProgress();
                        finish();

                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d(TAG, error);
                        dismissProgress();
                        showToast(error);
                    }
                });
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_acc_type:
                String[] type = {"Cuenta Corriente", "Cuenta Vista", "Cuenta de Ahorro"};
                dialogAccountTypePicker(type);
                break;
            case R.id.tv_bank:
                if (banks.size() >= 0) {
                    Log.d(TAG, banks.size() + "");
                    String[] bankName = new String[banks.size()];
                    for (int i = 0; i < banks.size(); i++) {
                        BankBean bank = banks.get(i);
                        bankName[i] = bank.getName();
                    }
                    dialogBankPicker(bankName);
                } else {
                    showProgress();
                    ApiHelper.getInstance().getBanks(token, new ApiCallback.BanksListener() {
                        @Override
                        public void onSuccess(List<BankBean> result) {
                            dismissProgress();
                            banks = result;
                            Log.d(TAG, result.size() + "");
                            String[] bankName = new String[result.size()];
                            for (int i = 0; i < result.size(); i++) {
                                BankBean bank = result.get(i);
                                bankName[i] = bank.getName();
                            }
                            dialogBankPicker(bankName);

                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            Log.d(TAG, error);
                            showToast(error);
                        }
                    });
                }
                break;

        }
    }
}
