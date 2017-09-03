package org.my.Test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.my.Test.R;
import org.my.Test.stock.Capitalflow;
import org.my.Test.utils.DateUtil;

import java.util.List;

public class StockTransactionAdapter extends ArrayAdapter<Capitalflow> {

    private int resourceId;

    public StockTransactionAdapter(Context context, int resource, List<Capitalflow> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Capitalflow capitalflow = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.jsrq = (TextView) view.findViewById(R.id.jsrq);
            viewHolder.cjsl = (TextView) view.findViewById(R.id.cjsl);
            viewHolder.cjjg = (TextView) view.findViewById(R.id.cjjg);
            viewHolder.fsje = (TextView) view.findViewById(R.id.fsje);
            viewHolder.syje = (TextView) view.findViewById(R.id.syje);
            viewHolder.zy = (TextView) view.findViewById(R.id.zy);
            viewHolder.tclose = (TextView) view.findViewById(R.id.tclose);
            viewHolder.topen = (TextView) view.findViewById(R.id.topen);
            viewHolder.high = (TextView) view.findViewById(R.id.high);
            viewHolder.low = (TextView) view.findViewById(R.id.low);
            viewHolder.topen = (TextView) view.findViewById(R.id.topen);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.jsrq.setText(DateUtil.format(capitalflow.getJsrq()));
        viewHolder.cjsl.setText(String.format("%1$.0f",capitalflow.getCjsl()));
        viewHolder.cjjg.setText(String.format("%1$.2f",capitalflow.getCjjg()));
        viewHolder.fsje.setText(String.format("%1$.2f",capitalflow.getFsje()));
        viewHolder.syje.setText(String.format("%1$.2f",capitalflow.getSyje()));
        viewHolder.zy.setText(capitalflow.getZy());

        if(capitalflow.getStockDaily()!=null) {
            viewHolder.tclose.setText(String.format("%1$.2f", capitalflow.getStockDaily().getTclose()));
            viewHolder.high.setText(String.format("%1$.2f", capitalflow.getStockDaily().getHigh()));
            viewHolder.low.setText(String.format("%1$.2f", capitalflow.getStockDaily().getLow()));
            viewHolder.topen.setText(String.format("%1$.2f", capitalflow.getStockDaily().getAmplitude()).concat("%"));
            viewHolder.topen.setText(String.format("%1$.2f", capitalflow.getStockDaily().getTopen()));

            setColor(capitalflow, viewHolder);
        }

        return view;
    }

    private void setColor(Capitalflow capitalflow, ViewHolder viewHolder) {
        if(capitalflow.getStockDaily().getChg()>0){
/*            viewHolder.chg.setTextColor(Color.rgb(150,0,0));
            viewHolder.pchg.setTextColor(Color.rgb(150,0,0));*/
            viewHolder.tclose.setTextColor(Color.rgb(150,0,0));
        }else if(capitalflow.getStockDaily().getChg()<0){
            /*viewHolder.chg.setTextColor(Color.rgb(0,150,0));
            viewHolder.pchg.setTextColor(Color.rgb(0,150,0));*/
            viewHolder.tclose.setTextColor(Color.rgb(0,150,0));
        }else if(capitalflow.getStockDaily().getChg()==0){
            /*viewHolder.chg.setTextColor(Color.rgb(0,0,0));
            viewHolder.pchg.setTextColor(Color.rgb(0,0,0));*/
            viewHolder.tclose.setTextColor(Color.rgb(0,0,0));
        }
    }

    class ViewHolder {
        TextView jsrq;
        TextView cjsl;
        TextView cjjg;
        TextView fsje;
        TextView syje;
        TextView zy;
        TextView tclose;
        TextView topen;
        TextView high;
        TextView low;
    }
}
