package com.example.task_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.annotations.Nullable;


public class CertificateFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_certificate, container, false);

        Button winCertificateButton = view.findViewById(R.id.win_certificate_button);
        winCertificateButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WinCertificateActivity.class);
            startActivity(intent);
        });

        return view;
    }
}