package com.almajedgarage.almajedgarage.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdminRegisterCarActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imageOne,imageTwo,imageThree;
    EditText commentstv,chargestv,carnotv,customertv,contacttv;
    Button savebtn;
    Spinner currentStatusSpin;
    boolean uploadLogo = false,uploadLogoTwo=false,uploadLogoThree=false;
    protected Bitmap newLogo = null,newLogoTwo = null,newLogoThree = null;
    File fileOne,fileTwo,fileThree;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 2;
    int currentImage = 1;
    String status,description,charges,carno,customer,contact,imageone,imagetwo,imagethree;
    String PREFS_NAME = "Camera_AR";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String[] statusArray = new String[]{"Pending","Body Work","Painting","Parts Required","Ready"};
    ProgressDialog progressDialog;
    RequestParams params;
    int care_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_car);


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
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        //endregion
        //region Initializing
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        int pictureNumber = prefs.getInt("picture",0);

        if (pictureNumber == 0){
            editor.putInt("picture", 1);
            editor.commit();
        }
        //endregion
        //region getViews
        imageOne = (ImageView) findViewById(R.id.imageOne);
        imageTwo = (ImageView) findViewById(R.id.imageTwo);
        imageThree = (ImageView) findViewById(R.id.imageThree);
        commentstv = (EditText) findViewById(R.id.commentTV);
        chargestv = (EditText) findViewById(R.id.chargesTV);
        carnotv = (EditText) findViewById(R.id.carnoTV);
        customertv = (EditText) findViewById(R.id.customerTV);
        contacttv = (EditText)findViewById(R.id.cantactTV);
        savebtn = (Button) findViewById(R.id.saveNewCar);

        currentStatusSpin = (Spinner) findViewById(R.id.currentStatus_spin);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, statusArray);//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




        currentStatusSpin.setAdapter(adapter);
        currentStatusSpin.setSelection(0, true);
        View v = currentStatusSpin.getSelectedView();
        ((TextView)v).setTextColor(getResources().getColor(R.color.yellow));

        //implement OnClick Listeners
        imageOne.setOnClickListener(this);
        imageTwo.setOnClickListener(this);
        imageThree.setOnClickListener(this);
        savebtn.setOnClickListener(this);

        //endregion

        try{
            //region Receive data from Intent
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
            care_id = receivedCarId.getInt("id");
            if (care_id > 0){
                savebtn.setText("Update Record");
            }
            //endregion
            //Region set Values in Edit case
            //fullShowStatus.setText(status);
            commentstv.setText(description);
            chargestv.setText(charges);
            carnotv.setText(carno);
            customertv.setText(customer);
            contacttv.setText(contact);


            ArrayAdapter<String> adaptert = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, statusArray);//setting the country_array to spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            currentStatusSpin.setAdapter(adaptert);
            currentStatusSpin.setSelection(0, true);
            View vv = currentStatusSpin.getSelectedView();
            ((TextView)vv).setTextColor(getResources().getColor(R.color.yellow));

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
            //endregion
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void saveData() {

        description = commentstv.getText().toString();
        charges = chargestv.getText().toString();
        carno = carnotv.getText().toString();
        customer = customertv.getText().toString();
        contact = contacttv.getText().toString();
        status = currentStatusSpin.getSelectedItem().toString();

        if (description.length()==0 || charges.length() == 0 || carno.length() == 0 || customer.length() == 0 || contact.length() == 0 || status.length()==0){
            Toast.makeText(getApplicationContext(),"Please fill all fields. ",Toast.LENGTH_LONG).show();
            return;
        }


        params = new RequestParams();
        params.put("contact",contact);
        params.put("customer",customer);
        params.put("carno",carno);
        params.put("charges",charges);
        params.put("comments",description);
        params.put("status",status);

        if (uploadLogo){
            try{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                newLogo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                byte[] myByteArray = out.toByteArray();
                params.put("imageone", new ByteArrayInputStream(myByteArray), "newlogo.png");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (uploadLogoTwo){
            try{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                newLogoTwo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                byte[] myByteArray = out.toByteArray();
                params.put("imagetwo", new ByteArrayInputStream(myByteArray), "newlogo.png");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (uploadLogoThree){
            try{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                newLogoThree.compress(Bitmap.CompressFormat.JPEG, 100, out);
                byte[] myByteArray = out.toByteArray();
                params.put("imagethree", new ByteArrayInputStream(myByteArray), "newlogo.png");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        serverRequest();

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    private void serverRequest() {

        Log.d("response in serverreq: ","Saving data..");
        if (isNetworkAvailable()==false){
            Toast.makeText(getApplicationContext(),"Network Unavailable!",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url;
        if (savebtn.getText().toString().trim().toLowerCase().equals("update record")){
            url = GlobCar.base_url+GlobCar.update_car;
            params.add("id", String.valueOf(care_id));
        } else {
            url = GlobCar.base_url+GlobCar.add_car;
        }
        Log.d("resp",url);
        client.post(getApplicationContext(),url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d("response: ",response.toString());
                if (response.optString("success").equals("false")){
                    Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Data Successfully Saved",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),AdminAllCarsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("response failure:  ",responseString);
                Toast.makeText(getApplicationContext(),"Network Problem!!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    //region Clicks
    @Override
    public void onClick(View view) {
        if (imageOne==view){
            Log.d("ARCA imageone","imageone clicked");
            chooseImage(1);
        } else if (imageTwo == view){
            chooseImage(2);
        } else if (imageThree == view){
            chooseImage(3);
        } else if (savebtn == view){
            saveData();
        }
    }
    //endregion
    //region choose Image
    private void chooseImage(int imageNumber) {

        currentImage = imageNumber;

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                     bm = Bitmap.createScaledBitmap(bm, 500, 500, true);

                    if (currentImage == 1){
                        newLogo = bm;
                        imageOne.setImageBitmap(bm);
                        uploadLogo = true;
                    } else if (currentImage == 2){
                        newLogoTwo = bm;
                        imageTwo.setImageBitmap(bm);
                        uploadLogoTwo = true;
                    } else if(currentImage == 3){
                        newLogoThree = bm;
                        imageThree.setImageBitmap(bm);
                        uploadLogoThree = true;
                    }


                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";

                    Log.d("image path: ",path);

                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");

                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

                        if (currentImage == 1){
                            newLogo = bm;
                            uploadLogo = true;
                        } else if (currentImage == 2){
                            newLogoTwo = bm;
                            uploadLogoTwo = true;
                        } else if(currentImage == 3){
                            newLogoThree = bm;
                            uploadLogoThree = true;
                        }
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                f.delete();
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

                if (currentImage == 1){
                    newLogo = bm;
                    imageOne.setImageBitmap(bm);
                    uploadLogo = true;
                } else if (currentImage == 2){
                    newLogoTwo = bm;
                    imageTwo.setImageBitmap(bm);
                    uploadLogoTwo = true;
                } else if (currentImage == 3){
                    newLogoThree = bm;
                    imageThree.setImageBitmap(bm);
                    uploadLogoThree = true;
                }

            }
        }
    }
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //endregion
}
