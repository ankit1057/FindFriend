package sdu.cs.pichsinee.findfriend.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import sdu.cs.pichsinee.findfriend.MainActivity;
import sdu.cs.pichsinee.findfriend.R;
import sdu.cs.pichsinee.findfriend.utility.MyAlert;
import sdu.cs.pichsinee.findfriend.utility.UserMode;

public class RegisterFragment extends Fragment {

    private String nameString, emailString, passwordString,pathAvatarString, uidUserString;
    private Uri uri;    //ตัวแปรที่รับค่าข้อมูลจากการเลือกรูปจาก App Image ซึ่งเป็นข้อมูลทั้งก้อน ไม่ได้มีเฉพาะรูปภาพ
    private ImageView imageView;
    private boolean chooseBool = true;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Avatar Controller
        avatarController();


    }//end Main Method (onActivityCreated)

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            uri = data.getData();   //ตัวแปรที่รับค่าข้อมูลจากการเลือกรูปจาก App Image ซึ่งเป็นข้อมูลทั้งก้อน ไม่ได้มีเฉพาะรูปภาพ
            chooseBool = false; //เมื่อมีการเลือกรูปภาพแล้ว สถานะ  chooseBool เปลี่ยนเป็น false

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));  //ทำการแยกข้อมูลเอาแต่ รูปภาพ
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }// end if
    }//end onActivityResult

    private void avatarController() {   //เปิด Gallery บนมือถือเมื่อคลิกที่รูป
        imageView = getView().findViewById(R.id.imvAvatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");  //กำหนดชนิด content ที่ต้องการส่งค่ากลับ มี รูป วีดิโอ เสียง ในที่นี้เลือก รูป
                startActivityForResult(Intent.createChooser(intent, "Please Choose App Image"), 1); //ค่า requestCode กำหนดเป็นเลขจำนวนเต็มอะไรก็ได้
            }
        });

    }//end avatarController

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //ทำให้ Menu Active ทำงานได้
        if (item.getItemId() == R.id.itemUploadValue) {

//            To Do
            CheckTextField();
            return true;
        }
        return super.onOptionsItemSelected(item);
    } //end onOptionsItemSelected

    private void CheckTextField() { //Chek ค่าว่าง

//        Get Value From EditText
        //ผูกตัวแปรบน JAVA กับอิลิเมนต์บน XML
        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText emailEditText = getView().findViewById(R.id.edtEmail);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        //แปลงข้อมุลที่รับจาก EditText เป็น String
        nameString = nameEditText.getText().toString().trim();
        emailString = emailEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        MyAlert myAlert = new MyAlert(getActivity());

        if (chooseBool) {
//            Non Choose Image
            myAlert.NormalDialog("Non Choose Image", "Please Choose Image for Avatar");

        } else if (nameString.isEmpty()|| emailString.isEmpty()|| passwordString.isEmpty()) {
//            No Space
            myAlert.NormalDialog(getString(R.string.title_space),getString(R.string.title_message));

        } else {
//            No Space
            UploadValueToFirebase();

        }


    }//end CheckTextField

    private void UploadValueToFirebase() {

//        Upload Image
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        StorageReference storageReference1 = storageReference.child("Avatar/" + nameString + "Avatar");
        storageReference1.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {  //ถ้า Upload สำเร็จ
                    Log.d("5MayV1", "Upload Success");

                    findPath();

                } else {        //ถ้า Upload ไม่สำเร็จ
                    Log.d("5MayV1", "Upload Can't Success ==>" + task.getException().getMessage().toString());
                }
            }
        });

    }//end UploadValueToFirebase

    private void findPath() {   //Find Path Avatar

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        final String[] strings = new String[1];

        storageReference.child("Avatar/" + nameString + "Avatar")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        strings[0] = uri.toString();
                        pathAvatarString = strings[0];
                        Log.d("5MayV1", "Path Avatar ==> " + pathAvatarString);
                        registerEmail();

                    }
                });


    }//end findPath

    private void registerEmail() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d("5MayV1", "Register Success");
                            findUidUser();

                        } else {
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.NormalDialog("Cannot Register",
                                    task.getException().getMessage().toString());
                        }
                    }
                });


    }//end registerEmail

    private void findUidUser() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        uidUserString = firebaseUser.getUid();
        Log.d("5MayV1", "uidUser ==> " + uidUserString);

        updateNewUseToFirebase();

    }//end findUidUser

    private void updateNewUseToFirebase() { //สำหรับอัพเดตข้อมูลบน Firebase

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child(uidUserString);

        UserMode userMode = new UserMode(nameString, pathAvatarString); //Setter value to Model กำหนดค่า child ที่ต้องการใส่ใน Model

        databaseReference.setValue(userMode).addOnSuccessListener(new OnSuccessListener<Void>() {   //ส่งข้อมูล Model ไปยัง Firebase
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("5MayV1", "Success Update");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("5MayV1", "Cannot Update ==> " + e.toString());
            }
        });


    }//end updateNewUseTpFirebase

    private void uploadTextToFirebase() {//upload Name, Email, password & path Avatar To Firebase



    }//edn uploadTextToFirebase


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { //สร้าง Menu Upload บน Toolbar
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_register, menu);

    }//end onCreateOptionsMenu

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.new_register));
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please Fill All Blank");

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setHasOptionsMenu(true);    //ขออนุญาตใช้เมนู บน Fragment

    }//end Method createToolbar

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
