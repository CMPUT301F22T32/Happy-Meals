package com.example.happymeals.database;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.happymeals.userlogin.OutputListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**@author bfiogbe
 */


public class FirebaseAuthenticationHandler {

    // create FireAuth to function statically and allow login and register to access it
    public FirebaseFirestore fireStore;
    public FirebaseAuth authenticate;
    static private FirebaseAuthenticationHandler fireAuth;


    private FirebaseAuthenticationHandler(){
        fireAuth = null;
        fireStore = FirebaseFirestore.getInstance();
        authenticate = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthenticationHandler getFireAuth(){

        if(fireAuth == null){
            fireAuth = new FirebaseAuthenticationHandler();
        }
        return fireAuth;
    }





    /** 1) Authenticate user Login -- Checks with data to see if userinput is valid and in Firestore
     * @param: user - email/username
     * @param: password - user password
     * @param: listener - checks if UI works as expected
     */

    public void userLogin(String user, String password, OutputListener listener){
        fireAuth.authenticate.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            // test
            public void onComplete(@NonNull Task<AuthResult> action) {
                if (action.isSuccessful()) {
                    listener.onSuccess();
                    Log.d("LoginActivity", "User has successfully authenticated");
                }
                else {
                    listener.onFailure(new Exception());
                    Log.d("LoginActivity", "User authentication was unsuccessful");
                }

            }
        });
    }

    public void validUser(OutputListener listener) {
        // check is a user already exists

        if(fireAuth.authenticate.getCurrentUser() != null) {
            Log.d("LoginActivity", "Valid existing user");
        }
        else {
            Log.d("LoginActivity", "Not an existing user");
        }
    }

    /** 2) Create new users
     * @param: user - email/username
     * @param: password - user password
     * @param: listener - checks if UI works as expected
     */

    public void userRegister(String user, String password, OutputListener listener) {
        fireAuth.authenticate.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> action) {
                if(action.isSuccessful()) {
                    String IdUser = fireAuth.authenticate.getCurrentUser().getUid();
                    DocumentReference documentReference = fireAuth.fireStore.collection("Users").document(IdUser);

                    //store every user in a hashmap into Firestore

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("User", user);

                    // add to Firestore

                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            listener.onSuccess();
                            Log.d("RegisterActivity", "User has been added to firebase");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("RegisterActivity", "Error: User not created or added to firebase");
                            listener.onFailure(e);
                        }
                    });


                }
            }
        });
    }

}
