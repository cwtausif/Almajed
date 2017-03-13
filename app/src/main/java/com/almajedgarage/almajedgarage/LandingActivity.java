package com.almajedgarage.almajedgarage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.admin.AdminLogin;
import com.almajedgarage.almajedgarage.customer.CustomerSearchActivity;


public class LandingActivity extends AppCompatActivity implements View.OnClickListener {
    //region variables
    EditText mobileNumberTV,carNumberTv;
    Button adminBtn,searchNowBtn;
    String searchText;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_search);

        //region getViews

        mobileNumberTV = (EditText) findViewById(R.id.mobile_number_tv);
        carNumberTv = (EditText) findViewById(R.id.car_number_tv);


        adminBtn = (Button) findViewById(R.id.admin_btn);
        searchNowBtn = (Button) findViewById(R.id.searchNow_btn);

        adminBtn.setOnClickListener(this);
        searchNowBtn.setOnClickListener(this);
        //endregion
    }




    @Override
    public void onClick(View view) {
        if (view==adminBtn){
            Intent intent = new Intent(getApplicationContext(),AdminLogin.class);
            startActivity(intent);
        }else if(view == searchNowBtn){
            hideKeyboard();
            searchText = mobileNumberTV.getText().toString();
            searchText = searchText.trim();

            if (searchText.length()==0){
                searchText = carNumberTv.getText().toString();
            }

            if (searchText.length()==0){
                Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                return;
            }


            Intent intent = new Intent(getApplicationContext(),CustomerSearchActivity.class);
            intent.putExtra("search",searchText);
            startActivity(intent);

        }
    }

    private void hideKeyboard() {
        View view= this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

