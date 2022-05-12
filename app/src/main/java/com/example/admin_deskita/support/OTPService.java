package com.example.admin_deskita.support;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin_deskita.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPService extends Activity {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String verificationId;
    public void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public boolean signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        final boolean[] res = {true};
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                         res[0] =true;
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            res[0] =false;

                        }
                    }
                });
        return res[0];
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId=s;



        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            Log.d("resulttttt",code);
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("errorrrrr",e.getMessage().toString());
        }


    };
    public boolean verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        Boolean res;
        try {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            res= signInWithCredential(credential);
            return res;
        }catch(Exception e){
            Log.d("Errorrrrrrr",e.getMessage());
            return false;
        }
    }
}
