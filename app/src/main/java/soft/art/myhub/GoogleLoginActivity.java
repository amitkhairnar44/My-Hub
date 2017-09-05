package soft.art.myhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GoogleLoginActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiclient;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    SignInButton signInButton;
    SweetAlertDialog pDialog;
    private static final int RC_SIGN_IN = 9001;
    public static final String userTypePref = "usertypepref";
    public static final String userType = "usertype";
    public static final String student = "student";
    public static final String faculty = "faculty";
    String appUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences(userTypePref,MODE_PRIVATE);
        appUser = sharedPreferences.getString(userType,"");

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        if (appUser.equals(faculty))
        {
            if (currentUser != null)
            {
                Intent facultyIntent = new Intent(GoogleLoginActivity.this,NoticeActivity.class);
                startActivity(facultyIntent);
                finish();
            }
        }
        if (appUser.equals(student))
        {
            if (currentUser != null)
            {
                Intent intent = new Intent(GoogleLoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                showLoadingDialog();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                Toast.makeText(this, "Sign in Failed :(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiclient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(GoogleLoginActivity.this, "Login Successful :)", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();

                            if (appUser.equals(faculty))
                            {
                                Intent intent = new Intent(GoogleLoginActivity.this,NoticeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if (appUser.equals(student))
                            {
                                Intent intent = new Intent(GoogleLoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                        else
                        {
                            Toast.makeText(GoogleLoginActivity.this, "Login Unsuccessful :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showLoadingDialog()
    {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

    }
}
