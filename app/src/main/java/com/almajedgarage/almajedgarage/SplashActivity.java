package com.almajedgarage.almajedgarage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.admin.AdminLogin;
import com.almajedgarage.almajedgarage.admin.AdminMenu;
import com.almajedgarage.almajedgarage.admin.ManageAdmins;
import com.google.android.gms.maps.GoogleMap;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SplashActivity";
    GlobalData globalData;
    boolean userLoggingStatus = false;
    Button loginBtn,logoutBtn,addNewAdminBtn,manageAdminsBtn,adminSettingsBtn;
    String type = "2";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        //getView
        loginBtn = (Button) findViewById(R.id.adminLoginBtn);
        logoutBtn = (Button) findViewById(R.id.adminLogoutBtn);
        logoutBtn.setOnClickListener(this);
        addNewAdminBtn = (Button) findViewById(R.id.addNewAdminBtn);
        manageAdminsBtn = (Button) findViewById(R.id.manageAdminsBtn);
        adminSettingsBtn = (Button) findViewById(R.id.adminSettingsBtn);


        loggedInLayoutChanges();
        GlobCar.userToString(context);
    }

    private void loggedInLayoutChanges() {
        //Our Global Class Reference
        globalData = new GlobalData(getApplicationContext());
        userLoggingStatus = GlobCar.isLoggedIn(SplashActivity.this);
        type = GlobCar.adminType(SplashActivity.this);

        if (userLoggingStatus == true){
            Log.d(TAG+"userlogin",userLoggingStatus+"");
            //User already Logged in
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            adminSettingsBtn.setVisibility(View.VISIBLE);
            if (type.equals("2")){
                addNewAdminBtn.setVisibility(View.GONE);
                manageAdminsBtn.setVisibility(View.GONE);
            } else{
                addNewAdminBtn.setVisibility(View.VISIBLE);
                manageAdminsBtn.setVisibility(View.VISIBLE);
            }
        }else {
            hideAdminSections();
        }
    }

    public void hideAdminSections(){
        addNewAdminBtn.setVisibility(View.GONE);
        manageAdminsBtn.setVisibility(View.GONE);
        adminSettingsBtn.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.GONE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        loggedInLayoutChanges();
        GlobCar.userToString(context);
    }

    public void gotoSearchActivity(View view){
        startActivity(new Intent(getApplicationContext(),LandingActivity.class));
    }
    public void gotoAdminLogin(View view){
        if (userLoggingStatus==false){
            startActivity(new Intent(getApplicationContext(),AdminLogin.class));
        }else{
            startActivity(new Intent(getApplicationContext(),AdminMenu.class));
        }
    }

    public void addNewAdmin(View view){
        Intent intent = new Intent(SplashActivity.this,AdminLogin.class);
        intent.putExtra("addNewAdmin","yes");
        startActivity(intent);
    }

    public void manageAdmins(View v){
        startActivity(new Intent(getApplicationContext(),ManageAdmins.class));
    }
    public void adminAccountSettings(View view){
        Intent intent = new Intent(SplashActivity.this,AdminLogin.class);
        intent.putExtra("addNewAdmin","changepassword");
        startActivity(intent);
    }

    public void showAboutus(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.drawable.contact);
        builder.setTitle("Almajed Son's Garage");
        builder.setMessage("ALMAJED Son's Garage is a new garage founded in 2017 located in Salmabad, Bahrain. We have gathered all our experience in this field that we inherited which goes back to 1974 Specialized in Body Woks and Painting.");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showContact(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(R.drawable.contact);
        builder.setTitle("Contact Info");
        builder.setMessage("\t\tOffice: 77077792\n" +
                "\tGeneral Inquiries: 39225219\n" +
                "\tInsurance Inquiries: 39197700\n" +
                "Email: Almajed.Garage@gmail.com\n" +
                "Website: www.almajed-garage.com\n");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void locationShow(View view){
        askPermissions();
        Intent intent = new Intent(SplashActivity.this,MapsActivity.class);
        startActivity(intent);
    }

    public void askPermissions(){
        //region Permissions

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(AdminRegisterCarActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(AdminRegisterCarActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        //request permissions
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
    @Override
    public void onClick(View view) {
        if (view == logoutBtn){
            GlobCar.logOut(SplashActivity.this);
            loginBtn.setVisibility(view.VISIBLE);
            logoutBtn.setVisibility(view.GONE);
            loggedInLayoutChanges();
            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
        }
    }
}
