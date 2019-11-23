package com.example.sikstagram;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.sikstagram.Adapter.RankGalleryAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private Uri mImageUri;
    String miUrlOk = "";
    private StorageTask uploadTask;
    StorageReference storageRef;

    ImageView close, image_added;
    TextView post;
    EditText description;
    EditText extra_label;
    Classifier classifier;
    private String pltnumber;
    private String pltname;

    private ArrayList<String> images = new ArrayList<String>() ; //resourceIDs에 있는 값들을 가져옴. 비트맵 형식임.
    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private View btnNext, btnPrev;
    private FragmentStatePagerAdapter adapter;
    private TextView plt_name;
    private int plt_index =0;
    private ArrayList<String> kr_name = new ArrayList<String>();
    private  String[] pltnumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        String modelPath = Classifier_Utils.assetFilePath(this, "traced_mobilenet_v2_dataset_v2_best.pth");
        classifier = new Classifier(modelPath);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        extra_label = findViewById(R.id.extra_label);
        storageRef = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage_10();
            }
        });


        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage_10(){


        Log.d("SHIM", extra_label.getText().toString());

        if(extra_label.getText().toString().length()==0) {
            pltname = plt_name.getText().toString();
            pltnumber = pltnumbers[plt_index];
        } else {
            pltname = extra_label.getText().toString();
            pltnumber = "99999"; //사용자가 식물 태그를 직접 입력했을 때, 식물 코드는 99999
        }
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (mImageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
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
                        Uri downloadUri = task.getResult();
                        miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", miUrlOk);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("pltnumber", pltnumber);
                        hashMap.put("pltname", pltname);
                        reference.child(postid).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(PostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            image_added.setImageURI(mImageUri);

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                String[] kor_name = classifier.predict(bitmap, "kor");
                String[] plt_code = classifier.predict(bitmap, "label");
                pltnumbers = plt_code;
                ImageUrl_Convertor cvrt = new ImageUrl_Convertor();

                for(int i=0; i<plt_code.length; i++) {
                    images.add(cvrt.getUrl(plt_code[i]));
                    kr_name.add(kor_name[i]);
                }

                // TODO: do something with String[] results
                viewPager = (ViewPager) findViewById(R.id.view_pager);
                btnNext = findViewById(R.id.next);
                btnPrev = findViewById(R.id.prev);
                plt_name = (TextView)findViewById(R.id.plt_name);
                plt_name.setText(kr_name.get(0));
                btnPrev.setOnClickListener(onClickListener(0));
                btnNext.setOnClickListener(onClickListener(1));


                adapter = new RankGalleryAdapter(getSupportFragmentManager(), images);
                viewPager.setAdapter(adapter);
                inflateThumbnails();

            }catch (Exception e){
                e.printStackTrace(); // TODO: Error Handle
            }

        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }

    private View.OnClickListener onClickListener(final int i) {

        plt_name.invalidate();

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        plt_name.setText(kr_name.get(plt_index++));
                    }
                } else {
                    //previous page
                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        plt_name.setText(kr_name.get(plt_index--));
                    }
                }
            }
        };
    }
    private void inflateThumbnails() {
        for (int i = 0; i < images.size(); i++) {
            View imageLayout = getLayoutInflater().inflate(R.layout.rank_item, null);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(i));
            options = new BitmapFactory.Options();
            options.inSampleSize = 3; //이미지의 크기를 1/3
            options.inDither = false; //이미지를 빠르게 로딩하는 장치
            GetBitmapImageFromUrl cvrt = new GetBitmapImageFromUrl();
            Bitmap bitmap = cvrt.getImageBitmap(images.get(i), options);
            imageView.setImageBitmap(bitmap);
            //set to image view
            imageView.setImageBitmap(bitmap);
            //add imageview
//            thumbnailsContainer.addView(imageLayout);
        }
    }
    //어뎁터로 받은 list 중에 i번째 요소로 페이지를 변경함.
    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }

}
