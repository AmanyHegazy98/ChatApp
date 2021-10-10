package com.amany.chat.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amany.chat.ui.activities.MainChatActivity;
import com.amany.chat.R;
import com.amany.chat.data.model.ChatModel;
import com.amany.chat.data.model.MainChatModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {

    private Context context;
    private List<ChatModel> chatModelList;
    String theLastMassage;
   Long theDate;
    String  photo;
    String  file;

    public MessageAdapter(Context context,List<ChatModel> chatModelList) {

        this.context = context;
        this.chatModelList=chatModelList;
    }
    @NonNull
    @Override
    public MessageAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.viewHolder holder, int position) {

        ChatModel chatModel=chatModelList.get(position);
        holder.name.setText(chatModel.getName());





        if(chatModel.getImage().equals("default")){
            holder.profileImage.setImageResource(R.drawable.images);

        }else {
            Glide.with(context).load(chatModel.getImage()).into(holder.profileImage);

        }
        lastMessage(chatModel.getId(), holder.message,holder.date);


        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context,MainChatActivity.class);
            intent.putExtra("id",chatModelList.get(position).getId());
            context.startActivity(intent);
        });






    }

    @Override
    public int getItemCount() {

        return chatModelList!=null?chatModelList.size():0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView message,name,date;
        private ImageView profileImage;


        public viewHolder(@NonNull View itemView) {

            super(itemView);


            name=itemView.findViewById(R.id.textView);
            profileImage=itemView.findViewById(R.id.profile_image);
            message=itemView.findViewById(R.id.message);
            date=itemView.findViewById(R.id.textView7);

        }
    }
    private void lastMessage( String userid, TextView message ,TextView date){
        theLastMassage="default";
          photo="photo";
          file="file";


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    MainChatModel chat=snapshot1.getValue(MainChatModel.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&& chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMassage = chat.getMessage();
                        theDate = chat.getTime();
                        message.setText(theLastMassage);
                        if (!chat.getFile().equals("")) {
                            message.setText("file");
                        } else if (! chat.getUpload().equals("")) {
                            message.setText("photo");
                        }else if (theLastMassage.equals("default"))
                        {
                            message.setText("No Message");
                        }



                    }

                }






                String dataString= DateFormat.format("MMM d \nh:mm a", new Date(theDate)).toString();
                date.setText(dataString);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
