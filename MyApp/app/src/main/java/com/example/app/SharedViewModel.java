package com.example.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    public void setIndex(Integer integer){
        mIndex.setValue(integer);
    }

    public LiveData<Integer> getIndex(){
        return mIndex;
    }


}
