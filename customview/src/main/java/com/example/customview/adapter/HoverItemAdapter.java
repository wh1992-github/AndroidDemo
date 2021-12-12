package com.example.customview.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.customview.R;
import com.example.customview.bean.UserBean;

import java.util.List;

public class HoverItemAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    public HoverItemAdapter(@Nullable List<UserBean> data) {
        super(R.layout.adapter_item_hover_user, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.user_name_tv, item.getUserName());
    }

}
