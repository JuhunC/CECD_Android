package com.example.cecd.ui.start;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Start Fragment");
    }
}