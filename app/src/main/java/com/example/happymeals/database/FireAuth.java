package com.example.happymeals.database;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.happymeals.OutputListener;
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

/*

 */


public class FireAuth {

    // create FireAuth to function statically and allow login and register to access it
    public FirebaseFirestore fireStore;
    public FirebaseAuth authenticate;
    static private FireAuth fireauth;

    // getter and setter for FireAuth
    public static FireAuth getFireauth() {
        // check if empty than initialize
        if(fireauth == null) {
            fireauth = new FireAuth();
            fireauth.authenticate = FirebaseAuth.getInstance();
            fireauth.fireStore = FirebaseFirestore.getInstance();
        }
        return fireauth;
    }

    /** 1) Authenticate user Login -- Checks with data to see if userinput is valid and in Firestore
     * @param: user - email/username
     * @param: password - user password
     * @param: listener - checks if UI works as expected
     */

    public void userLogin(String user, String password, OutputListener listener){
        fireauth.authenticate.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            // test
            public void onComplete(@NonNull Task<AuthResult> action) {
                if (action.isSuccessful()) {
                    listener.onSuccess();
                    Log.d("TAG", "User has successfully authenticated");
                }
                else {
                    listener.onFailure(new Exception());
                    Log.d("TAG", "User authentication was unsuccessful");
                }

            }
        });
    }

    public void validUser(OutputListener listener) {
        // check is a user already exists

        if(fireauth.authenticate.getCurrentUser() != null) {
            Log.d("TAG", "Valid existing user");
        }
        else {
            Log.d("TAG", "Not an existing user");
        }
    }

    /** 2) Create new users
     * @param: user - email/username
     * @param: password - user password
     * @param: listener - checks if UI works as expected
     */

    public void userRegister(String user, String password, OutputListener listener) {
        fireauth.authenticate.createUserWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> action) {
                if(action.isSuccessful()) {
                    String IdUser = fireauth.authenticate.getCurrentUser().getUid();
                    DocumentReference documentReference = fireauth.fireStore.collection("Users").document(IdUser);

                    //store every user in a hashmap into Firestore

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("User", user);

                    // add to Firestore

                    documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            listener.onSuccess();
                            Log.d("TAG", "User has been added to firebase");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "Error: User not created or added to firebase");
                            listener.onFailure(e);
                        }
                    });


                }
            }
        });
    }

}
