package com.example.lml.dz_reader.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lml.dz_reader.R;
import com.example.lml.dz_reader.db.ArticleDao;

/**
 * Created by lml on 2016/8/5.
 */
public class MySimpleCursorAdapter extends SimpleCursorAdapter {
    private Cursor cursor;
    private Context context;

    public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.cursor = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_item, null);
        }
        cursor.moveToPosition(position);

        String title = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Title.columnName));
        String subtitle = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Content.columnName));
        String date = cursor.getString(cursor.getColumnIndex(ArticleDao.Properties.Date.columnName));
        byte[] icon = cursor.getBlob(cursor.getColumnIndexOrThrow(ArticleDao.Properties.Icon.columnName));

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_1);
        if (icon != null){
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(icon,0,icon.length));
        }else {
            imageView.setImageResource(R.drawable.ic_add_black_24dp);
        }

        TextView titleText = (TextView) view.findViewById(R.id.tv_title);
        titleText.setText(title);

        TextView contentText = (TextView) view.findViewById(R.id.tv_content);
        contentText.setText(subtitle);

        TextView dateText = (TextView)view.findViewById(R.id.tv_date);
        dateText.setText(date);

        return view;
    }
}
