package com.example.group.livedata;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

/**
 * Created by test on 19-1-22.
 */
public class TestViewModel extends ViewModel {

    private final String mKey;
    private MutableLiveData<String> mNameEvent = new MutableLiveData<>();

    public MutableLiveData<String> getNameEvent() {
        return mNameEvent;
    }

    public TestViewModel(String key) {
        mKey = key;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private String mKey;

        public Factory(String key) {
            mKey = key;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TestViewModel(mKey);
        }
    }

    public String getKey() {
        return mKey;
    }
}
