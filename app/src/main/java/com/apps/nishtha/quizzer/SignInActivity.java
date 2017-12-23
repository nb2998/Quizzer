package com.apps.nishtha.quizzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "TAG";
    Button sign_in_button;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.server_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        if(GoogleSignIn.getLastSignedInAccount(this)!=null){

        } else{

        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.sign_in_button){
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent=googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN) {
            Task<GoogleSignInAccount> googleSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(googleSignInTask);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> googleSignInTask) {
//        try {
            GoogleSignInAccount googleSignInAccount = googleSignInTask.getResult();
            updateUi(googleSignInAccount);
//        }catch(ApiException e){
//            Log.e(TAG, "handleSignInResult: "+ e.getLocalizedMessage());
//        }

    }

    private void updateUi(GoogleSignInAccount googleSignInAccount) {
        sign_in_button.setVisibility(View.GONE);
        TextView tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvWelcomeUser.setText(R.string.welcome+googleSignInAccount.getDisplayName());
        tvWelcomeUser.setVisibility(View.VISIBLE);
    }


}
