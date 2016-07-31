package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.SignUpInPacket;
import okhttp3.Response;

public class ActivitySignIn extends XActivity{
    EditText phone;
    EditText password;
    Button signin;

    @Override
    public int GetContentView() {
        return R.layout.activity_signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.sign_in);
        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.sign_up);

        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.sign_in);
        
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = phone.getText().toString();
                if (phoneNum.length()!=11){
                    Toast.makeText(ActivitySignIn.this, R.string.warning_phone_number,Toast.LENGTH_SHORT).show();
                }

                try {
                    Response response = Http.Post(Const.SERVER_IP+Const.URL_SIGN_IN,new SignUpInPacket(phoneNum,password.getText().toString()));
                    //// TODO: 2016/7/31
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
