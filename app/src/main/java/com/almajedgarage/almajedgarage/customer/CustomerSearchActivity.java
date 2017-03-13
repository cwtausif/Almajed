package com.almajedgarage.almajedgarage.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.almajedgarage.almajedgarage.Adapters.CustomerSearchAdapter;
import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
public class CustomerSearchActivity extends AppCompatActivity {
    ListView customerSearchList;
    CustomerSearchAdapter customerSearchAdapter;
    String searchText = "";
    CustomerSearchModel customerSearchModel;
    ArrayList<CustomerSearchModel> carsArray;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);

        carsArray = new ArrayList<>();


        try {
            Bundle data = getIntent().getExtras();
            searchText = data.getString("search");
        }catch (Exception e){

        }

        //References
        customerSearchList = (ListView) findViewById(R.id.customerSearch_list);
        pDialog = new ProgressDialog(CustomerSearchActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching data..");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(GlobCar.isNetworkAvailable(CustomerSearchActivity.this)){
            serverRequest();
        }


    }
    public void serverRequest()
    {
        pDialog.show();
        Log.d("response in serverreq: ",searchText);
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("searchtext",searchText);

        RequestParams params = new RequestParams(paramMap);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = GlobCar.base_url+GlobCar.search;
                client.post(url,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        Log.d("response: ",response.toString());
                        if(response.toString().length()<5){
                            Log.d("res noresult", response.toString());

                            if (response.length()<1){
                                pDialog.hide();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "No Record Found", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                return;
                            }

                        }
                        if (response.length()>0){
                            Log.d("res: record founds:",response.length()+"");

                                try {
                                    for (int i=0; i<response.length(); i++){
                                        JSONObject carData = response.getJSONObject(i);
                                        Log.d("res cardata: ",i+" "+carData.toString());
                                        customerSearchModel = new CustomerSearchModel(carData.getInt("id"),carData.getString("contact"),carData.getString("customer"),carData.getString("carno"),carData.getString("charges"),carData.getString("comments"),carData.getString("status"),carData.getString("imageone"),carData.getString("imagetwo"),carData.getString("imagethree"));
                                        carsArray.add(customerSearchModel);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                            }
                            pDialog.hide();
                            customerSearchAdapter = new CustomerSearchAdapter(getApplicationContext(),carsArray);
                            customerSearchList.setAdapter(customerSearchAdapter);
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