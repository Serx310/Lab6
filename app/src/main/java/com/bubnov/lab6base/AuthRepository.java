package com.bubnov.lab6base;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bubnov.lab6base.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//repository class for db + auth
public class AuthRepository {
    //log tag
    private static final String TAG = "Firebase:";
    //firebase variables
    private final FirebaseAuth firebaseAuth;
    private Boolean emailVerified = false;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //mutablelivedata variables
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final MutableLiveData<ArrayList<User>> userLiveData;
    private final ArrayList<User> userArrayList = new ArrayList<>();
    private final Application application;

    public AuthRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        loggedOutMutableLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null){
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
            loadUserData();
        }
    }

    //method for registering user
    public void userRegistration(String firstName, String lastName, String email, String password){
        //creating a user with email + password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task->{
                    //if user was created successfully then save data to firebase firestore
                    if (task.isSuccessful()){
                        sendEmailVerification();
                        verifyEmailAddress();
                        if (firebaseAuth.getCurrentUser() != null){
                            //gets the newly created users UID
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            //creates new collection named users if one doesn't exist into it add a new document with UID reference
                            DocumentReference documentReference = db.collection("users").document(userId);
                            //this is the data that will be written into the document
                            Map<String,Object> user = new HashMap<>();
                            user.put("firstname",firstName);
                            user.put("lastname",lastName);
                            user.put("email",email);
                            documentReference.set(user)
                                    .addOnSuccessListener(aVoid -> Log.i(TAG, "onSuccess: user data was saved"))
                                    .addOnFailureListener(e -> Log.e(TAG, "onFailure: Error writing to DB document", e));
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException().getMessage())
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void verifyEmailAddress(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        emailVerified = firebaseUser.isEmailVerified();
        if(emailVerified){
            logOut();
        }else{
            Toast.makeText(application, "Please verify your email first!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(application, "Registration successful. Please check your inbox for email verification.", Toast.LENGTH_SHORT).show();
                        logOut();
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException().getMessage())
                                , Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    //method for user logout
    public void logOut(){
        firebaseAuth.signOut();
        loggedOutMutableLiveData.postValue(true);
    }

    //method for user log in with email + password
    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                   if (task.isSuccessful()){
                       userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                   }else{
                       Toast.makeText(application, application.getString(R.string.error, task.getException()
                                       .getMessage())
                               , Toast.LENGTH_SHORT).show();
                   }
                });
    }
    //method for obtaining user data from db
    public void loadUserData(){
        if (firebaseAuth.getCurrentUser() != null){
            String uid = firebaseAuth.getCurrentUser().getUid();
            DocumentReference doc = db.collection("users").document(uid);
            doc.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        userArrayList.add(user);
                        userLiveData.setValue(userArrayList);
                    }).addOnFailureListener(e ->
 Toast.makeText(application, application.getString(R.string.error,e.getMessage()), Toast.LENGTH_SHORT).show());
        }
    }
    //getters for livedata
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }
    public MutableLiveData<ArrayList<User>> getUserLiveData() {
        return userLiveData;
    }
}










