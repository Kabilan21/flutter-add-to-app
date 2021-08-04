package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NativeFragment extends Fragment {


    private  SharedViewModel sharedViewModel;

    public NativeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_native, container, false);

        TextView textView = view.findViewById(R.id.textButton);

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedViewModel.setIndex(0);
            }
        });
        return view;
    }
}