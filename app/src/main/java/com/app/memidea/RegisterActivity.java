package com.app.memidea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 01/03/2559.
 */
public class RegisterActivity extends Activity {
    EditText txtPhone, txtPassword, txtConfirmPassword, txtEmail;
    Button btRegister, btBack;
    HttpActivity Http = new HttpActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtPhone = (EditText) findViewById(R.id.editTextPhone);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        txtEmail = (EditText)findViewById(R.id.editTextEmail);

        btRegister = (Button) findViewById(R.id.btnRegister);
        btBack = (Button) findViewById(R.id.btnBack);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterMember();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void RegisterMember() {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        /*** Default Value ***/
        String strStatusID = "0";
        String strMemberID = "0";
        String strError = "Unknow Status!";
        if (!"".equals(txtEmail.getText().toString().trim()) && !"".equals(txtPhone.getText().toString().trim()) && !"".equals(txtPassword.getText().toString().trim()) && !"".equals(txtConfirmPassword.getText().toString().trim())) {
            if (txtPassword.getText().toString().trim().equals(txtConfirmPassword.getText().toString().trim())) {
                String url = getString(R.string.str_url) + "register.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("strUser", txtPhone.getText().toString().trim()));
                params.add(new BasicNameValuePair("strPass", txtPassword.getText().toString().trim()));
                params.add(new BasicNameValuePair("strEmail", txtEmail.getText().toString().trim()));
                String resultServer = Http.getHttpPost(url, params);

                JSONObject c;
                try {
                    c = new JSONObject(resultServer);
                    strStatusID = c.getString("StatusID");
                    strMemberID = c.getString("MemberID");
                    strError = c.getString("Error");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    strError = e.getMessage();
                    e.printStackTrace();
                }
            } else {
                strError = "พาสเวิร์ดไม่ตรงกัน";
            }
        } else {
            strError = "กรุณากรอกข้อมูลให้ครบถ้วน";
        }

        // Prepare Login
        if (strStatusID.equals("0")) { // Dialog
            ad.setTitle("แจ้งเตือน! ");
            ad.setIcon(android.R.drawable.btn_star_big_on);
            ad.setPositiveButton("ปิด", null);
            ad.setMessage(strError);
            ad.show();

        } else {
            Toast.makeText(RegisterActivity.this, "สมัครสมาชิกสำเร็จ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            //i.putExtra("MemberID", strMemberID);
            startActivity(i);
        }
    }
}
