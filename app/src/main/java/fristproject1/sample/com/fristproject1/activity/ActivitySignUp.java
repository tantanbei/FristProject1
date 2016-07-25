package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.string.XString;

public class ActivitySignUp extends Activity{

    EditText phone;
    EditText code;
    EditText password;
    EditText confirmPassword;
    Button getCode;
    Button signUp;

    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        getCode = (Button) findViewById(R.id.get_verification_code);
        signUp = (Button) findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XString.IsEmpty(code.getText().toString())){
                    Toast.makeText(ActivitySignUp.this,XString.GetString(ActivitySignUp.this,R.string.input_verification_code),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!code.getText().toString().equals(verificationCode)){
                    Toast.makeText(ActivitySignUp.this, R.string.verification_error,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                    Toast.makeText(ActivitySignUp.this, R.string.confirm_password_error,Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });
    }
}
