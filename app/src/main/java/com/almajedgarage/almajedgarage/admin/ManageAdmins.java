package com.almajedgarage.almajedgarage.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.Adapters.AllAdminsAdapter;
import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ManageAdmins extends AppCompatActivity {
    private static final String TAG = "ManageAdmin";
    ArrayList<AdminModel> mDataList;
    AllAdminsAdapter allAdminsAdapter;
    AdminModel adminModel;
    ProgressDialog pDialog;
    Context context;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admins);
        context = ManageAdmins.this;
        listView = (ListView) findViewById(R.id.listview);
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching data..");

        mDataList = new ArrayList<>();
        allAdminsAdapter = new AllAdminsAdapter(ManageAdmins.this,mDataList);
        listView.setAdapter(allAdminsAdapter);


        if(GlobCar.isNetworkAvailable(context)==true){
            serverRequest();
        }
    }
    public void serverRequest()
    {
        pDialog.show();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("alladmins","yes");

        RequestParams params = new RequestParams(paramMap);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = GlobCar.base_url+GlobCar.admin;
        client.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d("response: ",response.toString());
                pDialog.hide();
                try {
                    String success = response.optString("success");
                    if (success.equals("true")){
                        Toast.makeText(getApplicationContext(),response.optString("message"),Toast.LENGTH_SHORT).show();
                        if (response.optString("message").trim().equals("All Admins")){
                            JSONArray admins = response.getJSONArray("admins");
                            for (int i=0; i<admins.length(); i++){
                                JSONObject eachAdmin = admins.getJSONObject(i);
                                Log.d(TAG,"eachAdmin:"+eachAdmin.toString());
                                adminModel = new AdminModel();
                                adminModel.setId(eachAdmin.getString("id"));
                                adminModel.setUsername(eachAdmin.getString("username"));
                                mDataList.add(adminModel);
                            }
                        }
                    }
                    allAdminsAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Action Faild",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response failure:  ",responseString);
                pDialog.hide();
                Snackbar.make(getWindow().getDecorView().getRootView(), "Problem!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("response failure:  ","snake click");
                            }
                        }).show();
            }
        });
    }
}
