package com.atharvredij.audioplus.UI;

import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.atharvredij.audioplus.Adapter.MyFragmentAdapter;
import com.atharvredij.audioplus.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        final MyFragmentAdapter adapter = new MyFragmentAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager
        tabLayout.setupWithViewPager(viewPager);

        createFolder();

    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/AudioPlus");

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

}
