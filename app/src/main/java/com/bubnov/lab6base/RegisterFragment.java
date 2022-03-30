package com.bubnov.lab6base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "ARGS:";
    NavController navController;
    TextInputEditText name, surname, email, password, confirm_password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        name = view.findViewById(R.id.register_name);
        surname = view.findViewById(R.id.register_surname);
        email = view.findViewById(R.id.register_email);
        password = view.findViewById(R.id.register_password);
        confirm_password = view.findViewById(R.id.register_confirm_password);
        view.findViewById(R.id.registerBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.registerBtn){
            if(TextUtils.isEmpty(name.getText())){
                name.setError("Enter your name!");
            }else if(TextUtils.isEmpty(surname.getText())){
                surname.setError("Enter your surname!");
            }else if(TextUtils.isEmpty(email.getText()) || !isEmailValid(email.getText())){
                email.setError("Enter a valid email!");
            }else if(TextUtils.isEmpty(password.getText())){
                password.setError("Enter your password!");
            }else if(TextUtils.isEmpty(confirm_password.getText())){
                confirm_password.setError("Confirm your password!");
            }else if(confirm_password.getText() != password.getText()){
                confirm_password.setError("Passwords don't match");
            }else{
                Log.i(TAG, name.getText()+ "," + surname.getText()+ "," + email.getText());
                Bundle args = new Bundle();
                //args.putString("recipient", String.valueOf(etRecipient.getText()));
            }
        }else{
            navController.navigate(R.id.action_registerFragment_to_userFragment);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}