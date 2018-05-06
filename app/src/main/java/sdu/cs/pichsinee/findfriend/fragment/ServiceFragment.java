package sdu.cs.pichsinee.findfriend.fragment;

import android.os.Bundle;
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
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sdu.cs.pichsinee.findfriend.MainActivity;
import sdu.cs.pichsinee.findfriend.R;
import sdu.cs.pichsinee.findfriend.utility.FriendAdapter;
import sdu.cs.pichsinee.findfriend.utility.UserMode;

public class ServiceFragment extends Fragment {

    private String displayNameString,uidUserLoggedinString;
    private ArrayList<String> uidFriendStringArrayList, friendStringArrayList, pathAvatarStringArrayList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Find Member of Friends on Firebase
        findMember();

    }//end Main Method (onActivityCreated)

    private void findMember() { //หาจำนวน Member บน Friebase
        uidFriendStringArrayList = new ArrayList<>();
        friendStringArrayList = new ArrayList<>();
        pathAvatarStringArrayList = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Log.d("6MayV1", "dataSnapshot1 ==> " + dataSnapshot1.toString());

                    if (!uidUserLoggedinString.equals(dataSnapshot1.getKey())) {    //ถ้าค่า uid ที่ get มาตรงกับ uid ของ user ที่ Login จะไม่แสดงใน Listview
                        uidFriendStringArrayList.add(dataSnapshot1.getKey());
                    }
                }//end For

                Log.d("6MayV1", "uidFriend ==> " + uidFriendStringArrayList.toString());

                createListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }//end findMember

    private void createListView() { //สร้าง ListView แสดงรายชื่อ Friend แต่ไม่แสดงชื่อของ user ที่ Login

        final int[] ints = new int[]{0};

        for (int i=0; i<uidFriendStringArrayList.size(); i+=1) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference()
                    .child(uidFriendStringArrayList.get(i));

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("6MayV2", "dataSnapshot ==> " + dataSnapshot.toString());
                    UserMode userMode = dataSnapshot.getValue(UserMode.class);

                    friendStringArrayList.add(userMode.getNameString());
                    pathAvatarStringArrayList.add(userMode.getPathAvatarString());

                    if (ints[0] == (uidFriendStringArrayList.size()-1)) {

                        Log.d("6MayV3", "Friend ==> " + friendStringArrayList.toString());
                        Log.d("6MayV3", "Path ==> " + pathAvatarStringArrayList.toString());

                        FriendAdapter friendAdapter = new FriendAdapter(getActivity(),
                                friendStringArrayList, pathAvatarStringArrayList);

                        ListView listView = getView().findViewById(R.id.listViewFriend);
                        listView.setAdapter(friendAdapter);



                    }//end if
                    ints[0] += 1;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }//end For




    }//end createListView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemSignOut) {

            SignOutFirebase();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }//end onOptionsItemSelected

    private void SignOutFirebase() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        //พอ Signout แล้วให้กลับไปที่หน้าแรห คือ หน้า MainFragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentMainFragment, new MainFragment())
                .commit();

    }//end SignOutFirebase

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { //แสดง Menu บน Optionbar
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_service, menu);

    }//end onCreateOptionsMenu

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarService);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My All Friends");

//        Show DisplayName on SubTitle Toolbar
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();  //ดังค่า user ทั้งหมดจาก Authen บน Firebase
        displayNameString = firebaseUser.getDisplayName();
        uidUserLoggedinString = firebaseUser.getUid();

        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(displayNameString + " Signed");

        setHasOptionsMenu(true);    //กำหนดให้ Toolbar มี Optionmenu

    }//end createToolbar

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);  //ผูก Class กับ XML
        return view;
    }//end onCreateView

}//end Class
