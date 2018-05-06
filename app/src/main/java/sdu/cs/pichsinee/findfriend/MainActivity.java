package sdu.cs.pichsinee.findfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import sdu.cs.pichsinee.findfriend.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMainFragment, new MainFragment())
                    .commit();
        }

    }//end Main Method

    @Override
    public void onBackPressed() {   //กำหนดการห้ามใช้ปุ่ม ย้อนกลับ บนมือถือ
        //super.onBackPressed();
        Toast.makeText(MainActivity.this,"Connot Undo",Toast.LENGTH_LONG).show();
    }
}//end Class
