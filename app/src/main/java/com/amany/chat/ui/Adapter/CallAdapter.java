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

import com.amany.chat.R;
import com.amany.chat.data.model.UserModel;
import com.amany.chat.ui.activities.ProfileActivity;
import com.amany.chat.ui.activities.VoiceChat;
import com.bumptech.glide.Glide;

import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.viewHolder>{



    private Context context;
    private List<UserModel> userModelList;

    public CallAdapter(Context context,List<UserModel>mUser) {
        this.context = context;
        this.userModelList =mUser;
    }


    @NonNull
    @Override
    public CallAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_call_items,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.viewHolder holder, int position) {
        UserModel user= userModelList.get(position);
        holder.name.setText(user.getName());
        holder.name.setText(user.getName());
        if(user.getImage().equals("default")){
            holder.personPhoto.setImageResource(R.drawable.images);

        }else {
            Glide.with(context).load(user.getImage()).into(holder.personPhoto);

        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, VoiceChat.class);
          //  intent.putExtra("id",user.getId());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return userModelList!=null?userModelList.size():0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView name,time,status;
        ImageView personPhoto,statusIcon,callIcon;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView);
            time=itemView.findViewById(R.id.textView2);
            status=itemView.findViewById(R.id.textView10);
            personPhoto=itemView.findViewById(R.id.imageView4);
            statusIcon=itemView.findViewById(R.id.imageView9);
            callIcon=itemView.findViewById(R.id.imageView10);


        }
    }
}
