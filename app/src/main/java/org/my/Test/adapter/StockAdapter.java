package org.my.Test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.my.Test.R;
import org.my.Test.stock.Stock;

import java.util.List;

public class StockAdapter extends ArrayAdapter<Stock> {

    private int resourceId;

    public StockAdapter(Context context, int resource, List<Stock> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stock stock = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) view.findViewById(R.id.code);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.trade = (TextView) view.findViewById(R.id.trade);
            viewHolder.region = (TextView) view.findViewById(R.id.region);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.code.setText(stock.getCode());
        viewHolder.name.setText(stock.getName());
        viewHolder.trade.setText(stock.getTrade());
        viewHolder.region.setText(stock.getRegion());
        return view;
    }

    class ViewHolder {
        TextView code;
        TextView name;
        TextView trade;
        TextView region;
    }
}
