package lk.ac.uwu.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoast.StyleableToast;

public class UpdateActivity extends AppCompatActivity {

    EditText year_input, semester_input, course_name_input, course_code_input, course_grade_input;
    Button update_button, delete_button;

    //For updating and deleting
    String id, year, sem, name, code, grade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        year_input = findViewById(R.id.year_input2);
        semester_input = findViewById(R.id.semester_input2);
        course_name_input = findViewById(R.id.course_name_input2);
        course_code_input = findViewById(R.id.course_code_input2);
        course_grade_input = findViewById(R.id.course_grade_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //1. Call to get and set method
        getAndSetIntentData();


        // SET COURSE CODE AS ACTION BAR TITLE AFTER getAndSetIntentData() METHOD CALL
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(code);
        }


        //************************ UPDATE BUTTON PERFORMANCE ********************
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);

                year = year_input.getText().toString().trim();
                sem = semester_input.getText().toString().trim();
                name = course_name_input.getText().toString().trim();
                code = course_code_input.getText().toString().trim();
                grade = course_grade_input.getText().toString().trim();

//                Input Validations
                if (year_input.length() < 1) {
                    year_input.setError("Academic Year is Required!");
                    year_input.requestFocus();
                    return;
                }
                if (semester_input.length() < 1) {
                    semester_input.setError("Semester is Required!");
                    semester_input.requestFocus();
                    return;
                }
                if (course_name_input.length() < 1) {
                    course_name_input.setError("Course Name is Required!");
                    course_name_input.requestFocus();
                    return;
                }
                if (course_code_input.length() < 1) {
                    course_code_input.setError("Course Code is Required!");
                    course_code_input.requestFocus();
                    return;
                }
                if (course_grade_input.length() < 1) {
                    course_grade_input.setError("Course Grade is Required!");
                    course_grade_input.requestFocus();
                    return;
                }

                //2. Call to update method
                myDB.updateData(id, year, sem, name, code, grade);
            }
        });



        //************************ DELETE BUTTON PERFORMANCE ********************
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
//                myDB.deleteOneRow(id);

                //Call for confirmation method
                confirmDialog();
            }
        });
    }

    // ***************************** GET AND SET DATA WHICH IS PARSED FROM RESULTLISTACTIVITY *************************************
    private void getAndSetIntentData() {
        //If data available after transfered from recycler view
        if (getIntent().hasExtra("id") && getIntent().hasExtra("year") && getIntent().hasExtra("sem") &&
                getIntent().hasExtra("name") && getIntent().hasExtra("code") && getIntent().hasExtra("grade")){

            //Getting data from intent and Store inside Strings
            id = getIntent().getStringExtra("id");
            year = getIntent().getStringExtra("year");
            sem = getIntent().getStringExtra("sem");
            name = getIntent().getStringExtra("name");
            code = getIntent().getStringExtra("code");
            grade = getIntent().getStringExtra("grade");

            //Setting intent data
            year_input.setText(year);
            semester_input.setText(sem);
            course_name_input.setText(name);
            course_code_input.setText(code);
            course_grade_input.setText(grade);
        }
        else{
            StyleableToast.makeText(this, "No Data Available!", Toast.LENGTH_SHORT, R.style.myToastFail).show();
        }
    }

    //******************** METHOD FOR DELETE CONFIRM DIALOG *********************
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure to delete " + code + " ?");

        //Set two buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                //Call to delete method
                myDB.deleteOneRow(id);

                finish();   //This method automatically navigate to parent page after click "Yes"
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();        //Display alert dialog
    }


    //******************* FOR LOGOUT FUNCTION *********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu_home, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {      //After click on an option

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
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
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