package com.example.networkdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.networkdemo.databinding.DialogLocationBinding;

public class LocationDialog extends Dialog implements View.OnClickListener {

    private DialogLocationBinding mBinding;
    private OnDialogButtonClickListener mDialogButtonClickListener;

    public LocationDialog(@NonNull Context context) {
        super(context);
    }

    public void setDialogButtonClickListener(OnDialogButtonClickListener listener) {
        mDialogButtonClickListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_location, null, false);
        setContentView(mBinding.getRoot());
        setCanceledOnTouchOutside(false);

        mBinding.dialogCancel.setOnClickListener(this);
        mBinding.dialogAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                if (mDialogButtonClickListener != null) {
                    mDialogButtonClickListener.onCancelButtonClick();
                }
                this.dismiss();
                break;
            case R.id.dialog_accept:
                if (mDialogButtonClickListener != null) {
                    String currentLocation = mBinding.edDialogLocation.getText().toString();
                    if (!TextUtils.isEmpty(currentLocation)) {
                        if (mDialogButtonClickListener != null) {
                            mDialogButtonClickListener.onAcceptButtonClick(currentLocation);
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.location_error, Toast.LENGTH_SHORT).show();
                    }

                }
                this.dismiss();
                break;
        }
    }

    public interface OnDialogButtonClickListener {
        void onAcceptButtonClick(String location);
        void onCancelButtonClick();
    }
}
