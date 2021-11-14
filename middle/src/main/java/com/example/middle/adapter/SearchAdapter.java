package com.example.middle.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.middle.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "SearchAdapter";
    private final Object mLock = new Object();
    private List<String> mList;
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;
    private String mPrefix;

    public SearchAdapter(Context context, List<String> list) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
    }

    public void setData(List<String> list) {
        if (mList != null && mList.size() > 0) {
            mList = null;
        }
        mList = list;
    }

    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    public String getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.serach_item, parent, false);
            holder.textView = convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.textView.setText(getContent(getItem(position), mPrefix));
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            Log.i(TAG, "performFiltering: prefix = " + prefix.toString());
            FilterResults results = new FilterResults();
            synchronized (mLock) {
                if (TextUtils.isEmpty(prefix)) {
                    mPrefix = "";
                    results.count = 0;
                } else {
                    mPrefix = prefix.toString().trim();
                    results.count = mList.size();
                }
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private CharSequence getContent(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 >= len2 && s1.contains(s2)) {
            int start = s1.indexOf(s2);
            SpannableString spannableString = new SpannableString(s1);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF0E1218"));
            spannableString.setSpan(foregroundColorSpan, start, start + len2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return spannableString;
        } else {
            return s1;
        }
    }
}
