package com.example.songhyeonseok.drawing;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogInPopUp extends AppCompatActivity{

    final Context context = this;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_popup);
        final TextView forgetLogin = (TextView)findViewById(R.id.forgotLogin);
        final EditText sendingMail = new EditText(this);
        TextView membership = (TextView)findViewById(R.id.membership);
        forgetLogin.setText(Html.fromHtml("<u>로그인 정보를 잊으셨나요?</u>"));
        membership.setText(Html.fromHtml("<u>회원가입</u>"));



        alertDialogBuilder = new AlertDialog.Builder(context);
        // 제목셋팅
        alertDialogBuilder.setTitle("계정 정보 찾기");
        alertDialogBuilder.setView(sendingMail); // 로그인잃어버릴시 메일 입력창

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("이메일을 입력해주세요.")
                .setCancelable(false)
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("보내기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                String email = sendingMail.getText().toString();
                                forgetLogin.setText(email); //입력 받은값이 지대로 되는지 테스트하는 부분
                                dialog.cancel();
                                //               LogInPopUp.this.finish();
                            }
                        });

        // 다이얼로그 생성
        alertDialog = alertDialogBuilder.create();


        forgetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 다이얼로그 보여주기

                alertDialog.show();
            }
        });

        Button login = (Button)findViewById(R.id.submit_go);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
