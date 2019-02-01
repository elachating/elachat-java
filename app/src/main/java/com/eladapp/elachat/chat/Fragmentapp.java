package com.eladapp.elachat.chat;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import com.eladapp.elachat.R;
public class Fragmentapp extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_app, container,false);
        return view;
    }
}