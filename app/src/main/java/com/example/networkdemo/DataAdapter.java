package com.example.networkdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networkdemo.databinding.ItemContentBinding;
import com.example.networkdemo.databinding.ItemHeaderBinding;
import com.example.networkdemo.model.Data;
import com.example.networkdemo.model.Header;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER_ITEM = 0;
    private final int CONTENT_ITEM = 1;

    private List<Object> mDataList = new ArrayList<>();
    private OnActionClickListener mActionClickListener;
    private Context mContext;

    public DataAdapter(Context context) {
        this.mContext = context;
        this.mDataList.add(new Header());
    }

    public void setActionClickListener(OnActionClickListener mActionClickListener) {
        this.mActionClickListener = mActionClickListener;
    }

    public void setNewData(List<Data> dataList) {
        if (dataList != null) {
            mDataList.clear();
            mDataList.add(new Header());
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void addListData(List<Data> dataList) {
        if (dataList != null) {
            mDataList.addAll(dataList);
            notifyItemRangeChanged(mDataList.size(), dataList.size());
        }
    }

    public void addData(Data data) {
        if (data != null) {
            mDataList.add(data);
            notifyItemInserted(mDataList.size());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_ITEM) {
            ItemHeaderBinding binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new HeaderViewHolder(binding);
        } else {
            ItemContentBinding binding = ItemContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ContentViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_ITEM:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.bind();
                break;
            case CONTENT_ITEM:
                Data data = (Data) mDataList.get(position);
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.bind(data);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position) instanceof Header) {
            return HEADER_ITEM;
        } else {
            return CONTENT_ITEM;
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        ItemHeaderBinding mBinding;

        public HeaderViewHolder(@NonNull ItemHeaderBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind() {
            mBinding.executePendingBindings();
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        ItemContentBinding mBinding;

        public ContentViewHolder(@NonNull ItemContentBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        public void bind(final Data data) {

            mBinding.time.setText(data.getTime() + "");
            mBinding.mac.setText(data.getMac());

            int currentStatusId = data.getStatus();
            String status;
            if (currentStatusId == Constants.NG_STATUS) {
                status = mContext.getResources().getString(R.string.ng);
                mBinding.setShowAction(true);
                mBinding.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActionClickListener != null) {
                            mActionClickListener.onAddClick(data);
                        }
                    }
                });
            } else {
                mBinding.setShowAction(false);
                status = mContext.getResources().getString(R.string.ok);
            }
            mBinding.status.setText(status);

            mBinding.executePendingBindings();
        }
    }

    public interface OnActionClickListener {
        void onAddClick(Data data);
    }

}
