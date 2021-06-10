package lk.ac.uwu.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AddActivity extends AppCompatActivity {

    EditText year_input, semester_input, course_name_input, course_code_input, course_grade_input;
    Button save_button, view_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        year_input = findViewById(R.id.year_input);
        semester_input = findViewById(R.id.semester_input);
        course_name_input = findViewById(R.id.course_name_input);
        course_code_input = findViewById(R.id.course_code_input);
        course_grade_input = findViewById(R.id.course_grade_input);
        save_button = findViewById(R.id.save_button);
        view_button = findViewById(R.id.view_button);


        //************************ SAVE BUTTON PERFORMANCE ********************
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);

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



                //Insert
                myDB.addResult(
                        Integer.valueOf(year_input.getText().toString().trim()),
                        Integer.valueOf((semester_input.getText().toString().trim())),
                        course_name_input.getText().toString().trim(),
                        course_code_input.getText().toString().trim(),
                        course_grade_input.getText().toString().trim()
                );
//                Intent intent = new Intent(AddActivity.this, AddActivity.class);
//                startActivity(intent);
//                finish();

                year_input.setText("");
                semester_input.setText("");
                course_name_input.setText("");
                course_code_input.setText("");
                course_grade_input.setText("");
            }
        });



        //************************ VIEW BUTTON PERFORMANCE ********************
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, ResultListActivity.class);
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(AddActivity.this, MainActivity.class));
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