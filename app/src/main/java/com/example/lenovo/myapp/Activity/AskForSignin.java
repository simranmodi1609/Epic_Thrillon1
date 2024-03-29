package com.example.lenovo.myapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.myapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

public class AskForSignin extends AppCompatActivity {

    private static final String TAG = "Hello";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private SignInButton signInButton;

    private SharedPreferences sharedPreferences;

    public static final String My_pref="loginDetail";
    private GoogleApiClient mGoogleApiClient;


    boolean email_login;
    JSONObject j;
    SharedPreferences.Editor editor;
    private final static int RC_SIGN_IN = 0;
    private Button register_button,fb,login;
    private Profile profile;
    String email_id;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_signin);
        sharedPreferences=getSharedPreferences(My_pref, Context.MODE_PRIVATE);
        signInButton = (SignInButton) findViewById(R.id.SignInButton);
        loginButton=(LoginButton) findViewById(R.id.login_button);
        fb=(Button)findViewById(R.id.fb);
        login = (Button)findViewById(R.id.login);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });
        callbackManager=CallbackManager.Factory.create();
        editor=sharedPreferences.edit();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FUVCKUNGNFHFJFJFKKFL","HRLLL LKLSFSJFKJS F JSH HFKJHSFUHSJ KFHSK");
                Intent i = new Intent(AskForSignin.this, Home_page.class);
                i.putExtra("Login","facebook");
                editor.putBoolean("Facebook",true);
                editor.commit();
                startActivity(i);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(AskForSignin.this,"failed login",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(AskForSignin.this,"Error in login",Toast.LENGTH_LONG);
            }
        });

        email_login=sharedPreferences.getBoolean("Email",false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient =new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(AskForSignin.this,"Went wrong",Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        register_button=(Button)findViewById(R.id.register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AskForSignin.this,Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskForSignin.this,Login.class);
                startActivity(intent);
            }
        });

    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent( mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if(email_login){
            Intent i = new Intent(AskForSignin.this, Home_page.class);
            startActivity(i);
            finish();
        }
        if(Profile.getCurrentProfile()!=null && AccessToken.getCurrentAccessToken()!=null){
            Intent i = new Intent(AskForSignin.this, Home_page.class);
            //i.putExtra("Login","facebook");
            //editor.putBoolean("Facebook",true);
            startActivity(i);
            finish();
        }
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }



    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Intent i = new Intent(AskForSignin.this,Home_page.class);
            editor.putBoolean("Google",true);
            editor.commit();
            i.putExtra("Login","Google");

            i.putExtra("not",getIntent().getStringExtra("name"));
            startActivity(i);
            finish();

        } else {

        }
    }
}
