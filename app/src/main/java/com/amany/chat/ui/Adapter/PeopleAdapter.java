package com.amany.chat.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amany.chat.ui.activities.MainChatActivity;
import com.amany.chat.R;
import com.amany.chat.data.model.UserModel;
import com.amany.chat.ui.activities.ProfileActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.viewHolder> {

    private Context context;
    private List<UserModel> userModelList;

    public PeopleAdapter(Context context,List<UserModel>mUser) {
        this.context = context;
        this.userModelList =mUser;
    }

    @NonNull
    @Override
    public PeopleAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_people_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.viewHolder holder, int position) {

        UserModel userModel= userModelList.get(position);
        holder.name.setText(userModel.getName());
        holder.job.setText(userModel.getJob());
        holder.online.setText(userModel.getStatus());
        if(userModel.getStatus().equals("online"))

        {
            //Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_1)).into(holder.imageActive);
            holder.imageActive.setImageResource(R.drawable.ic_ellipse_1);

        }else{
            holder.imageActive.setImageResource(R.drawable.ic_ellipse_4);

           // Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_4)).into(holder.imageActive);
        }



        holder.name.setText(userModel.getName());
        if(userModel.getImage().equals("default")){
            holder.imagePerson.setImageResource(R.drawable.images);

        }else {
            Glide.with(context).load(userModel.getImage()).into(holder.imagePerson);

        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, ProfileActivity.class);
            intent.putExtra("id",userModel.getId());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return
                userModelList!=null?userModelList.size():0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView name,job,online;
        private ImageView imagePerson,imageActive;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView);
            job=itemView.findViewById(R.id.textView2);
            online=itemView.findViewById(R.id.textView10);
            imagePerson=itemView.findViewById(R.id.imageView4);
            imageActive=itemView.findViewById(R.id.imageView9);

        }
    }
}
