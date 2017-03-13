package com.almajedgarage.almajedgarage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.almajedgarage.almajedgarage.customer.CustomerCarFullDetailActivity;
import com.almajedgarage.almajedgarage.customer.CustomerSearchModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mg on 12/4/2016.
 */
public class CustomerSearchAdapter extends BaseAdapter {

    //Arrays to receive Country Names and Flags
    String [] countryNamesArr;
    int [] countryFlagsArr;
    ArrayList<CustomerSearchModel> allCarsData;
    //Context reference of MainActivity
    Context mainActivityContext;
    CustomerSearchModel customerSearchModel;
    LayoutInflater xmlInflater=null;

    //Create Constructor of CustomAdapter which is being called from MainActivity.java while creating CustomAdapter Class Object.
    public CustomerSearchAdapter(Context mainActContext, ArrayList<CustomerSearchModel> carsData) {
        this.mainActivityContext = mainActContext;
        this.allCarsData = carsData;
        customerSearchModel = new CustomerSearchModel();
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

        View carItem = xmlInflater.inflate(R.layout.customer_search_row, null);
        TextView status = (TextView) carItem.findViewById(R.id.statusTV);
        TextView description = (TextView) carItem.findViewById(R.id.descriptionTVcar);
        final Button viewDetailsBtn = (Button) carItem.findViewById(R.id.viewDetails_btn);
        ImageView car_image_row = (ImageView) carItem.findViewById(R.id.car_image_row);

        customerSearchModel = allCarsData.get(position);
        status.setText(customerSearchModel.getStatus());
        if (customerSearchModel.getDescription().length()>50){
            description.setText(customerSearchModel.getDescription().substring(0,90)+"...");
        }else {
            description.setText(customerSearchModel.getDescription());
        }


        if (customerSearchModel.getImageone().length()>0){
            Picasso.with(mainActivityContext).load(GlobCar.base_url+customerSearchModel.getImageone()).into(car_image_row);
        }


        viewDetailsBtn.setTag(position);

        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mainActivityContext,"ID"+viewDetailsBtn.getTag(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainActivityContext, CustomerCarFullDetailActivity.class);
                int pos = (int) viewDetailsBtn.getTag();
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
        //return CustomRow
        return carItem;
    }
}

