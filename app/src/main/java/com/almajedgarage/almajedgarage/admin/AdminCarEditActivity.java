package com.almajedgarage.almajedgarage.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.squareup.picasso.Picasso;

public class AdminCarEditActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageOne,imageTwo,imageThree;
    TextView commentTV,chargesTV,carnoTV,customerTV,cantactTV;
    int id;
    Spinner currentStatusSpin;
    String status,description,charges,carno,customer,contact,imageone,imagetwo,imagethree;
    Button saveUpdateBtn;
    String[] statusArray = new String[]{"Pending","Boder Work","Painting","Parts Required","Ready"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_car_edit);
        Bundle receivedCarId = getIntent().getExtras();

        status = receivedCarId.getString("status");
        description = receivedCarId.getString("description");
        charges = receivedCarId.getString("charges");
        carno = receivedCarId.getString("carno");
        customer = receivedCarId.getString("customer");
        contact = receivedCarId.getString("contact");

        imageone = receivedCarId.getString("imageone");
        imagetwo = receivedCarId.getString("imagetwo");
        imagethree = receivedCarId.getString("imagethree");

        Log.d("response CFDA: ",receivedCarId+"");

        //region getviews
        currentStatusSpin = (Spinner) findViewById(R.id.currentStatus_spin);
        commentTV = (TextView) findViewById(R.id.commentTV);
        chargesTV = (TextView) findViewById(R.id.chargesTV);
        carnoTV = (TextView) findViewById(R.id.carnoTV);
        customerTV = (TextView) findViewById(R.id.customerTV);
        cantactTV = (TextView) findViewById(R.id.cantactTV);
        saveUpdateBtn = (Button) findViewById(R.id.saveUpdateCar);

        saveUpdateBtn.setOnClickListener(this);



        //imageviews
        imageOne = (ImageView) findViewById(R.id.imageOne);
        imageTwo = (ImageView) findViewById(R.id.imageTwo);
        imageThree = (ImageView) findViewById(R.id.imageThree);

        //endregion

        //setviews
        //fullShowStatus.setText(status);
        commentTV.setText(description);
        chargesTV.setText(charges);
        carnoTV.setText(carno);
        customerTV.setText(customer);
        cantactTV.setText(contact);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusArray);//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currentStatusSpin.setAdapter(adapter);
        currentStatusSpin.setSelection(0, true);
        View v = currentStatusSpin.getSelectedView();
        ((TextView)v).setTextColor(getResources().getColor(R.color.yellow));




        //Set Images
        if (imageone.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imageone).into(imageOne);
        }
        if (imagetwo.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagetwo).into(imageTwo);
        }
        if (imagethree.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagethree).into(imageThree);
        }

        //hide keyboard
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
