package com.daahae.damoyeo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.view.Constant;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGuestLogin;
    private Context context;

    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;

    private FirebaseAuth mAuth;

    public LoginActivity(){
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();

        setGoogleLoginSetting();
    }

    private void setGoogleLoginSetting() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(Constant.TAG, "Login fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // auto login
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
            firebaseAuthWithGoogle(account);
    }

    private void initView(){
        btnGuestLogin = findViewById(R.id.btn_guest_login);
        signInButton = findViewById(R.id.btn_google_login);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        Constant.displayWidth = size.x;
    }

    private void initListener(){
        btnGuestLogin.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constant.LOG_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(Constant.TAG, "Google sign in failed", e);
                signInButton.setEnabled(true);
                signInButton.setClickable(true);
            }
        } else if(requestCode == Constant.GOOGLE_LOGIN) {
            if(resultCode == Constant.LOG_OUT) {
                //FirebaseAuth.getInstance().signOut();
                signOut();
                Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_google_login:
                googleSignIn();
                signInButton.setClickable(false);
                signInButton.setEnabled(false);
                break;
            case R.id.btn_guest_login:
                guestSignIn();
                break;
        }
    }

    // 구글 로그인
    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constant.LOG_IN);
    }

    // 게스트 로그인
    private void guestSignIn(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(Constant.LOGIN, Constant.GUEST_LOGIN);
        startActivityForResult(intent, Constant.GUEST_LOGIN);
    }

    // 파이어베이스와 로그인 인증정보 동기화
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(Constant.TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constant.TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "안녕하세요, "+user.getDisplayName() + "님", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constant.LOGIN, Constant.GOOGLE_LOGIN);
                            startActivityForResult(intent, Constant.GOOGLE_LOGIN);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Constant.TAG, "signInWithCredential:failure", task.getException());
                        }
                        signInButton.setEnabled(true);
                        signInButton.setClickable(true);
                    }
                });
    }

    // 로그아웃
    private void signOut() {
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mAuth.signOut();
                if(googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()) {
                                Log.d(Constant.TAG, "User Logged out");
                                //setResult();
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }
}