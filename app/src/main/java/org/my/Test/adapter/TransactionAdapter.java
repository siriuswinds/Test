package org.my.Test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.my.Test.R;
import org.my.Test.stock.Transaction;
import org.my.Test.utils.DateUtil;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private int resourceId;

    public TransactionAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.cjrq = (TextView) view.findViewById(R.id.cjrq);
            viewHolder.zqdm = (TextView) view.findViewById(R.id.zqdm);
            viewHolder.zqmc = (TextView) view.findViewById(R.id.zqmc);
            viewHolder.mmbz = (TextView) view.findViewById(R.id.mmbz);
            viewHolder.cjsl = (TextView) view.findViewById(R.id.cjsl);
            viewHolder.cjjg = (TextView) view.findViewById(R.id.cjjg);
            viewHolder.cjje = (TextView) view.findViewById(R.id.cjje);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.cjrq.setText(DateUtil.format(transaction.getCjrq()));
        viewHolder.zqdm.setText(transaction.getZqdm());
        viewHolder.zqmc.setText(transaction.getZqmc());
        viewHolder.mmbz.setText(transaction.getMmbz());
        viewHolder.cjsl.setText(String.format("%1$.0f",transaction.getCjsl()));
        viewHolder.cjjg.setText(String.format("%1$.2f",transaction.getCjjg()));
        viewHolder.cjje.setText(String.format("%1$.2f",transaction.getCjje()));

        setColor(transaction, viewHolder);

        return view;
    }

    private void setColor(Transaction transaction, ViewHolder viewHolder) {
/*        if(stockdaily.getChg()>0){
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
        }*/
    }

    class ViewHolder {
        TextView cjrq;
        TextView zqdm;
        TextView zqmc;
        TextView mmbz;
        TextView cjsl;
        TextView cjjg;
        TextView cjje;
/*        TextView yhs;
        TextView sxf;
        TextView dsgf;
        TextView ghf;
        TextView jsf;
        TextView jyf;
        TextView jshl;
        TextView wtbh;
        TextView gddm;
        TextView jysmc;
        TextView beizhu;*/
    }
}
