package cl.activaresearch.android_app.Dooit.activities.questions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

public class PhotoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvCancel, tvCapture, tvOk, tvQuestion;
    private SurveyBean surveyBean;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String imageUrl = "null";
    private StorageReference mStorageRef;
    private ImageView ivCapturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
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
                Glide.with(PhotoActivity.this)
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
                updateImage();
                break;
            case R.id.tv_update_photo:
                selectImage();
                break;
        }
    }

    /**
     * Selecting image form Gallery or Camera
     */
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.capture_photo), getString(R.string.select_photo),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PhotoActivity.this);
                if (items[item].equals(getString(R.string.capture_photo))) {
                    if (result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.select_photo))) {
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
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);
    }

    /**
     * Fire intent for camera
     */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    /**
     * Retrieve image from captured
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUrl = String.valueOf(destination);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ivCapturedImage.getLayoutParams().height = width;
        ivCapturedImage.setImageBitmap(thumbnail);
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {
            tvOk.setEnabled(true);
            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void updateImage() {
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ivCapturedImage.getLayoutParams().height = width;
        ivCapturedImage.setImageURI(Uri.parse(imageUrl));
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {
            tvOk.setEnabled(true);
            tvOk.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

}
