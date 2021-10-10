package com.amany.chat.ui.activities;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amany.chat.R;
import com.amany.chat.data.Notifications.APIService;
import com.amany.chat.data.Notifications.CallData;
import com.amany.chat.data.Notifications.Client;
import com.amany.chat.data.Notifications.Data;
import com.amany.chat.data.Notifications.MyResponse;
import com.amany.chat.data.Notifications.Sender;
import com.amany.chat.data.Notifications.SenderCall;
import com.amany.chat.data.Notifications.Token;
import com.amany.chat.ui.Adapter.MessageChatAdapter;
import com.amany.chat.data.model.MainChatModel;
import com.amany.chat.data.model.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainChatActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView cardImage, sendImage, photo_status, back, sendImageToChat, imageViewSend, camera, file, emoji, pdf,phone_call,video_call;
    private TextView name, status, file_name;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private Intent intent;
    private EditText editText ;
    private String userid;
    List<MainChatModel> mChat;
    RecyclerView recyclerView;
    private ValueEventListener seenListener;
    APIService apiService;
    boolean notify = false;
    StorageReference storageReference;
    private Uri imageUri;
    private Uri fileUri;
    private StorageTask uploadTask;
    private static final int IMAGE_REQUEST = 1;
    Uri downloadUriFile, downloadUri;
    String msg;
    ConstraintLayout constraintFile,rootView;
    UserModel myData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        cardImage = findViewById(R.id.imageView8);
        name = findViewById(R.id.textView8);
        sendImage = findViewById(R.id.imageView18);
        editText = findViewById(R.id.editText1);
        status = findViewById(R.id.textView55);
        photo_status = findViewById(R.id.imageView16);
        back = findViewById(R.id.imageView6);
        sendImageToChat = findViewById(R.id.image_setting);
        imageViewSend = findViewById(R.id.image);
        camera = findViewById(R.id.image_calls);
        file = findViewById(R.id.image_groups);
        constraintFile = findViewById(R.id.constraintFile);
        emoji = findViewById(R.id.image_chat);
        file_name = findViewById(R.id.titleFile);
        rootView=findViewById(R.id.rootView);
        pdf = findViewById(R.id.imageView20);
        phone_call = findViewById(R.id.phone_call);
        video_call = findViewById(R.id.video_call);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sendImage.setOnClickListener(this);
        back.setOnClickListener(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        intent = getIntent();
        userid = intent.getStringExtra("id");
        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                UserModel userModel = snapshot.getValue(UserModel.class);
                name.setText(userModel.getName());


                if (userModel.getImage().equals("default")) {
                    cardImage.setImageResource(R.drawable.images);

                } else {
                    Glide.with(MainChatActivity.this).load(userModel.getImage()).into(cardImage);
                }
                readMessage(firebaseUser.getUid(), userid, userModel.getImage());

                status.setText(userModel.getStatus());
                if (userModel.getStatus().equals("online")) {
                    //Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_1)).into(holder.imageActive);
                    photo_status.setImageResource(R.drawable.ic_ellipse_1);

                } else {
                    photo_status.setImageResource(R.drawable.ic_ellipse_4);


                    // Glide.with(context).load(context.getDrawable(R.drawable.ic_ellipse_4)).into(holder.imageActive);
                }
                sendImageToChat.setOnClickListener(v -> {
                    openImage();
                });
                camera.setOnClickListener(v -> {
                    openCamera();

                });
                file.setOnClickListener(v -> {
                    openFile();


                });
                emoji.setOnClickListener(v -> {
                    setEmoji();
                });



                phone_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     getMyData();


                    }
                });
                video_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMyData();
                        Intent intent1=new Intent(MainChatActivity.this,VideoChat.class);
                        intent1.putExtra("id3", userid);
                        startActivity(intent1);

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    /*private void uploadFile() {
        final ProgressDialog pd2 = new ProgressDialog(MainChatActivity.this);
        pd2.setMessage("File is loading .... ");
        pd2.show();
        final StorageReference storageReferenceFile = storageReference.child("uploadPDF" + System.currentTimeMillis() + (".pdf"));




           storageReferenceFile.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                   while (! uriTask.isSuccessful());
                    Uri uri= uriTask.getResult();
                   // PutPDF putPDF=new PutPDF();


               }
           }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

               }
           });



        }*/
    private void uploadFile() {
        final ProgressDialog pd2 = new ProgressDialog(MainChatActivity.this);
        pd2.setMessage("File is loading .... ");
        pd2.show();
        final StorageReference storageReferenceFile = storageReference.child("chatFile").child("file" + System.currentTimeMillis() + (".pdf"));

        if (fileUri != null) {

            uploadTask = storageReferenceFile.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReferenceFile.getDownloadUrl();
                }


            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // هنا لو فعلا الصوره اترفعت صح بياخد uri بتاعها ويحطه فى firebase
                        downloadUriFile = task.getResult();
                        sendMessage(firebaseUser.getUid(), userid, msg, Calendar.getInstance().getTimeInMillis(), "", downloadUriFile.toString(), getFileName(fileUri));
                        pd2.dismiss();
                        editText.getText().clear();

                    } else {
                        Toast.makeText(MainChatActivity.this, "Failed:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        pd2.dismiss();
                    }
                }
            });

        } else {
            Toast.makeText(MainChatActivity.this, "No File selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMessage(String sender, String receiver, String message, Long time, String upload, String file, String fileName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        MainChatModel mainChatModel = new MainChatModel(sender, receiver, message, "false", time, upload, file, fileName);

        reference.child("Chats").push().setValue(mainChatModel).addOnCompleteListener(task -> {
            imageUri=null;
            fileUri=null;

        });



        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                if (notify) {

                    if(!mainChatModel.getFile().equals("")){
                        sendNotification(receiver, user.getName(), "file");

                    }
                    else  if (! mainChatModel.getUpload().equals(""))
                    {
                        sendNotification(receiver, user.getName(), "photo");
                    }
                    else {

                        sendNotification(receiver, user.getName(), msg);
                    }

                }

                notify = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotification(String receiver, String name, String message) {
        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = token.orderByKey().equalTo(receiver);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Token token = snapshot1.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.slack, name + ": " + message, "new Message", userid);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    Toast.makeText(MainChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                try {
                                    Toast.makeText(MainChatActivity.this, "onFailure! : " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(MainChatActivity.this, "onFailure! : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == sendImage) {
            notify = true;
            msg = editText.getText().toString();
            imageViewSend.setVisibility(View.GONE);
            constraintFile.setVisibility(View.GONE);


            if (imageUri != null) {
                uploadImage();


            } else if (fileUri != null) {
                uploadFile();
                editText.getText().clear();
            } else if (!msg.equals("")) {
                sendMessage(firebaseUser.getUid(), userid, msg, Calendar.getInstance().getTimeInMillis(), "", "", "");
                // setEmoji();
                editText.getText().clear();

            } else {
                Toast.makeText(MainChatActivity.this, "you can't send empty message", Toast.LENGTH_LONG).show();
            }

        }


        if (v == back) {
            onBackPressed();
        }


    }

    private void readMessage(String myid, String userid, String imageUrl) {

        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mChat.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MainChatModel chat = snapshot1.getValue(MainChatModel.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }
                    MessageChatAdapter messageChatAdapter = new MessageChatAdapter(MainChatActivity.this, mChat, imageUrl);
                    recyclerView.setAdapter(messageChatAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(userid);
    }

    private void seenMessage(String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MainChatModel chat = snapshot1.getValue(MainChatModel.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                        chat.setIsSeen("true");
                        reference.child(snapshot1.getKey()).setValue(chat);

                        //DatabaseReference chatRef =FirebaseDatabase.getInstance().getReference()

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentUser", userid);
        editor.apply();

    }


    @Override
    protected void onResume() {
        super.onResume();
        currentUser(userid);

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentUser("none");
    }


    private void uploadImage() {
        // ده dialog لما بعمل upload
        final ProgressDialog pd = new ProgressDialog(MainChatActivity.this);
        pd.setMessage("Uploading");
        pd.show();
        if (imageUri != null) {
            // هنا بيحط الصوره فى storage
            final StorageReference fileReference = storageReference.child("chatPhoto").child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            //هنا بيرفعها
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }


            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // هنا لو فعلا الصوره اترفعت صح بياخد uri بتاعها ويحطه فى firebase
                        downloadUri = task.getResult();
                        sendMessage(firebaseUser.getUid(), userid, msg, Calendar.getInstance().getTimeInMillis(), downloadUri.toString(), "", "");
                        pd.dismiss();
                        editText.getText().clear();

                    } else {
                        Toast.makeText(MainChatActivity.this, "Failed:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        } else {
            Toast.makeText(MainChatActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // دى عشان اعرف نوع الصوره
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = MainChatActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // دى عشان  افتح صفحه جديده اختار( select) منها الصوره
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);


    }

    // هنا بياخد uri بتاعه الصوره
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).into(imageViewSend);
            imageViewSend.setVisibility(View.VISIBLE);


        }
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), photo, "val", null);
            imageViewSend.setImageBitmap(photo);
            imageUri = Uri. parse(path);
            imageViewSend.setVisibility(View.VISIBLE);


        }
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            // Get the Uri of the selected file
            fileUri = data.getData();
            constraintFile.setVisibility(View.VISIBLE);
            pdf.setImageResource(R.drawable.pdf);
            file_name.setText(getFileName(fileUri));
           /*


           String uriString = fileUri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName =myFile.getName();


           String uriString = fileUri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(fileUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }*/


           /* fileUri = data.getData();
            imageViewSend.setVisibility(View.VISIBLE);*/

            /*        StorageReference storageReferenceFile = storageReference.child("files");
            StorageReference file_name = storageReferenceFile.child("file" + fileUri.getPathSegments());
            file_name.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
                    file_name.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    });
                }
            });*/
        }


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    //هعمل فانكشن عشان افتح الكاميرا واخد صوره
    private void openCamera() {
        Intent sendImage = new Intent();
        sendImage.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(sendImage, 101);

    }

    // ده عشان افتح واختار file
    private void openFile() {
        Intent file = new Intent();
        file.setType("application/pdf");
        file.setAction(file.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(file, "PDF FILE SELECT"), 12);

    }

   private void setEmoji() {


        EmojiPopup popup = EmojiPopup.Builder.fromRootView(rootView)
                .build(editText);


        emoji.setOnClickListener(v -> popup.toggle());



    }



    private void calLNotification () {
        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = token.orderByKey().equalTo(userid);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Token token = snapshot1.getValue(Token.class);
                    CallData callData = new CallData(myData.getImage(),myData.getName(),userid);
                    SenderCall senderCall = new SenderCall(callData, token.getToken());
                    apiService.sendCall(senderCall).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    Toast.makeText(MainChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();

                                }else {
                                    Intent intent1 = new Intent(MainChatActivity.this, VoiceChat.class);
                                    intent1.putExtra("id2", userid);
                                    startActivity(intent1);
                                }
                            } else {
                                try {
                                    Toast.makeText(MainChatActivity.this, "onFailure! : " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(MainChatActivity.this, "onFailure! : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

public void getMyData(){
    reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
             myData = snapshot.getValue(UserModel.class);
          calLNotification();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
}



