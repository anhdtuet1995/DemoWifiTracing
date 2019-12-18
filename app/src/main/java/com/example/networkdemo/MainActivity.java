package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.networkdemo.databinding.ActivityMainBinding;
import com.example.networkdemo.model.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DataAdapter.OnActionClickListener {

    private final int REQUEST_WRITE_STORAGE_PERMISSION = 207;
    private final String FILE_NAME = "wifi-log.txt";
    private final String TAG = "WifiTracing";
    private List<Data> fakeData;
    private ActivityMainBinding mBinding;
    private DataAdapter mDataAdapter;
    private Data mInteractingData;
    private LocationDialog mLocationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //TODO for testing
        fakeData = new ArrayList<>();
        fakeData.add(new Data(1576584886 , "00-FF-42-43-75-16", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-43-35-20", Constants.OK_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-13-75-21", Constants.OK_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-93-75-30", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-93-75-41", Constants.OK_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-22-75-50", Constants.OK_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-44-75-39", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));
        fakeData.add(new Data(1576584886 , "00-FF-42-11-75-87", Constants.NG_STATUS));

        mDataAdapter = new DataAdapter(this);

        mBinding.recyclerViewData.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mBinding.recyclerViewData.setAdapter(mDataAdapter);
        mDataAdapter.setActionClickListener(this);
        mBinding.btnFooterStart.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //TODO show dialog to guide user open setting
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_footer_start:
                //TODO for testing
                Collections.shuffle(fakeData);
                mDataAdapter.setNewData(fakeData);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mInteractingData != null) {
                        showLocationDialog(mInteractingData);
                    }
                } else {
                    this.finish();
                }
                return;
            }
        }
    }

    @Override
    public void onAddClick(Data data) {
        mInteractingData = data;
        if (checkPermission()) {
            showLocationDialog(data);
        }
    }

    private void showLocationDialog(final Data data) {
        mLocationDialog = new LocationDialog(this);
        mLocationDialog.setDialogButtonClickListener(new LocationDialog.OnDialogButtonClickListener() {
            @Override
            public void onAcceptButtonClick(String location) {
                saveLog(data, location);
            }

            @Override
            public void onCancelButtonClick() {

            }
        });
        mLocationDialog.show();
    }

    private void saveLog(Data data, String location) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            if (!file.exists()) {
                boolean isCreated = file.createNewFile();
                Log.d(TAG, "File is created = " + isCreated);
                if (!isCreated) {
                    Toast.makeText(this, R.string.write_location_error, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            String format = "Time: %d    Mac Addr: %s    Location: %s\n";
            writer.write(String.format(Locale.getDefault(), format, data.getTime(), data.getMac(), location));
            Toast.makeText(this, String.format(getResources().getString(R.string.write_location_success), FILE_NAME), Toast.LENGTH_SHORT).show();
            writer.close();

        } catch (IOException e) {
            Toast.makeText(this, R.string.write_location_error, Toast.LENGTH_SHORT).show();
            Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    @Override
    protected void onDestroy() {
        if (mLocationDialog != null && mLocationDialog.isShowing()) {
            mLocationDialog.dismiss();
        }
        super.onDestroy();
    }
}
