package sdu.cs.pichsinee.findfriend.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import sdu.cs.pichsinee.findfriend.R;
import sdu.cs.pichsinee.findfriend.utility.MyAlert;

public class MainFragment extends Fragment {

    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Check Status การ Login
        checkStatus();


//        Register Controller
        registerController();

//        SignIn Controller
        signInController();

    }//end MainMethod (onActivityCreated)

    private void signInController() {
        Button button = getView().findViewById(R.id.btnSignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Check Authen few minus ...");
                progressDialog.show();

                EditText emailEditText = getView().findViewById(R.id.edtEmail);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String emailString = emailEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                MyAlert myAlert = new MyAlert(getActivity());   //เรียกใช้ Class MyAlert ที่สร้างไว้มาใช้

                if (emailString.isEmpty() || passwordString.isEmpty()) {    //ถ้ายังไม่ใส่ emial&pass
//                    Have Space
                    myAlert.NormalDialog(getString(R.string.title_space),getString(R.string.title_message));
                    progressDialog.dismiss();

                } else {    //ถ้าใส่ emial&pass แล้ว ก็ check
//                    No Space
                    checkEmailAndPassword(emailString, passwordString);
                }
            }
        });

    }//end signInController

    private void checkEmailAndPassword(String emailString, String passwordString) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {  //ถ้า Sign In ถูกต้อง ไปหน้า ServiceFragment
//                            Sign In true

                            progressDialog.dismiss();

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.contentMainFragment, new ServiceFragment())
                                    .commit();

                        } else {
//                            Sign In False
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.NormalDialog("Cannot Sign In",
                                    task.getException().getMessage().toString());

                            progressDialog.dismiss();
                        }
                    }
                });


    }//end checkEmailAndPassword

    private void checkStatus() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {    //ถ้า user มีค่า Login ค้างไว้อยู่ คือ ไม่ SignOut ก่อนปิดแอป

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentMainFragment, new ServiceFragment())
                    .commit();
        }

    }//end checkStatus

    private void registerController() {

        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Replace Fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

}//end class
