package cl.activaresearch.android_app.Dooit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiClient;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiInterface;
import cl.activaresearch.android_app.Dooit.utils.Validation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class ForgotActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtEmail;
    private TextView tvRecover;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvRecover = (TextView) findViewById(R.id.tv_recover);
        tvRecover.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recover:
                String strEmail = edtEmail.getText().toString().trim();
                if (strEmail.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_email));
                } else if (!Validation.isEmailValid(strEmail)) {
                    showToast(getString(R.string.invalid_email));
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    showNetwork();
                } else {
                    forgotProcess(strEmail);
                    //intent = new Intent(this, HomeActivity.class);
                    //startActivity(intent);
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void forgotProcess(String strEmail) {
        showProgress();
        HashMap<String, String> body = new HashMap<>();
        body.put("email", strEmail);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiService.forgot(body);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        JSONObject jsonObject1 = new JSONObject(body);
                        String message = jsonObject1.getString("message");
                        boolean success = jsonObject1.getBoolean("success");
                        showToast(message);
                        dismissProgress();
                        Log.d("", "");

                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgress();
                        Log.d("", "");
                    }
                } else {
                    dismissProgress();
                    Log.d("", "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                dismissProgress();
                Log.d("", "");
            }
        });
    }

}
