package com.app.memidea;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity {
    private String[] menu_list = { "การงาน", "เกษตร", "เทคโนโลยี", "บทกลอน", "คำคม", "ธรรมมะ", "อื่นๆ" };
    private String strCategory = "";
    private Spinner spnCategory;
    private EditText txtTitle, txtNote;
    private Button btNoteSave, btNoteClear;
    private String note = "";
    HttpActivity Http = new HttpActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, menu_list);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("เมนูหลัก"));
        tabLayout.addTab(tabLayout.newTab().setText("จดบันทึก"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                spnCategory = (Spinner)viewPager.findViewById(R.id.spinnerCategory);
                txtTitle = (EditText) viewPager.findViewById(R.id.editTextTitle);
                txtNote = (EditText) viewPager.findViewById(R.id.editTextNote);

                btNoteSave = (Button) viewPager.findViewById(R.id.btnNoteSave);
                btNoteClear = (Button) viewPager.findViewById(R.id.btnNoteClear);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategory.setAdapter(dataAdapter);

                btNoteSave.setVisibility(View.INVISIBLE);
                btNoteClear.setVisibility(View.INVISIBLE);

                spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        spnCategory.setSelection(position);
                        strCategory = (String) spnCategory.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

                btNoteSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ValidateData(strCategory, txtTitle.getText().toString().trim(), txtNote.getText().toString().trim());
                    }
                });
                btNoteClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtNote.setText("");
                        btNoteSave.setVisibility(View.INVISIBLE);
                        btNoteClear.setVisibility(View.INVISIBLE);
                    }
                });
                txtNote.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        note = txtNote.getText().toString().trim();
                        if (note.length() > 0) {
                            btNoteSave.setVisibility(View.VISIBLE);
                            btNoteClear.setVisibility(View.VISIBLE);
                        } else {
                            btNoteSave.setVisibility(View.INVISIBLE);
                            btNoteClear.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void ValidateData(String category, String title, String detail) {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        if("".equals(category) || "".equals(title)  || "".equals(detail) ){
            // Dialog
            ad.setTitle("แจ้งเตือน! ");
            ad.setIcon(android.R.drawable.btn_star_big_on);
            ad.setPositiveButton("ปิด", null);
            ad.setMessage("กรุณาระบุข้อมูลให้ครบถ้วนก่อนบันทึก!");
            ad.show();
        }else {
            String strStatusID = "0";
            String strError = "Unknow Status!";
            String url = getString(R.string.str_url) + "saveNote.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", category));
            params.add(new BasicNameValuePair("title", title));
            params.add(new BasicNameValuePair("detail", detail));
            String resultServer = Http.getHttpPost(url, params);

            JSONObject c;
            try {
                c = new JSONObject(resultServer);
                strStatusID = c.getString("StatusID");
                strError = c.getString("Error");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                strError = e.getMessage();
                e.printStackTrace();
            }

            // Dialog
            ad.setTitle("สถานะการบันทึก! ");
            ad.setIcon(android.R.drawable.btn_star_big_on);
            ad.setPositiveButton("ปิด", null);
            ad.setMessage(strError);
            ad.show();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       /* int id = item.getItemId();
        switch (id) {
            case R.id.action_call:
                Intent dialer= new Intent(Intent.ACTION_DIAL);
                startActivity(dialer);
                return true;
            case R.id.action_speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, 1234);

                return true;
            case R.id.action_done:

                Bundle args = new Bundle();
                args.putString("Menu", "You pressed done button.");
                Fragment detail = new TextFragment();
                detail.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();

                return true;
            case R.id.action_contacts:
                Toast.makeText(getApplicationContext(),"Contacts Clicked",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_status:
                Toast.makeText(getApplicationContext(),"Status Clicked",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();

        }*/
    }

/*    *//**
     * On selecting action bar icons
     * *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.phone:
                // search action
                return true;
            case R.id.computer:
                // location found
                LocationFound();
                return true;
            case R.id.action_refresh:
                // refresh
                return true;
            case R.id.action_help:
                // help action
                return true;
            case R.id.action_check_updates:
                // check for updates action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    *//**
     * Launching new activity
     * *//*
    private void LocationFound() {
        Intent i = new Intent(MainActivity.this, LocationFound.class);
        startActivity(i);
    }*/

}
