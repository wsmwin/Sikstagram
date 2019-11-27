package com.example.sikstagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, fullname, email, password;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        // firebase 사용자 인스턴스에 연결
        auth = FirebaseAuth.getInstance();

        // 로그인 버튼 클릭 시 로그인 화면으로 이동
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // register 버튼 클릭 시 실행 함수
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로딩 창
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                // 입력 정보들을 저장
                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                // 정보 미입력시 메세지 띄움
                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if(str_password.length() < 6){
                    // 비밀번호 6자리 이상
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    // register 함수 실행
                    register(str_username, str_fullname, str_email, str_password);
                }
            }
        });
    }

        // 실제 회원가입 기능 함수
        public void register(final String username, final String fullname, String email, String password){

            // 파이어베이스에 새로운 회원 객체 생성
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("username", username.toLowerCase());
                            map.put("fullname", fullname);
                            // 기본 프로필 이미지는 firebase에 저장되어 있는 이미지를 사용
                            map.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/sikstagram.appspot.com/o/sikstagram.png?alt=media&token=a06edc97-f610-4bd6-9cee-04205ff64a40");
                            map.put("bio", "");

                            // map 형식으로 db에 저장.
                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                // 작업 성공 시 메인 엑티비티로 넘어감.
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        // 테스크 초기화 후 로그인 상태로 플래그 설정
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        // 재접속시 로그인 상태 유지를 위해 intent를 startActivity에 전달.
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            // 회원가입 실패시 메세지
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
