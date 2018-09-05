package com.example.songhyeonseok.drawing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StartActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SignInButton lginbtn;
    private GoogleApiClient mgoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private Users members;
    private User login_user;
    private DatabaseReference ref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser firebaseUser;
    FrameLayout startTap;
    LinearLayout touchArea,coverArea;
    ProgressBar progressTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        startTap = (FrameLayout) findViewById(R.id.StartTap);
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.start_2,bo);
        Drawable drawable = new BitmapDrawable(bitmap);
        startTap.setBackground(drawable);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ref = mFirebaseDatabase.getReference().child("Members");

        touchArea = (LinearLayout)findViewById(R.id.touchArea);
        coverArea = (LinearLayout)findViewById(R.id.coverArea);
        progressTap = (ProgressBar)findViewById(R.id.progress_init);
        lginbtn = (SignInButton)findViewById(R.id.sign_in_btn);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        touchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.appear_slowly);
                lginbtn.setVisibility(View.VISIBLE);
                lginbtn.startAnimation(animation);
                lginbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mgoogleApiClient);
                        startActivityForResult(intent, 100);

                    }
                });

            }
        });

        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        /*
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).addApi(Drive.API).addScope(Drive.SCOPE_FILE)
                .build();
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            coverArea.setVisibility(View.VISIBLE);
            touchArea.setVisibility(View.GONE);
            GoogleSignInResult result
                    = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            //Toast.makeText(AuthActivity.this,result.isSuccess()+"온액리저트",Toast.LENGTH_LONG).show();
            if(result.isSuccess()){
                firebaseWithGoogle(account);
            }else{
                Toast.makeText(StartActivity.this,"인증 실패",Toast.LENGTH_LONG).show();
                coverArea.setVisibility(View.GONE);
                touchArea.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(StartActivity.this, "실패",Toast.LENGTH_LONG).show();
        coverArea.setVisibility(View.GONE);
        touchArea.setVisibility(View.VISIBLE);
    }

    private void firebaseWithGoogle(GoogleSignInAccount account){
        AuthCredential credential
                = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        Task<AuthResult> authResultTask
                = mFirebaseAuth.signInWithCredential(credential);

        authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               // startActivity(new Intent(StartActivity.this,ChoiceService.class));
                firebaseUser = authResult.getUser();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        members = dataSnapshot.getValue(Users.class);

                        ref.removeEventListener(this);
                        login_user = new User();
                        login_user.setUser_id(firebaseUser.getUid());
                        login_user.setUser_name(firebaseUser.getDisplayName());
                        login_user.setUser_email(firebaseUser.getEmail());
                        if(members == null){
                            Log.d("@@@@@@@@@@@@@@@@@@@@@@","멤버스가 널입니다@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            ref.removeValue();
                            members = new Users();
                            members.addUser(login_user);
                            ref.setValue(members);
                        }else if (members.findUserForUID(login_user.getUser_id()) != true) {
                            Log.d("@@@@@@@@@@@@@@@@@@@@@@","맴버 목록에 회원이 없습니다.");
                            ref.removeValue();
                            members.addUser(login_user);
                            ref.setValue(members);
                        }else{
                            Log.d("@@@@@@@@@@@@@@@@@@@@@@","목록이 널값도 아니고 회원도 존재합니다.: 정상");
                        }

                        startActivity(new Intent(StartActivity.this,ChoiceService.class));
                        mgoogleApiClient.clearDefaultAccountAndReconnect();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

    }

}
