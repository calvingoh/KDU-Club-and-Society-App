package com.example.kduclubandsociety.ui.clubs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClubsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClubsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is clubs fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}