package com.example.prasant.demoapp;

import android.Manifest;
import android.app.usage.ExternalStorageStats;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.Permission;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PERMISSION_CODE=200;
    Button btnCheckPermission,btnRequestPermission;
    //Button btnDexSinglePermission,btnDexMultiplePermission;

    @BindView(R.id.txtDexter)
    TextView txtDexter;

    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnCheckPermission=findViewById(R.id.btnCheckPerm);
        btnRequestPermission=findViewById(R.id.btnRequestPerm);
        btnCheckPermission.setOnClickListener(this);
        btnRequestPermission.setOnClickListener(this);

        /*btnDexSinglePermission=findViewById(R.id.btnDexSinglePerm);
        btnDexMultiplePermission=findViewById(R.id.btnDexMultiplePerm);
        btnDexSinglePermission.setOnClickListener(this);
        btnDexMultiplePermission.setOnClickListener(this);
*/
    }

    @Override
    public void onClick(View view) {
        this.view=view;
        int id=view.getId();
        switch (id){
            case R.id.btnCheckPerm:{
                if(checkPermission()){
                    Snackbar.make(view,"Permission already granted",Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
                else {
                    Snackbar.make(view,"Please request permission",Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btnRequestPerm:{
                if (!checkPermission()){
                    requestPermission();
                }
                else {
                    Snackbar.make(view,"Permission already granted",Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
            /*case R.id.btnDexSinglePerm:{

                break;
            }
            case R.id.btnDexMultiplePerm:{
                break;
            }*/
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA},PERMISSION_CODE);
    }

    public boolean checkPermission(){

        int result= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int result1=ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return result== PackageManager.PERMISSION_GRANTED&&result1==PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0){
                    boolean locationAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted&&cameraAccepted){
                        Snackbar.make(view,"Permission granted, now you can access both location and camera",Snackbar.LENGTH_SHORT).show();

                    }
                    else {
                        Snackbar.make(view,"Permission denied, now you can't access both location and camera",Snackbar.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                                showMesageOkCancel("You need to allow access to both of permissions",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                            Manifest.permission.CAMERA},PERMISSION_CODE);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }

                }

                break;
            }
        }
    }

    private void showMesageOkCancel(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setTitle("Need Permissions")
                .setPositiveButton("Ok",onClickListener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    @OnClick(R.id.btnDexSinglePerm)
    public void onDexSinglePermButtonClick(final View view){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        //Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.co.in"));
                        //startActivityForResult(intent,103);
                        //Snackbar.make(view,"Dexter's has selected ACCESS COARSE LOCATION permission",Snackbar.LENGTH_LONG).show();
                        Intent intent=new Intent(MainActivity.this,SliderActivity.class);
                        startActivity(intent);
                        //finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()) {
                            Snackbar.make(view, "Permission Denmied for Access_Coarse_Loaction", Snackbar.LENGTH_LONG).show();
                            showSettingDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void showSettingDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                .setTitle("Need Permissions")
                .setMessage("This app needs permission to use this feature. You can grant them in app settings.");
                builder.setPositiveButton("Goto Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        openSetting();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


    }

    private void openSetting() {
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,101);
    }

    @OnClick(R.id.btnDexMultiplePerm)
    public void onDexMultiplePermButtonClick(final View view){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Snackbar.make(view,"All permission granted",Snackbar.LENGTH_LONG).show();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()){
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Snackbar.make(view,"Error Occureence!",Snackbar.LENGTH_LONG).show();
            }
        }).onSameThread().check();
        txtDexter.setText("Dexter's Multiple Permission select");
    }
}
