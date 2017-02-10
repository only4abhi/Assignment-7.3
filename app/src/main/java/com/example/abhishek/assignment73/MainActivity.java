package com.example.abhishek.assignment73;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper mydb;
    EditText editName,editAge;
    ImageView imageView;
    Button btnAddData;
    private static final int SELECT_PICTURE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editAge = (EditText)findViewById(R.id.editText_age);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnAddData = (Button)findViewById(R.id.button_add);
        AddData();
    }

    public void AddData(){

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);


               // byte[] image = new byte[imageView.];
               //
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    try {
                        mydb.open();
                        InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                        byte[] inputData = Utils.getBytes(iStream);
                        boolean isInserted  = mydb.insertData(editName.getText().toString(),editAge.getText().toString(),inputData);
                        if(isInserted == true){
                            Toast.makeText(MainActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                        }

                        mydb.close();
                    }catch (Exception e){}

                    //Log.i("MainActivity", "Image Path : " + path);
                    // Set the image in ImageView
                   //imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
