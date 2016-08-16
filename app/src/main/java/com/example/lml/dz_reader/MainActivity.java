package com.example.lml.dz_reader;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lml.dz_reader.Adapter.MySimpleCursorAdapter;
import com.example.lml.dz_reader.db.Article;
import com.example.lml.dz_reader.db.ArticleDao;
import com.example.lml.dz_reader.db.DaoMaster;
import com.example.lml.dz_reader.db.DaoSession;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    private Cursor cursor;
    private SQLiteDatabase mdb;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QR_Activity.class);
                startActivity(intent);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initDateBase();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        cursor = mdb.query(getArticleDao().getTablename(), null, null, null, null, null, null);
        int[] to = {R.id.tv_title,R.id.tv_content,R.id.tv_date,R.id.iv_1};

        MySimpleCursorAdapter myAdapter = new MySimpleCursorAdapter(this, R.layout.layout_item, cursor, getFrom(), to);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "点击了int = " + i + "long = " + l,Toast.LENGTH_LONG).show();
                String url = daoSession.load(Article.class, l).getContent();
                Intent intent = new Intent(MainActivity.this, YueWebView.class);
                intent.putExtra("URL", url);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "您长摁了int = " + i + "long = " + l,Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.main);
        toolbar.setTitle("最近");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_search) {
                    Toast.makeText(MainActivity.this, "这是个搜索框", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }


    private void initDateBase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ArticleTable.db", null);
        mdb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mdb);
        daoSession = daoMaster.newSession();
    }

    private void setListViewTouch(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }




    /**
     * 获得 Adapter的第四个from参数
     * @return String[]
     */
    public String[] getFrom(){
        String titleColumn = ArticleDao.Properties.Title.columnName;
        String subTitleColumn = ArticleDao.Properties.Content.columnName;
        String dateColumn = ArticleDao.Properties.Date.columnName;
        String IconColumn = ArticleDao.Properties.Icon.columnName;
        String[] from = {titleColumn,subTitleColumn,dateColumn,IconColumn};
        return from;
    }

    public ArticleDao getArticleDao(){
        return daoSession.getArticleDao();
    }


    /**
     * easypermissions 部分
     */
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQrcodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQrcodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }





    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
