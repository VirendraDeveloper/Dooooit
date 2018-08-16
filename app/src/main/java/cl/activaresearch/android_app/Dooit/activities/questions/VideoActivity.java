package cl.activaresearch.android_app.Dooit.activities.questions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import java.io.File;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.activities.BaseActivity;
import cl.activaresearch.android_app.Dooit.models.SurveyBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.FilePathManager;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
import cl.activaresearch.android_app.Dooit.utils.Utility;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jun,2018
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvCancel, tvCapture, tvOk, tvQuestion;
    private SurveyBean surveyBean;
    private VideoView vvCaptured;
    private int REQUEST_VIDEO = 0, SELECT_FILE = 1;
    private String selectedVideoPath = "null", videoUrl;
    private StorageReference mStorageRef;
    private ImageView ivFile, ivPlay;
    private RelativeLayout rlVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvQuestion = (TextView) findViewById(R.id.tv_question_name);
        tvCapture = (TextView) findViewById(R.id.tv_update_video);
        rlVideo = (RelativeLayout) findViewById(R.id.rl_video);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivFile = (ImageView) findViewById(R.id.iv_file);
        vvCaptured = (VideoView) findViewById(R.id.vv_captured_video);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        Intent intent = getIntent();
        surveyBean = (SurveyBean) intent.getSerializableExtra(Constants.SURVEY);
        tvTitle.setText(getIntent().getStringExtra(Constants.TITLE));
        tvQuestion.setText(surveyBean.getQuestion());
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        tvCapture.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        rlVideo.setVisibility(View.GONE);
        ivFile.setVisibility(View.VISIBLE);
        vvCaptured.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlay.setVisibility(View.VISIBLE);
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (surveyBean != null) {
            tvTitle.setText(intent.getStringExtra(Constants.TITLE));
            tvQuestion.setText(surveyBean.getQuestion());
            if (surveyBean.getAnswer() != null) {
                Uri uriVideo = Uri.parse(surveyBean.getAnswer().getData());
                vvCaptured.setVideoURI(uriVideo);
                if (uriVideo != null) {
                    rlVideo.setVisibility(View.VISIBLE);
                    ivFile.setVisibility(View.GONE);
                } else {
                    rlVideo.setVisibility(View.GONE);
                    ivFile.setVisibility(View.VISIBLE);
                }
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
                updateVideo();
                break;
            case R.id.tv_update_video:
                selectVideo();
                break;
            case R.id.iv_play:
                vvCaptured.start();
                ivPlay.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Selecting image form Gallery or Camera
     */
    private void selectVideo() {
        final CharSequence[] items = {getString(R.string.record_video), getString(R.string.select_video_fr_galery),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(VideoActivity.this);
                if (items[item].equals(getString(R.string.record_video))) {
                    if (result)
                        videoIntent();
                } else if (items[item].equals(getString(R.string.select_video_fr_galery))) {
                    if (result)
                        galleryIntent();
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Fire intent for gallery
     */
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);
    }

    /**
     * Fire intent for camera
     */
    private void videoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uriVideo = data.getData();
                vvCaptured.setVideoURI(uriVideo);
                if (uriVideo != null) {
                    rlVideo.setVisibility(View.VISIBLE);
                    ivFile.setVisibility(View.GONE);
                } else {
                    rlVideo.setVisibility(View.GONE);
                    ivFile.setVisibility(View.VISIBLE);
                }
                // OI FILE Manager
                String filemanagerstring = uriVideo.getPath();
                // MEDIA GALLERY
                selectedVideoPath = FilePathManager.getVideoPath(this, uriVideo);
                Log.d("", "");
            } else if (requestCode == REQUEST_VIDEO) {
                Uri uriVideo = data.getData();
                // MEDIA GALLERY
                selectedVideoPath = FilePathManager.getVideoPath(this, uriVideo);
                vvCaptured.setVideoURI(uriVideo);
                if (uriVideo != null) {
                    rlVideo.setVisibility(View.VISIBLE);
                    ivFile.setVisibility(View.GONE);
                    tvOk.setEnabled(true);
                    tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    rlVideo.setVisibility(View.GONE);
                    ivFile.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void updateVideo() {
        Uri filePath = Uri.fromFile(new File(selectedVideoPath));
        //if there is a file to upload
        try {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.uploading));
            //progressDialog.show();
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            showProgress();
            StorageReference riversRef = mStorageRef.child("videos/" + ts + "_dooit.mp4");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            Uri uri = taskSnapshot.getDownloadUrl();
                            //and displaying a success toast
                            videoUrl = String.valueOf(uri);
                            // Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            // body.put("profilePictureURL", imageUrl);
                            setAnswerForQuestion(videoUrl);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            dismissProgress();
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
            dismissProgress();
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

}
