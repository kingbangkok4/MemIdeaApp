package com.app.memidea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nuttapong_i on 31/03/2559.
 */
public class NoteAcivity extends Activity{
    private TextView txtCategory;
    private EditText txtTitle;
    private ListView lvNote;
    private Button btSearch;
    private JSONUrl json = new JSONUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        String i_category = intent.getStringExtra("category");

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtCategory = (TextView)findViewById(R.id.textViewCategory);
        txtTitle = (EditText)findViewById(R.id.editTextTitle);
        lvNote = (ListView)findViewById(R.id.listViewNote);
        btSearch = (Button)findViewById(R.id.btnSearch);

        txtCategory.setText(i_category);

        LoadData(txtCategory.getText().toString().trim(), "");

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadData(txtCategory.getText().toString().trim(), txtTitle.getText().toString().trim());
            }
        });
    }

    private void LoadData(String category, String title) {
        String url = getString(R.string.str_url) + "queryNote.php";

        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("title", title));

        try {
            JSONArray data = new JSONArray(json.getJSONUrl(url, params));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("id", c.getString("id"));
                map.put("category", c.getString("category"));
                map.put("date_time", c.getString("date_time"));
                map.put("title", c.getString("title"));
                map.put("detail", c.getString("detail"));
                MyArrList.add(map);

            }


            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(NoteAcivity.this, MyArrList, R.layout.activity_column_note,
                    new String[] {"title", "date_time"}, new int[] {R.id.ColTitle, R.id.ColDateTime});
            lvNote.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
            // OnClick Item
            lvNote.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    String strId = MyArrList.get(position).get("id")
                            .toString();
                    String strCategory = MyArrList.get(position).get("category")
                            .toString();
                    String strDateTime = MyArrList.get(position).get("date_time")
                            .toString();
                    String strTitle = MyArrList.get(position).get("title")
                            .toString();
                    String strDetail = MyArrList.get(position).get("detail")
                            .toString();

                    viewDetail.setIcon(android.R.drawable.btn_star_big_on);
                    viewDetail.setTitle("Note Detail");
                    viewDetail.setMessage("id : " + strId + "\n"
                            + "category : " + strCategory + "\n"
                            + "date_time : " + strDateTime + "\n"
                            + "title : " + strTitle + "\n"
                            + "detail : " + strDetail);
                    viewDetail.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });
                    viewDetail.show();

                }
            });


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

   /* public String getJSONUrl(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }*/
}
