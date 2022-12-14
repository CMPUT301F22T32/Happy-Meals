package com.example.happymeals.database;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.happymeals.userlogin.OutputListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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

        if( fireAuth == null ){
            fireAuth = new FirebaseAuthenticationHandler();
        }
        return fireAuth;
    }

    /**
     * Tries to log the user in.
     * @param email The {@link String} value that was passed in as the email.
     * @param password The {@link String } that was passed in as the password.
     * @param listener The {@link OutputListener } which will listen for the successful login.
     */
    public void userLogin( String email, String password, OutputListener listener ){
        fireAuth.authenticate.signInWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            // test
            public void onComplete( @NonNull Task<AuthResult> action ) {
                if ( action.isSuccessful() ) {
                    listener.onSuccess();
                    Log.d( "LoginActivity", "User has successfully authenticated" );
                }
                else {
                    listener.onFailure( action.getException() );
                    Log.d( "LoginActivity", "User authentication was unsuccessful" );
                }

            }
        } );
    }

    public void validUser( OutputListener listener ) {
        // check is a user already exists

        if( fireAuth.authenticate.getCurrentUser() != null ) {
            Log.d( "LoginActivity", "Valid existing user" );
        }
        else {
            Log.d( "LoginActivity", "Not an existing user" );
        }
    }

    /** 2 ) Create new users
     * @param: user - email/username
     * @param: password - user password
     * @param: listener - checks if UI works as expected
     */

    public void userRegister( String email, String password, String username, OutputListener listener ) {
        fireAuth.authenticate.createUserWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete( @NonNull Task<AuthResult> action ) {
                if( action.isSuccessful() ) {
                    listener.onSuccess();
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName( username ).build();
                    fireAuth.authenticate.getCurrentUser().updateProfile( profileUpdate );
                } else {
                    listener.onFailure( action.getException() );
                }
            }
        } );
    }
}
