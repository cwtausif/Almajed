package com.almajedgarage.almajedgarage.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AdminLogin";
    Button adminLoginBtn;
    EditText admin_email,admin_password;
    String email,password;
    HashMap<String,String> savedAccounts;
    TextView adminLogin,userNameUpdate;
    Bundle extras;
    ProgressDialog pDialog;
    Context context;
    String registernewadmin = "Register New Admin",changepassword="Change Password";
    CheckBox isSuperAdminCB;
    String adminType = "2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        context = AdminLogin.this;

        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Fetching data..");

        adminLoginBtn = (Button) findViewById(R.id.admin_login);
        adminLoginBtn.setOnClickListener(this);

        admin_email = (EditText) findViewById(R.id.admin_email);
        admin_password = (EditText) findViewById(R.id.admin_password);
        adminLogin = (TextView) findViewById(R.id.adminLogin);
        isSuperAdminCB = (CheckBox) findViewById(R.id.isSuperAdminCB);
        userNameUpdate = (TextView) findViewById(R.id.userNameUpdate);
        userNameUpdate.setVisibility(View.GONE);
        extras = getIntent().getExtras();
        try{
            if (extras.size()>0){
                String registerAdmin = extras.getString("addNewAdmin",null);
                if (registerAdmin != null && registerAdmin.equals("yes")){
                    adminLogin.setText(registernewadmin);
                    adminLoginBtn.setText("Create Account");
                    isSuperAdminCB.setVisibility(View.VISIBLE);
                } else if (registerAdmin != null && registerAdmin.equals("changepassword")){
                    adminLogin.setText(changepassword);
                    admin_email.setText(GlobCar.adminUsername(context));
                    admin_email.setFocusable(false);
                    admin_email.setFocusableInTouchMode(false);
                    admin_email.setClickable(false);
                    admin_password.setHint("Enter New Password");
                    admin_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    adminLoginBtn.setText("Update");
                    userNameUpdate.setVisibility(View.VISIBLE);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
     if (view == adminLoginBtn){
            checkValidation();
     }
    }
    private void checkValidation() {
        email = admin_email.getText().toString();
        password = admin_password.getText().toString();
        email = email.trim();
        password = password.trim();
        if (email.length() ==0 || password.length() == 0){
            Toast.makeText(getApplicationContext(),"Fields can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.length() < 4 || password.length() < 4){
            Toast.makeText(getApplicationContext(),"Minimum length 4 characters",Toast.LENGTH_LONG).show();
            return;
        }


        serverRequest(email,password);
        Log.d(TAG,"serverreqeust");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

    private void serverRequest(final String username, String password) {

        pDialog.show();
        Log.d(TAG+"username",username);
        Log.d(TAG+"password",password);

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username",username);
        paramMap.put("password",password);
        String buttonText = adminLoginBtn.getText().toString();
        try{
            if (adminLoginBtn.getText().equals("Create Account")==true) {
                paramMap.put("register", "true");
                //Check Admin Type
                if (isSuperAdminCB.isChecked()){
                    adminType = "1";
                }
                paramMap.put("type", adminType);
            }else if(buttonText.equalsIgnoreCase("Update")== true){
                paramMap.put("update", "true");
                paramMap.put("id",GlobCar.adminUserId(context));
            }else{
                paramMap.put("login", "true");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
                        Toast.makeText(getApplicationContext(),response.optString("message"),Toast.LENGTH_SHORT).show();
                        if (response.optString("message").trim().equals("Password Updated Successfully")){
                            finish();
                            return;
                        }
                        if (response.optString("message").equals("User Registered Successfully")){
                            Intent intent = new Intent(getApplicationContext(),ManageAdmins.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        JSONObject user = response.getJSONObject("user");
                        Log.d(TAG+"user",user.toString());
                        GlobCar.setPreferencesCar(AdminLogin.this,user.getString("id"),user.getString("username"),user.getString("password"),user.getString("type"),true);
                        Intent intent = new Intent(getApplicationContext(),AdminMenu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),response.optString("message"),Toast.LENGTH_SHORT).show();
                    }
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
