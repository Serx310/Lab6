package com.bubnov.lab6base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ARGS:";
    NavController navController;
    TextInputEditText email, password;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        view.findViewById(R.id.btnLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                navController.navigate(R.id.action_loginFragment_to_userFragment);
                break;
            case R.id.not_registered:
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                break;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}