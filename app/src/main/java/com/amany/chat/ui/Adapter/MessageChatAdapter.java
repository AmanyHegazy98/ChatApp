package com.amany.chat.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.amany.chat.R;
import com.amany.chat.data.model.MainChatModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.viewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<MainChatModel> mChat;
    private String imageUrl;

    FirebaseUser fUser;
    private boolean checkRightOrLeft = false;



    public MessageChatAdapter(Context context, List<MainChatModel> mChat, String imageUrl) {
        this.context = context;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }


    @NonNull
    @Override
    public MessageChatAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            checkRightOrLeft = false;

        } else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            checkRightOrLeft = true;

        }
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageChatAdapter.viewHolder holder, int position) {


        MainChatModel chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        String upload= chat.getUpload();
        String file=chat.getFile();
        if (checkRightOrLeft) {



            if (imageUrl.equals("default")) {
                holder.profile_image.setImageResource(R.drawable.images);

            } else {
                Glide.with(context).load(imageUrl).into(holder.profile_image);
            }
        }
        if (position == mChat.size() - 1) {
            if (chat.getIsSeen().equals("true")) {
                holder.text_seen.setText("seen");
            } else {
                holder.text_seen.setText("Delivered");
            }


        } else {
            holder.text_seen.setVisibility(View.GONE);

        }
        String dateString = DateFormat.format("h:mm a", new Date(chat.getTime())).toString();

        holder.time.setText(dateString);


        if(!upload.equals("")){
            holder.upload_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(upload).into(holder.upload_image);
        }
        if (!file.equals("")){
            holder.constraintLayout.setVisibility(View.VISIBLE);
            holder.fileName.setText(mChat.get(position).getFileName());





        }
        holder.constraintLayout.setOnClickListener(v -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(file));
            context.startActivity(browserIntent);
        });


    }

    @Override
    public int getItemCount() {

        return mChat != null ? mChat.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView show_message,fileName;
        private ImageView profile_image,upload_image,fileImage;
        public TextView text_seen,time;
        ConstraintLayout constraintLayout;



        public viewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image1);
            text_seen = itemView.findViewById(R.id.seen);
            time=itemView.findViewById(R.id.textView13);
            upload_image=itemView.findViewById(R.id.imageView19);
            fileName=itemView.findViewById(R.id.file_name);
            constraintLayout=itemView.findViewById(R.id.constraintFile);
            fileImage=itemView.findViewById(R.id.imageView20);


        }
    }


    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
