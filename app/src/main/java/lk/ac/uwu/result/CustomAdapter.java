package lk.ac.uwu.result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;  //For refreshing page
    private ArrayList id, year, semester, course_name, course_code, course_grade;

    Animation translate_anim;   //Animation object

    // CUSTOM ADAPTER CONSTRUCTOR
    CustomAdapter(Activity activity, Context context, ArrayList id, ArrayList year, ArrayList semester,
                  ArrayList course_name, ArrayList course_code, ArrayList course_grade){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.year = year;
        this.semester = semester;
        this.course_name = course_name;
        this.course_code = course_code;
        this.course_grade = course_grade;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
        //Set the text fields to be displayed using holder object
        holder.id_txt.setText(String.valueOf(id.get(position)));        //Get the string from array
        holder.year_txt.setText(String.valueOf(year.get(position)));
        holder.semester_txt.setText(String.valueOf(semester.get(position)));
        holder.course_name_txt.setText(String.valueOf(course_name.get(position)));
        holder.course_code_txt.setText(String.valueOf(course_code.get(position)));
        holder.course_grade_txt.setText(String.valueOf(course_grade.get(position)));


        //onclicklistener for linear layout in my_row.xml (click on record to edit)
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to update activity after click on recycler view item
                Intent intent = new Intent(context, UpdateActivity.class);

                //Transfer and store data which is in resultList to new update intent - (to show already added details)
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("year", String.valueOf(year.get(position)));
                intent.putExtra("sem", String.valueOf(semester.get(position)));
                intent.putExtra("name", String.valueOf(course_name.get(position)));
                intent.putExtra("code", String.valueOf(course_code.get(position)));
                intent.putExtra("grade", String.valueOf(course_grade.get(position)));

                //context.startActivity(intent);
                activity.startActivityForResult(intent, 1);     //Use this method for auto refresh after change
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    //Inner Class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        //Text view objects (to display)
        TextView id_txt, year_txt, semester_txt, course_name_txt, course_code_txt, course_grade_txt;

        // Linear layout in my_row.xml
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id_txt = itemView.findViewById(R.id.id_txt);
            year_txt = itemView.findViewById(R.id.year_txt);
            semester_txt = itemView.findViewById(R.id.semester_txt);
            course_name_txt = itemView.findViewById(R.id.course_name_txt);
            course_code_txt = itemView.findViewById(R.id.course_code_txt);
            course_grade_txt = itemView.findViewById(R.id.course_grade_txt);

            mainLayout = itemView.findViewById(R.id.mainLayout);

            //Load animation from resource directory
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
