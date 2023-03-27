package com.example.group.util;

import android.support.v7.widget.RecyclerView;
import android.viewbinding.ViewBinding;

import io.reactivex.annotations.NonNull;

/**
 * @author test
 */
public class BaseViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {

    private final T mViewBinding;

    public BaseViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        mViewBinding = binding;
    }

    public T getViewBinding() {
        return mViewBinding;
    }
}