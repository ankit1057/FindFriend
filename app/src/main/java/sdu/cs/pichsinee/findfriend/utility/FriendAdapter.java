package sdu.cs.pichsinee.findfriend.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import sdu.cs.pichsinee.findfriend.R;

public class FriendAdapter extends BaseAdapter {    //สร้าง Constructor ก่อน แล้วค่อยมา Extends BaseAdapter

    private Context context;
    private ArrayList<String> nameStringArrayList, pathURLStringArrayList;

    public FriendAdapter(Context context,
                         ArrayList<String> nameStringArrayList,
                         ArrayList<String> pathURLStringArrayList) {
        this.context = context;
        this.nameStringArrayList = nameStringArrayList;
        this.pathURLStringArrayList = pathURLStringArrayList;
    }

    @Override
    public int getCount() {
        return nameStringArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.listview_friend, viewGroup, false);

        //แสดงข้อความ DisplayName
        TextView textView = view1.findViewById(R.id.txtDisplayName);
        textView.setText(nameStringArrayList.get(i));

        //ใช้ Picasso ในการดึงภาพมาแสดง โดย add Library ของ Picasso ก่อนที่เว็บ http://square.github.io/picasso/
        CircleImageView circleImageView = view1.findViewById(R.id.imvAvatar);
        Picasso.get().load(pathURLStringArrayList.get(i)).resize(150,150).into(circleImageView);

        return view1;
    }
}//end Class
