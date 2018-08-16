package cl.activaresearch.android_app.Dooit.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.RegionBean;
import cl.activaresearch.android_app.Dooit.models.UserBean;
import cl.activaresearch.android_app.Dooit.recievers.NetworkRecognizer;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiCallback;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiHelper;
import cl.activaresearch.android_app.Dooit.utils.Constants;
import cl.activaresearch.android_app.Dooit.utils.FilePathManager;
import cl.activaresearch.android_app.Dooit.utils.SharedPreferenceUtility;
import cl.activaresearch.android_app.Dooit.utils.Utility;
import cl.activaresearch.android_app.Dooit.utils.Validation;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 05 Jul,2018
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvOk, tvCancel, tvEmail;
    private EditText edtFName, edtLName, edtRegion, edtAddress, edtBirthday, edtSex, edtCity;
    private String strFName, strLName, dateDOB, strEmail, strAddress, strSex;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = myCalendar.getTime();
            dateDOB = Validation.getDateFormat(date);
            edtBirthday.setText(Validation.getMonthDate(date));
        }

    };
    private CircleImageView ivProfile;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosesTask, imageUrl = "null";
    private StorageReference mStorageRef;
    private int regionId = -1, cityId = -1, regionPosition = 0;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        edtCity.setOnClickListener(this);
        edtRegion.setOnClickListener(this);
        edtSex.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        showProgress();
        token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
        ApiHelper.getInstance().getRegionAndCity(token, new ApiCallback.RegionsListener() {
            @Override
            public void onSuccess(List<RegionBean> regionBeanList) {
                ApiHelper.getInstance().getUserProfileDetails(token, new ApiCallback.UserListener() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        updateDetails(userBean);
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
                dismissProgress();
            }
        });
    }

    private void updateDetails(UserBean userBean) {
        if (userBean.getNombres() != null) {
            edtFName.setText(userBean.getNombres());
        }
        if (userBean.getApellidos() != null) {
            edtLName.setText(userBean.getApellidos());
        }
        if (userBean.getEmail() != null) {
            tvEmail.setText(userBean.getEmail());
            strEmail = userBean.getEmail();
        }
        if (userBean.getFecha_nac() != null) {
            dateDOB = userBean.getFecha_nac();
            edtBirthday.setText(Validation.getMonthDate(userBean.getFecha_nac()));
        }
        if (userBean.getSexo() != null) {
            edtSex.setText(userBean.getSexo());
        }
        if (userBean.getDireccion() != null) {
            edtAddress.setText(userBean.getDireccion());
        }
        if (userBean.getRegionAsString() != null) {
            edtRegion.setText(userBean.getRegionAsString());
            regionId = userBean.getRegion();
        }
        if (userBean.getCityAsString() != null) {
            edtCity.setText(userBean.getCityAsString());
            cityId = userBean.getComuna();
        }

        if (userBean.getFoto_perfil() != null) {
            imageUrl = userBean.getFoto_perfil();
            Glide.with(ProfileActivity.this)
                    .load(imageUrl)
                    .into(ivProfile);
        } else {
            ivProfile.setImageResource(R.drawable.profile_bg);
        }
    }

    private void initUI() {
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        edtFName = (EditText) findViewById(R.id.edt_name);
        edtLName = (EditText) findViewById(R.id.edt_surname);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtBirthday = (EditText) findViewById(R.id.edt_birth_date);
        edtCity = (EditText) findViewById(R.id.edt_city);
        edtSex = (EditText) findViewById(R.id.edt_sex);
        edtRegion = (EditText) findViewById(R.id.edt_region);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        ivProfile = (CircleImageView) findViewById(R.id.iv_profile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_birth_date:
                new DatePickerDialog(ProfileActivity.this, R.style.CalenderTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.edt_city: {
                ApiHelper.getInstance().getRegionAndCity(SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN), new ApiCallback.RegionsListener() {
                    @Override
                    public void onSuccess(List<RegionBean> regionBeanList) {
                        if (regionPosition > 0) {
                            List<RegionBean.CitiesBean> citiesBeans = regionBeanList.get(regionPosition).getCities();
                            String[] strings = new String[citiesBeans.size()];
                            for (int i = 0; i < citiesBeans.size(); i++) {
                                RegionBean.CitiesBean regionBean = citiesBeans.get(i);
                                strings[i] = regionBean.getCityName();
                            }
                            dialogPicker(strings, 2);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                    }
                });
            }
            break;
            case R.id.iv_profile:
                selectImage();
                break;
            case R.id.edt_sex:
                dialogPicker(new String[]{getString(R.string.male), getString(R.string.female)}, 0);
                break;
            case R.id.edt_region: {
                ApiHelper.getInstance().getRegionAndCity(SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN), new ApiCallback.RegionsListener() {
                    @Override
                    public void onSuccess(List<RegionBean> regionBeanList) {
                        String[] strings = new String[regionBeanList.size()];
                        for (int i = 0; i < regionBeanList.size(); i++) {
                            RegionBean regionBean = regionBeanList.get(i);
                            strings[i] = regionBean.getRegionName();
                        }
                        dialogPicker(strings, 1);
                    }

                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                    }
                });
            }
            break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                strSex = edtSex.getText().toString().trim();
                strFName = edtFName.getText().toString().trim();
                strLName = edtLName.getText().toString().trim();
                strAddress = edtAddress.getText().toString().trim();
                String token = SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN);
                if (strFName.equalsIgnoreCase("")) {
                    showToast(getString(R.string.emp_first_name));
                } else if (regionId < 0) {
                    showToast(getString(R.string.emp_region));
                } else if (!NetworkRecognizer.isNetworkAvailable(this)) {
                    showNetwork();
                } else {
                    HashMap<String, String> body = new HashMap<>();
                    body.put("firstName", strFName);
                    body.put("lastName", strLName);
                    body.put("email", strEmail);
                    body.put("gender", strSex);
                    body.put("address", strAddress);
                    body.put("region", regionId + "");
                    body.put("city", cityId + "");
                    body.put("birthdate", dateDOB);
                    if (imageUrl.contains("http") || imageUrl.equalsIgnoreCase("null")) {
                        body.put("profilePictureURL", imageUrl);
                        showProgress();
                        Log.d("", "");
                        ApiHelper.getInstance().updateUserProfileDetails(body, token, new ApiCallback.Listener() {
                            @Override
                            public void onSuccess(String result) {
                                dismissProgress();
                                showToast(getString(R.string.update_success));
                                finish();

                            }

                            @Override
                            public void onFailure(String error) {
                                dismissProgress();
                                showToast(getString(R.string.failure));

                            }
                        });
                    } else {
                        Log.d(",", "");
                        showProgress();
                        updateProfileImage(body, token);
                    }
                }
                break;
        }
    }

    /**
     * Selecting image form Gallery or Camera
     */
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose),
                getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);
                if (items[item].equals(getString(R.string.take_photo))) {
                    userChoosesTask = getString(R.string.take_photo);
                    if (result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.choose))) {
                    userChoosesTask = getString(R.string.choose);
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
        ivProfile.setImageBitmap(thumbnail);
    }

    private void updateProfileImage(final HashMap<String, String> body, final String token) {
        Uri filePath = Uri.fromFile(new File(imageUrl));
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.uploading));
            //progressDialog.show();
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

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
                            body.put("profilePictureURL", imageUrl);
                            ApiHelper.getInstance().updateUserProfileDetails(body, token, new ApiCallback.Listener() {
                                @Override
                                public void onSuccess(String result) {
                                    dismissProgress();
                                    showToast(getString(R.string.update_success));
                                }

                                @Override
                                public void onFailure(String error) {
                                    dismissProgress();
                                    showToast(getString(R.string.failure));

                                }
                            });


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
        } else {
            ApiHelper.getInstance().updateUserProfileDetails(body, token, new ApiCallback.Listener() {
                @Override
                public void onSuccess(String result) {
                    dismissProgress();
                    showToast(getString(R.string.update_success));
                }

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(getString(R.string.failure));

                }
            });
        }
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
        ivProfile.setImageURI(Uri.parse(imageUrl));
    }

    private void dialogPicker(final String[] arrayString, final int type) {
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
                        int val = picker.getValue();
                        updatePickerValue(arrayString, type, val);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updatePickerValue(String[] arrayString, int type, int position) {
        switch (type) {
            case 0:
                edtSex.setText(arrayString[position] + "");
                break;
            case 1:
                getRegionId(arrayString[position]);
                break;
            case 2:
                getCityId(arrayString[position]);
                break;
        }
    }

    private void getCityId(final String city) {
        ApiHelper.getInstance().getRegionAndCity(SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN), new ApiCallback.RegionsListener() {
            @Override
            public void onSuccess(List<RegionBean> regionBeanList) {
                int cityId1 = 0;
                for (RegionBean.CitiesBean citiesBean : regionBeanList.get(regionPosition).getCities()) {
                    if (citiesBean.getCityName().equalsIgnoreCase(city)) {
                        cityId1 = citiesBean.getCityId();
                        edtCity.setText(citiesBean.getCityName());
                        break;
                    }
                }
                if (cityId1 != 0) {
                    cityId = cityId1;
                } else {
                    cityId = regionBeanList.get(regionPosition).getCities().get(0).getCityId();
                    edtCity.setText(regionBeanList.get(regionPosition).getCities().get(0).getCityName());
                }
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });

    }

    private void getRegionId(final String region) {
        ApiHelper.getInstance().getRegionAndCity(SharedPreferenceUtility.getInstance(this).getString(Constants.TOKEN), new ApiCallback.RegionsListener() {
            @Override
            public void onSuccess(List<RegionBean> regionBeanList) {
                for (int i = 0; i < regionBeanList.size(); i++) {
                    RegionBean regionBean = regionBeanList.get(i);
                    if (regionBean.getRegionName().equalsIgnoreCase(region)) {
                        regionId = regionBean.getRegionId();
                        regionPosition = i;
                        edtRegion.setText(regionBean.getRegionName());
                        break;
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

}
