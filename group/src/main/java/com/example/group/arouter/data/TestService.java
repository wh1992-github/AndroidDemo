package com.example.group.arouter.data;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * create by test on 2/7/2018.
 */
@Route(path = ARouterConstants.SERVICE_HELLO, name = "test service")
public class TestService implements IProvider {

    public String sayYes(String name) {
        return "yes " + name;
    }

    public String sayNo(String name) {
        return "no " + name;
    }

    @Override
    public void init(Context context) {

    }
}

