package com.example.test.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> listOptions;
    public AutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        listOptions = new ArrayList<>();

    }
    public void setOptions(List<String> list) {
        listOptions.clear();
        listOptions.addAll(list);
    }
    @Override
    public int getCount() {
        return listOptions.size();
    }

    @Nullable
    @Override
    public String getItem (int position) {
        return listOptions.get(position);
    }

    public String getObject(int position) {
        return listOptions.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering (CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = listOptions;
                    filterResults.count = listOptions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0 ) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }

}

