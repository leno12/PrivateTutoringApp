package com.example.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    List<MyDataList>myDataLists;
    Context context;

    public MyAdapter(List<MyDataList> myDataLists, Context context) {
        this.myDataLists = myDataLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDataList myDataList=myDataLists.get(position);
        Glide.with(context)
                .load(myDataList.getImageUrl())
                .into(holder.imageView);
        holder.course_name.setText(myDataList.getCourse_name());
        holder.tutor.setText("Tutor: " + myDataList.getTutor());
        holder.avg_rating.setText("Avg Rating: " + myDataList.getAvg_rating());

    }

    @Override
    public int getItemCount() {
        return myDataLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView course_name;
        private TextView tutor;
        private TextView avg_rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image_view);
            course_name = itemView.findViewById(R.id.course_list_name);
            tutor = itemView.findViewById(R.id.tutor);
            avg_rating = itemView.findViewById(R.id.avg_rating);

        }
    }
}