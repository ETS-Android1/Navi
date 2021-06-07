package com.tennessee.project_navi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class WritePostActivity extends Activity {
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        findViewById(R.id.btncheck).setOnClickListener(onClickListener);
        findViewById(R.id.btnImage).setOnClickListener(onClickListener);
        findViewById(R.id.btnVideo).setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btncheck:
                    storageUploader();
                    break;
                case R.id.btnImage:
                    StartMyActivity(GalleryActivity.class, "image");
                    break;
                case R.id.btnVideo:
                    StartMyActivity(GalleryActivity.class, "video");
                    break;
            }
        }
    };

    private void storageUploader() {
        final String title = ((EditText) findViewById(R.id.TitleEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.ContentsEditText)).getText().toString();


        if (title.length() > 0  && contents.length() > 0) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo writeInfo = new WriteInfo(title, contents, user.getUid(), new Date());
            Uploader(writeInfo);
            }
        else {
            startToast("제목,내용,카테고리는 필수입력항목입니다");
        }
    }

    private void Uploader(WriteInfo writeInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Error adding document", e);
                    }
                });


    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void StartMyActivity(Class c, String media){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent,0);
    }
}
