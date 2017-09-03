package org.my.Test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.my.Test.R;
import org.my.Test.stock.StockDaily;
import org.my.Test.utils.DateUtil;

import java.util.List;

public class StockDailyAdapter extends ArrayAdapter<StockDaily> {

    private int resourceId;

    public StockDailyAdapter(Context context, int resource, List<StockDaily> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockDaily stockdaily = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.tclose = (TextView) view.findViewById(R.id.tclose);
            viewHolder.high = (TextView) view.findViewById(R.id.high);
            viewHolder.low = (TextView) view.findViewById(R.id.low);
            viewHolder.amplitude = (TextView) view.findViewById(R.id.amplitude);
            viewHolder.topen = (TextView) view.findViewById(R.id.topen);
            viewHolder.lclose = (TextView) view.findViewById(R.id.lclose);
            viewHolder.chg = (TextView) view.findViewById(R.id.chg);
            viewHolder.pchg = (TextView) view.findViewById(R.id.pchg);
            //viewHolder.turnover = (TextView) view.findViewById(R.id.turnover);
            viewHolder.voturnover = (TextView) view.findViewById(R.id.voturnover);
            //viewHolder.vaturnover = (TextView) view.findViewById(R.id.vaturnover);
            viewHolder.tcap = (TextView) view.findViewById(R.id.tcap);
            //viewHolder.mcap = (TextView) view.findViewById(R.id.mcap);
            viewHolder.dif = (TextView) view.findViewById(R.id.dif);
            viewHolder.dea = (TextView) view.findViewById(R.id.dea);
            viewHolder.bar = (TextView) view.findViewById(R.id.bar);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.date.setText(DateUtil.format(stockdaily.getDate()));
        viewHolder.tclose.setText(String.format("%1$.2f",stockdaily.getTclose()));
        viewHolder.high.setText(String.format("%1$.2f",stockdaily.getHigh()));
        viewHolder.low.setText(String.format("%1$.2f",stockdaily.getLow()));
        viewHolder.amplitude.setText(String.format("%1$.2f",stockdaily.getAmplitude()).concat("%"));
        viewHolder.topen.setText(String.format("%1$.2f",stockdaily.getTopen()));
        viewHolder.lclose.setText(String.format("%1$.2f",stockdaily.getLclose()));
        viewHolder.chg.setText(String.format("%1$.2f",stockdaily.getChg()));
        viewHolder.pchg.setText(String.format("%1$.2f",stockdaily.getPchg()).concat("%"));
        //viewHolder.turnover.setText(String.format("%1$.2f",stockdaily.getTurnover()).concat("%"));
        viewHolder.voturnover.setText(String.format("%1$.2f",stockdaily.getVoturnover()/10000));
        //viewHolder.vaturnover.setText(String.format("%1$.4f",stockdaily.getVaturnover()/100000000.0));
        viewHolder.tcap.setText(String.format("%1$.2f",stockdaily.getTcap()/100000000.0));
        //viewHolder.mcap.setText(String.format("%1$.2f",stockdaily.getMcap()/100000000.0));
        viewHolder.dif.setText(String.format("%1$.2f",stockdaily.getDif()));
        viewHolder.dea.setText(String.format("%1$.2f",stockdaily.getDea()));
        viewHolder.bar.setText(String.format("%1$.2f",stockdaily.getBar()));

        setColor(stockdaily, viewHolder);

        return view;
    }

    private void setColor(StockDaily stockdaily, ViewHolder viewHolder) {
        if(stockdaily.getChg()>0){
            viewHolder.chg.setTextColor(Color.rgb(150,0,0));
            viewHolder.pchg.setTextColor(Color.rgb(150,0,0));
            viewHolder.tclose.setTextColor(Color.rgb(150,0,0));
        }else if(stockdaily.getChg()<0){
            viewHolder.chg.setTextColor(Color.rgb(0,150,0));
            viewHolder.pchg.setTextColor(Color.rgb(0,150,0));
            viewHolder.tclose.setTextColor(Color.rgb(0,150,0));
        }else if(stockdaily.getChg()==0){
            viewHolder.chg.setTextColor(Color.rgb(0,0,0));
            viewHolder.pchg.setTextColor(Color.rgb(0,0,0));
            viewHolder.tclose.setTextColor(Color.rgb(0,0,0));
        }
    }

    class ViewHolder {
        TextView date;
        TextView tclose;
        TextView high;
        TextView low;
        TextView amplitude;
        TextView topen;
        TextView lclose;
        TextView chg;
        TextView pchg;
        //TextView turnover;
        TextView voturnover;
       // TextView vaturnover;
        TextView tcap;
       // TextView mcap;
        TextView dif;
        TextView dea;
        TextView bar;
    }
}
