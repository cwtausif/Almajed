package com.almajedgarage.almajedgarage.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.almajedgarage.almajedgarage.SplashActivity;

public class AdminMenu extends AppCompatActivity implements View.OnClickListener{
    Button addNewCarBtn,logoutBtn,allCarsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        //getViews
        addNewCarBtn = (Button) findViewById(R.id.addNewCar_btn);
        allCarsBtn = (Button) findViewById(R.id.viewAllCars_btn);
        logoutBtn = (Button) findViewById(R.id.logout_btn);

        addNewCarBtn.setOnClickListener(this);
        allCarsBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        if(GlobCar.isLoggedIn(AdminMenu.this)==false){
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        if (view==addNewCarBtn){
            Intent intent1 = new Intent(getApplicationContext(),AdminRegisterCarActivity.class);
            startActivity(intent1);
        }else if (view==allCarsBtn){
            Intent intent2 = new Intent(getApplicationContext(),AdminAllCarsActivity.class);
            startActivity(intent2);
        }else if(view == logoutBtn){
            GlobCar.logOut(AdminMenu.this);
            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(intent3);
            finish();
        }
    }
}
