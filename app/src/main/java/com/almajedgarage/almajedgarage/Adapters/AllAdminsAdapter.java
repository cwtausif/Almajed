package com.almajedgarage.almajedgarage.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.almajedgarage.almajedgarage.admin.AdminModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mg on 1/8/2017.
 */
public class AllAdminsAdapter extends BaseAdapter {

    ArrayList<AdminModel> mDataList;
    Context mainActivityContext;

    LayoutInflater xmlInflater=null;
    AdminModel adminModel;
    ProgressDialog pDialog;
    String TAG = "AllAdmins";

    public AllAdminsAdapter(Context context, ArrayList<AdminModel> mDataList) {
        this.mainActivityContext = context;
        xmlInflater = (LayoutInflater) mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDataList = mDataList;

        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Deleting");

    }


    @Override
    public int getCount() {
        //Total Number of Records in Array
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        //Return Position Because by Calling getItem we can get Item at specific position in An Array i.e countryNamesArr[0] will return Australia
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = xmlInflater.inflate(R.layout.custom_admin_row, null);


        TextView usernameText = (TextView) item.findViewById(R.id.username);
        final ImageView deleteAdmin = (ImageView) item.findViewById(R.id.deleteAdmin);

        adminModel = new AdminModel();
        adminModel = mDataList.get(position);
        //Set country Name
        usernameText.setText(adminModel.getUsername().toString());
        deleteAdmin.setTag(position);
        deleteAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final int pos = (int) deleteAdmin.getTag();
                    adminModel = new AdminModel();
                    adminModel = mDataList.get(pos);
                    Log.d("Pos",pos+"");
                    final String admin_id = adminModel.getId();
                    Log.d("id",admin_id);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityContext)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    serverRequest(admin_id, pos);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.setIcon(R.drawable.delete);
                    builder.setTitle("Are you sure to delete Admin?");
                    AlertDialog alert = builder.create();
                    alert.show();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        //return CustomRow
        return item;
    }

    private void serverRequest(String admin_id, final int pos) {
        Log.d("server",admin_id);
       pDialog.show();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("deleteAdmin","true");
        paramMap.put("id",admin_id);

        RequestParams params = new RequestParams(paramMap);
        Log.d(TAG+"params",params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        String url = GlobCar.base_url+GlobCar.admin;
        Log.d(TAG+"url",url);
        client.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d(TAG+"response: ",response.toString());
                pDialog.dismiss();
                try {
                    String success = response.optString("success");
                    if (success.equals("true")){
                        Toast.makeText(mainActivityContext,response.optString("message"),Toast.LENGTH_SHORT).show();
                        mDataList.remove(pos);
                        notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mainActivityContext,"Action Failed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response failure:  ",responseString);
                pDialog.hide();
            }
        });
    }
}
