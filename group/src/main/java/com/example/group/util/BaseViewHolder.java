package com.example.group.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import io.reactivex.annotations.NonNull;

/**
 * @author test
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private final T mViewBinding;

    public BaseViewHolder(@NonNull T binding) {
        super(getRootView(binding));
        mViewBinding = binding;
    }

    public T getViewBinding() {
        return mViewBinding;
    }

    private static View getRootView(Object binding) {
        try {
            return (View) binding.getClass().getMethod("getRoot").invoke(binding);
        } catch (Exception e) {
            throw new IllegalArgumentException("Binding must expose getRoot()", e);
        }
    }
}
