package com.atharvredij.simplefileexplorer.UI;

import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atharvredij.simplefileexplorer.Adapter.BreadcrumbRecyclerAdapter;
import com.atharvredij.simplefileexplorer.ClickListener.BreadcrumbItemClickListener;
import com.atharvredij.simplefileexplorer.Model.FileModel;
import com.atharvredij.simplefileexplorer.Utils.FileType;
import com.atharvredij.simplefileexplorer.Utils.FileUtils;
import com.atharvredij.simplefileexplorer.R;

import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getCurrentPath;
import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getFileModelsFromFiles;
import static com.atharvredij.simplefileexplorer.Utils.FileUtils.getFilesFromPath;

public class MainActivity extends AppCompatActivity {

    // For displaying breadcrumbs
    public static BreadcrumbRecyclerAdapter mBreadcrumbRecyclerAdapter;
    public static RecyclerView breadcrumbRecyclerView;
    private Toolbar toolbar;

    // As root of phone; declared as global to survive rotation onDestroy call
    private FileModel phoneRoot;

    public static FloatingActionButton pasteFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pasteFAB = findViewById(R.id.pasteFAB);

        // TODO: ISSUE - EmptyFolderLayout is still visible after folder creation

        breadcrumbRecyclerView = findViewById(R.id.breadcrumbRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // if bundle is null i.e. First Time create and display the fragment

        if(savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(FilesListFragment.ARG_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
            FilesListFragment fragment = new FilesListFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .commit();

            phoneRoot = new FileModel(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    FileType.FOLDER, "/", 0.0 , "", 0);

            FileUtils.breadcrumbList.add(phoneRoot);
            initBreadcrumbs();

        } else {
            initBreadcrumbs();
        }


    }

    // Displays the breadcrumbs bar
    private void initBreadcrumbs() {
        mBreadcrumbRecyclerAdapter = new BreadcrumbRecyclerAdapter(new BreadcrumbItemClickListener() {
            @Override
            public void onBreadcrumbItemClick(int position) {
                FileModel fileM = FileUtils.breadcrumbList.get(position);

                while ((FileUtils.breadcrumbList.size()-1) != position) {
                    onBackPressed();
                }

            }
        });
        mBreadcrumbRecyclerAdapter.updateData(FileUtils.breadcrumbList);
        breadcrumbRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        breadcrumbRecyclerView.setAdapter(mBreadcrumbRecyclerAdapter);
        breadcrumbRecyclerView.smoothScrollToPosition(FileUtils.breadcrumbList.size()-1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FileUtils.breadcrumbList.remove(FileUtils.breadcrumbList.size() - 1);
        mBreadcrumbRecyclerAdapter.updateData(FileUtils.breadcrumbList);

        // Exit the app if there are no more fragments on the back stack

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuNewFile:
                createNewFileOrFolder(1);
                break;
                
            case R.id.menuNewFolder:
                createNewFileOrFolder(0);
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void createNewFileOrFolder(final int choice) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_enter_name, null);
        Button createButton = view.findViewById(R.id.createButton);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                bottomSheetDialog.dismiss();

                // 0 Folder, 1 File
                if(choice == 0 ) {
                    FileUtils.createNewFolder(name);
                } else {
                    FileUtils.createNewFile(name);
                }

                FilesListFragment.mFilesAdapter.updateData(getFileModelsFromFiles(getFilesFromPath(getCurrentPath())));

            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}
