package cl.activaresearch.android_app.Dooit.activities.questions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.BaseActivity;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.FilePathManager;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jun,2018
 */

public class AudioActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvCancel, tvCapture, tvOk, tvQuestion;
    private SurveyBean surveyBean;
    private int REQUEST_AUDIO = 0, SELECT_FILE = 1;
    private String imageUrl = "null";
    private StorageReference mStorageRef;
    private ImageView ivCapturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvQuestion = (TextView) findViewById(R.id.tv_question_name);
        tvCapture = (TextView) findViewById(R.id.tv_update_photo);
        ivCapturedImage = (ImageView) findViewById(R.id.iv_captured_image);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        Intent intent = getIntent();
        surveyBean = (SurveyBean) intent.getSerializableExtra(Constants.SURVEY);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        tvCapture.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (surveyBean != null) {
            tvTitle.setText(intent.getStringExtra(Constants.TITLE));
            tvQuestion.setText(surveyBean.getQuestion());
            if (surveyBean.getAnswer() != null) {
                Glide.with(AudioActivity.this)
                        .load(surveyBean.getAnswer().getData())
                        .into(ivCapturedImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                uploadAudio();
                break;
            case R.id.tv_update_photo:
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, REQUEST_AUDIO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_AUDIO)
                onSelectFromGalleryResult(data);
        }
    }
    private void uploadAudio() {
        Uri filePath = Uri.fromFile(new File(imageUrl));
        //if there is a file to upload
        try {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.uploading));
            //progressDialog.show();
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            showProgress();
            StorageReference riversRef = mStorageRef.child("images/" + ts + "_dooit.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            Uri uri = taskSnapshot.getDownloadUrl();
                            //and displaying a success toast
                            imageUrl = String.valueOf(uri);
                            // Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            // body.put("profilePictureURL", imageUrl);
                            setAnswerForQuestion(imageUrl);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            showProgress();
                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAnswerForQuestion(String imageUrl) {
        String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
        JsonObject body = new JsonObject();
        body.addProperty("answer", imageUrl);
        ApiHelper.getInstance().ansSurveyQuestion(token, surveyBean.getTaskId(), surveyBean.getId(), body, new ApiCallback.Listener() {
            @Override
            public void onSuccess(String result) {
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

    /**
     * Retrieved path to selected image from Gallery
     *
     * @param data
     */
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri selectedImageUri = data.getData();
        //MEDIA GALLERY
        imageUrl = FilePathManager.getImagePath(this, selectedImageUri);
        ivCapturedImage.setImageURI(Uri.parse(imageUrl));
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {
            tvOk.setEnabled(true);
            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

}
