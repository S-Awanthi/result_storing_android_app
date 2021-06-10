package lk.ac.uwu.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainGrid = findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item in main grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //Cast object to cardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int selected = i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected == 0) {
                        Intent intent = new Intent(HomeActivity.this, ResultListActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                        startActivity(intent);
                    }
//                    else {
//                        StyleableToast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT, R.style.mytoastfail).show();
//                    }
                }
            });
        }
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
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
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