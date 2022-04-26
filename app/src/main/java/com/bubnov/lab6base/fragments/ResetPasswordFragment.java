package com.bubnov.lab6base.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bubnov.lab6base.R;
import com.bubnov.lab6base.viewmodel.ResetPasswordViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class ResetPasswordFragment extends Fragment {

    private ResetPasswordViewModel resetPasswordViewModel;
    private final  String TAG = "";
    String email;

    public static boolean isStringNullOrEmpty(String... strings){
        for (String str : strings){
            if (str == null || str.isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            email = getArguments().getString("Email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);
        requireActivity().setTitle(getString(R.string.gafpe));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText oldEmail = view.findViewById(R.id.tvResetPassword);
        oldEmail.setText(email);
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        Log.i(TAG, "onViewCreated: here3");
        view.findViewById(R.id.sendPassword).setOnClickListener(view1 ->{
            email = Objects.requireNonNull(oldEmail.getText()).toString().trim();
            if(isStringNullOrEmpty(email)){
                Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            }
            else{
                resetPasswordViewModel.resetPassword(email);
                Navigation.findNavController(view).navigate(R.id.action_resetPasswordFragment_to_loginFragment);
            }
        });

    }
}