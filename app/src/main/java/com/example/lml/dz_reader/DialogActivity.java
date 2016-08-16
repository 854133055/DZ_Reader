package com.example.lml.dz_reader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lml.dz_reader.db.Article;
import com.example.lml.dz_reader.db.ArticleDao;
import com.example.lml.dz_reader.db.DaoMaster;
import com.example.lml.dz_reader.db.DaoSession;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogActivity extends AppCompatActivity {


    @BindView(R.id.edit_title_Text)
    EditText editTitleText;
    @BindView(R.id.btn_cancel)
    Button btn_Cancel;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private Cursor cursor;
    private SQLiteDatabase mdb;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private byte[] icon_byte;
    private String urlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);

        String Webtitle = getIntent().getStringExtra("title");
        icon_byte = getIntent().getByteArrayExtra("icon_byte[]");
        urlContent = getIntent().getStringExtra("data");

        setupDatabase();
        getArticleDao();
        editTitleText.setText(Webtitle);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArticle();
                onBackPressed();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cursor = mdb.query(getArticleDao().getTablename(), null, null, null, null, null, null);
    }

    /**
     * 创建数据库
     */
    public void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ArticleTable.db", null);
        mdb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mdb);
        daoSession = daoMaster.newSession();
    }


    /**
     * 获取 ArticleDao 对象
     *
     * @return ArticleDao
     */
    public ArticleDao getArticleDao() {
        return daoSession.getArticleDao();
    }

    /**
     * 获取插入的Artile对象的title、Content，date，byte[]，并插入数据库
     */
    public void addArticle() {

        String articleTitleText = editTitleText.getText().toString();
//        String articleContentText = contentText.getText().toString();

        editTitleText.setText("");

        //规定时间格式，并获取系统时间,如：15:43
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(new Date());

        Article article = new Article(null, articleTitleText, urlContent, date, icon_byte);
        getArticleDao().insert(article);

        Toast.makeText(this, "Inserted new note,ID:" + article.getId(), Toast.LENGTH_LONG).show();
        cursor.requery();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
