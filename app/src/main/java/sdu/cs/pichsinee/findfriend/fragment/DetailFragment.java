package sdu.cs.pichsinee.findfriend.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sdu.cs.pichsinee.findfriend.R;

public class DetailFragment extends Fragment {

    //การรับค่าจาก Fragment
    public static DetailFragment detailInstance(String uidString,
                                                String nameString,
                                                String pathString){

        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();   //สร้าง Bundle เพื่อรับข้อมูลทั้งก้อนจาก Fragment
        bundle.putString("Uid", uidString);
        bundle.putString("Name", nameString);
        bundle.putString("Path", pathString);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }//end Main Method (onActivityCreated)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;

    }//end onCreateView

}//end Class
