package com.example.jidelnicek;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DatabaseController db;
    private FragmentManager fragmentManager;
    private boolean writePermissionGranted;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null){
                    writePermissionGranted = Objects.requireNonNull(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                }
            }
        });

        updateOrRequestPermission();

        db = DatabaseController.getInstance(getApplicationContext());
        fragmentManager = getSupportFragmentManager();

        IntroductionFragment introductionFragment = IntroductionFragment.newInstance(db);
        fragmentManager.beginTransaction()
                .add(R.id.fcv_listFragment, introductionFragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    private void updateOrRequestPermission(){
        boolean hasWritePermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        writePermissionGranted = hasWritePermission || minSdk29;

        List<String> permissionRequest = new ArrayList<>();

        if(!writePermissionGranted){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissionRequest.isEmpty()){
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    public boolean saveImageToStorage(String img_name, Bitmap bmp){
        Uri imageCollection = null;
        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else{
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, img_name + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri imageUri = resolver.insert(imageCollection, contentValues);

        try{
            OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
            return true;
        }
        catch (Exception e){
            Toast.makeText(this, "Chyba! Obrázek nebyl uložen. \n" + e, Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}