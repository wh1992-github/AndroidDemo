package com.example.mixture.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mixture.R;
import com.example.mixture.bean.BookInfo;
import com.example.mixture.util.FileUtil;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class BookListAdapter extends BaseAdapter {
    private ArrayList<BookInfo> mBookList;
    private Context mContext;

    public BookListAdapter(Context context, ArrayList<BookInfo> bookList) {
        mContext = context;
        mBookList = bookList;
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mBookList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    //ffff

    /**
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_book, null);
            holder.iv_format = convertView.findViewById(R.id.iv_format);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_author = convertView.findViewById(R.id.tv_author);
            holder.tv_page_number = convertView.findViewById(R.id.tv_page_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BookInfo item = mBookList.get(position);
        holder.iv_format.setImageResource(getBookImage(item.path));
        holder.tv_title.setText(item.title);
        if (TextUtils.isEmpty(item.author)) {
            holder.tv_author.setVisibility(View.GONE);
        } else {
            holder.tv_author.setVisibility(View.VISIBLE);
            holder.tv_author.setText(item.author);
        }
        if (item.page_number != 0) {
            holder.tv_page_number.setText(item.page_number + "页");
        } else {
            holder.tv_page_number.setText("");
        }
        return convertView;
    }

    //根据文件路径选取对应的书籍图标
    private int getBookImage(String path) {
        int icon_id = R.drawable.icon_other;
        String extendName = FileUtil.getExtendName(path);
        if (extendName.equals("pdf")) {
            icon_id = R.drawable.icon_pdf;
        } else if (extendName.equals("epub")) {
            icon_id = R.drawable.icon_epub;
        } else if (extendName.equals("djvu")) {
            icon_id = R.drawable.icon_djvu;
        }
        return icon_id;
    }

    public final class ViewHolder {
        public ImageView iv_format;
        public TextView tv_title;
        public TextView tv_author;
        public TextView tv_page_number;
    }

}
