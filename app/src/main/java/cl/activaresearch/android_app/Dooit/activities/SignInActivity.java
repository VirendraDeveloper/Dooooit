package cl.activaresearch.android_app.Dooit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
import cl.activaresearch.android_app.Dooit.utils.Validation;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText edtEmail, edtPassword;
    private TextView tvRecover, tvLogin;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();

    }

    private void initUI() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvRecover = (TextView) findViewById(R.id.tv_recover);
        tvLogin.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvRecover.setOnClickListener(this);
        edtEmail.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String strEmail = edtEmail.getText().toString().trim();
                String strPassword = edtPassword.getText().toString().trim();
                if (strEmail.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_email));
                } else if (!Validation.isEmailValid(strEmail)) {
                    showToast(getString(R.string.invalid_email));
                } else if (strPassword.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_pass));
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    showNetwork();
                } else {
                    loginProcess(strEmail, strPassword);
                }
                break;
            case R.id.tv_recover:
                Intent intent = new Intent(this, ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }

    private void loginProcess(String strEmail, String strPassword) {
        showProgress();
        HashMap<String, String> body = new HashMap<>();
        body.put("email", strEmail);
        body.put("password", strPassword);
        ApiHelper.getInstance().userLogin(body, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
                SharedPreferenceUtility.getInstance(SignInActivity.this).putString(Constants.TOKEN, "login " + result);
                SharedPreferenceUtility.getInstance(SignInActivity.this).putBoolean(Constants.IS_LOGIN, true);
                Intent intent = new Intent(SignInActivity.this, PermissionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                dismissProgress();
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
                dismissProgress();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (!strEmail.equalsIgnoreCase("") &&
                !strPassword.equalsIgnoreCase("")) {
            tvLogin.setEnabled(true);
            tvLogin.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            tvLogin.setEnabled(false);
            tvLogin.setTextColor(getResources().getColor(R.color.colorLight));

        }
    }
}
