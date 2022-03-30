package com.bubnov.lab6base;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//repo class for db
public class AuthRepository {
    private static final String TAG = "Firebase";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        if(firebaseAuth.getCurrentUser() != null){
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
        }
    }

    public void userRegistration(String firstName, String surname, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if(task.isSuccessful()){
                        if(firebaseAuth.getCurrentUser() != null){
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("first name", firstName);
                            user.put("surname", surname);
                            user.put("email", email);
                            documentReference.set(user).addOnCompleteListener(AVoid -> Log.i(TAG, "onSuccessful: user data was saved"))
                            .addOnFailureListener(e -> Log.e(TAG, "onFailure: Error writing to DB document", e));
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        }
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException().getMessage())
                                , Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void logOut(){
        firebaseAuth.signOut();
        loggedOutMutableLiveData.postValue(true);
    }

    public void logIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task ->{
                    if(task.isSuccessful()){
                        userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException()
                        .getMessage())
                        , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadUserData(){
        if(firebaseAuth.getCurrentUser() != null){
            String uid = firebaseAuth.getCurrentUser().getUid();
            DocumentReference doc = db.collection("users").document(uid);
            doc.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
                        userArrayList.add(user);
                        userLiveData.setValue(userArrayList);
                    }).addOnFailureListener(e ->
                    Toast.makeText(application, application.getString(R.string.error, e.getMessage()), Toast.LENGTH_SHORT).show());
        }
    }

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
