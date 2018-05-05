package sdu.cs.pichsinee.findfriend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sdu.cs.pichsinee.findfriend.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {   //ถ้าค่า
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMainFragment, new MainFragment())
                    .commit();
        }
    }

}//end Class
