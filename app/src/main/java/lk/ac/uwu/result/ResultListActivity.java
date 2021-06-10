package lk.ac.uwu.result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    ImageView empty_image;
    TextView empty_text;

    MyDatabaseHelper myDB;  //Declaration of MyDatabaseHelper class
    ArrayList<String> id, year, semester, course_name, course_code, course_grade;

    CustomAdapter customAdapter;        //Declare adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);

        //************************ FLOATING  BUTTON PERFORMANCE ********************
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultListActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(ResultListActivity.this);   //db initialization

        id = new ArrayList<>();
        year = new ArrayList<>();
        semester = new ArrayList<>();
        course_name = new ArrayList<>();
        course_code = new ArrayList<>();
        course_grade = new ArrayList<>();

        //Call to the store data method
        storeDataInArrays();


        // ********** this is for display data in recycler view *********
        customAdapter = new CustomAdapter(ResultListActivity.this,this, id, year, semester, course_name, course_code, course_grade); //Initialize adapter using array

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResultListActivity.this));
    }



    // *********************** METHOD TO STORE READ DATA ********************
    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0){
//            StyleableToast.makeText(this, "No results available!", Toast.LENGTH_SHORT, R.style.mytoastfail).show();

            //Make empty_image and text visible if there is no data
            empty_image.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.VISIBLE);
        }
        else{
            while (cursor.moveToNext()){
                //Read all the data using cursor
                id.add(cursor.getString(0));
                year.add(cursor.getString(1));
                semester.add(cursor.getString(2));
                course_name.add(cursor.getString(3));
                course_code.add(cursor.getString(4));
                course_grade.add(cursor.getString(5));
            }

            //Make empty_image and text gone if there is available data
            empty_image.setVisibility(View.GONE);
            empty_text.setVisibility(View.GONE);
        }
    }

    //Override method for page refreshing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            recreate();     //Do refresh
        }
    }

    //******************* FOR DELETE ALL and LOGOUT FUNCTION *********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {      //After click on an option

        if (item.getItemId() == R.id.delete_all){
//            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
            confirmDialog();
        }
        if (item.getItemId() == R.id.menuLogout) {
            logoutConfirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    //******************** METHOD FOR LOGOUT CONFIRM DIALOG *********************
    private void logoutConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to logout?");

        //Set two buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ResultListActivity.this, MainActivity.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();        //Display alert dialog
    }



    //******************** METHOD FOR DELETE_ALL CONFIRM DIALOG *********************
    private void confirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure to delete all results permanently?");

        //Set two buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ResultListActivity.this);
                myDB.deleteAllData();

                //Refresh activity
                Intent intent = new Intent(ResultListActivity.this, ResultListActivity.class);
                startActivity(intent);
                finish();

                //recreate();     //Or use this only after calling deleteAllData   (Show immediately the data list)
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();        //Display alert dialog
    }

}