package com.almajedgarage.almajedgarage.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.almajedgarage.almajedgarage.admin.AdminRegisterCarActivity;
import com.almajedgarage.almajedgarage.customer.CustomerSearchModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tausif on 12/4/2016.
 */
public class AdminAllCarsAdapter extends BaseAdapter {

    //Arrays to receive Country Names and Flags
    ArrayList<CustomerSearchModel> allCarsData;
    //Context reference of MainActivity
    Context mainActivityContext;
    CustomerSearchModel customerSearchModel;
    LayoutInflater xmlInflater=null;
    Context context;

    //Create Constructor of CustomAdapter which is being called from MainActivity.java while creating CustomAdapter Class Object.
    public AdminAllCarsAdapter(Context mainActContext, ArrayList<CustomerSearchModel> carsData,Activity context) {
        this.mainActivityContext = mainActContext;
        this.allCarsData = carsData;
        this.context = context;
        //LayoutInflater will help us out to manipulate Activity XML(activity_main.xml) with predefined XML Layout(custom_row.xml)
        xmlInflater = (LayoutInflater) mainActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        //Total Number of Records in Array
        return allCarsData.size();
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

        View carItem = xmlInflater.inflate(R.layout.admin_all_cars_row, null);
        TextView status = (TextView) carItem.findViewById(R.id.status_tv);
        final TextView carno_tv = (TextView) carItem.findViewById(R.id.carno_tv);
        final Button row_edit = (Button) carItem.findViewById(R.id.row_edit);
        final Button row_del = (Button) carItem.findViewById(R.id.row_del);
        ImageView carImageRow = (ImageView) carItem.findViewById(R.id.car_image_row);

        customerSearchModel = new CustomerSearchModel();
        customerSearchModel = allCarsData.get(position);
        status.setText(customerSearchModel.getStatus());
        carno_tv.setText(customerSearchModel.getCarno());
        row_del.setTag(position);

        if (customerSearchModel.getImageone().length()>0){
            Picasso.with(mainActivityContext).load(GlobCar.base_url+customerSearchModel.getImageone()).into(carImageRow);
            Log.d("image",customerSearchModel.getImage());
        }

        row_edit.setTag(position);

        //region Edit Car Admin
        row_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivityContext, AdminRegisterCarActivity.class);
                int pos = (int) row_edit.getTag();
                CustomerSearchModel searchModel = new CustomerSearchModel();
                searchModel = allCarsData.get(pos);

                intent.putExtra("id", searchModel.getId());
                intent.putExtra("status",searchModel.getStatus());
                intent.putExtra("description",searchModel.getDescription());
                intent.putExtra("charges",searchModel.getCharges());
                intent.putExtra("carno",searchModel.getCarno());
                intent.putExtra("customer",searchModel.getCustomer());
                intent.putExtra("contact",searchModel.getContact());
                intent.putExtra("imageone",searchModel.getImageone());
                intent.putExtra("imagetwo",searchModel.getImagetwo());
                intent.putExtra("imagethree",searchModel.getImagethree());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityContext.startActivity(intent);
            }
        });
        //endregion

        //region Delete Car Admin
        row_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int position = (int) row_del.getTag();
                //Toast.makeText(mainActivityContext,"Delete: "+position,Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Authentication!");
                builder.setIcon(R.drawable.warning);
                builder.setMessage("Are you sure to delete this record ?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCarRecord(position);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                });
                builder.show();
            }
        });
        //endregion
        return carItem;
    }

    private void deleteCarRecord(final int position) {
        int id = 0;
        customerSearchModel = allCarsData.get(position);
        id = customerSearchModel.getId();
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(context, "dialog title",
                "dialog message", true);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Deleting Car...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = GlobCar.base_url+GlobCar.delete_car;
        final RequestParams params = new RequestParams();
        params.put("id",id);
        client.post(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d("response: ",response.toString());

                try {

                    String result = response.getString("success");
                    if (result.equals("true")){
                        Toast.makeText(mainActivityContext,"Successfully Deleted!!",Toast.LENGTH_SHORT).show();
                        allCarsData.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mainActivityContext,"Action Faild!!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response failure:  ",responseString);
                Toast.makeText(mainActivityContext,"Network Problem!!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
