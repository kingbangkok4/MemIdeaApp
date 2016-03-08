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


public class MainActivity extends Activity {
    HttpActivity Http = new HttpActivity();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        // txtUsername & txtPassword
        final EditText txtUser = (EditText)findViewById(R.id.editTextUsername);
        final EditText txtPass = (EditText)findViewById(R.id.editTextPassword);

        // btnLogin
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        // Perform action on click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = getString(R.string.str_url) + "checkLogin.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("strUser", txtUser.getText().toString()));
                params.add(new BasicNameValuePair("strPass", txtPass.getText().toString()));


                String resultServer  = Http.getHttpPost(url,params);

                /*** Default Value ***/
                String strStatusID = "0";
                String strMemberID = "0";
                String strError = "Unknow Status!";

                JSONObject c;
                try {
                    c = new JSONObject(resultServer);
                    strStatusID = c.getString("StatusID");
                    strMemberID = c.getString("MemberID");
                    strError = c.getString("Error");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                // Prepare Login
                if(strStatusID.equals("0"))
                {
                    // Dialog
                    ad.setTitle("แจ้งเตือน! ");
                    ad.setIcon(android.R.drawable.btn_star_big_on);
                    ad.setPositiveButton("ปิด", null);
                    ad.setMessage(strError);
                    ad.show();
                    txtUser.setText("");
                    txtPass.setText("");
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Login OK", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,MenuActivity.class);
                    //i.putExtra("MemberID", strMemberID);
                    startActivity(i);
                }

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

}