package cl.activaresearch.android_app.Dooit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
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
public class SignUpActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText edtEmail, edtFName, edtLName, edtPassword;
    private TextView tvFacebook, tvSignUp, tvLogin;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String TAG = SignUpActivity.class.getName();
    private String strEmail, strPassword, strFName, strLName;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final String accessToken = loginResult.getAccessToken().getToken();
                Log.d("", "");
                //EAAcaCvHdxLYBAAPtbV7SbnPW7uBE2TNFlTGVuNEb8ZCEbLnih6fswuFpZAgZAoEzL3RZBlblnbGv7gpYIOQ0D9uOGPhqELaAueaZA9cx8gwPBZC8VKcLX5KQuSUZAL5jeCLjWFDtgpZBstolucZCKPxcFRUF5UBu9eKInbquXDEboANsuhzNPmbkGZCAlmSBaqdukcZCDac0dOZCnNypcC0ZAkvhJuDBN9Kd7kMUZD
                HashMap<String, String> body = new HashMap<>();
                body.put("token", accessToken);
                showProgress();
                ApiHelper.getInstance().userFacebookLogin(body, new ApiCallback.Listener() {
                    @Override
                    public void onSuccess(String id) {
                        SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(Constants.TOKEN, "facebook " + accessToken);
                        SharedPreferenceUtility.getInstance(SignUpActivity.this).putBoolean(Constants.IS_LOGIN, true);
                        Intent intent = new Intent(SignUpActivity.this, PermissionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dismissProgress();
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                        LoginManager.getInstance().logOut();
                    }
                });
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("", "");
            }
        });
       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "cl.activaresearch.android_app.Dooit",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", hash);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUI() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtFName = (EditText) findViewById(R.id.edt_f_name);
        edtLName = (EditText) findViewById(R.id.edt_l_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvFacebook = (TextView) findViewById(R.id.tv_facebook);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvFacebook.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        edtFName.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
        edtLName.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_facebook:
                loginButton.performClick();
                break;
            case R.id.tv_sign_up:
                if (strEmail.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_email));
                } else if (!Validation.isEmailValid(strEmail)) {
                    showToast(getString(R.string.invalid_email));
                } else if (strPassword.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_pass));
                } else if (strFName.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_first_name));
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    showNetwork();
                } else {
                    HashMap<String, String> body = new HashMap<>();
                    body.put("nombres", strFName);
                    body.put("apellidos", strLName);
                    body.put("email", strEmail);
                    body.put("password", strPassword);
                    showProgress();
                    ApiHelper.getInstance().userSignUp(body, new ApiCallback.Listener() {
                        @Override
                        public void onSuccess(String token) {
                            SharedPreferenceUtility.getInstance(SignUpActivity.this).putString(Constants.TOKEN, "login " + token);
                            SharedPreferenceUtility.getInstance(SignUpActivity.this).putBoolean(Constants.IS_LOGIN, true);
                            Intent intent = new Intent(SignUpActivity.this, PermissionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            dismissProgress();
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();
        strFName = edtFName.getText().toString().trim();
        strLName = edtLName.getText().toString().trim();
        if (!strEmail.equalsIgnoreCase("") && !strFName.equalsIgnoreCase("") &&
                !strLName.equalsIgnoreCase("") && !strPassword.equalsIgnoreCase("")) {
            tvSignUp.setTextColor(getResources().getColor(R.color.colorWhite));
            tvSignUp.setEnabled(true);
        } else {
            tvSignUp.setTextColor(getResources().getColor(R.color.colorLight));
            tvSignUp.setEnabled(false);
        }
    }
}
