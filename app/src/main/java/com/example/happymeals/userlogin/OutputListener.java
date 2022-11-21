package com.example.happymeals.userlogin;

/** Interface used to check if a method performed is successful or fails

 */

public interface OutputListener {

    void onSuccess();

    void onFailure(Exception e);
}
