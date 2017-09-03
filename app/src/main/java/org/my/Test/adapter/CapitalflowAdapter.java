package org.my.Test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.my.Test.R;
import org.my.Test.stock.Capitalflow;
import org.my.Test.utils.DateUtil;

import java.util.List;

public class CapitalflowAdapter extends ArrayAdapter<Capitalflow> {

    private int resourceId;

    public CapitalflowAdapter(Context context, int resource, List<Capitalflow> objects) {
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
            viewHolder.zqdm = (TextView) view.findViewById(R.id.zqdm);
            viewHolder.zqmc = (TextView) view.findViewById(R.id.zqmc);
            viewHolder.cjsl = (TextView) view.findViewById(R.id.cjsl);
            viewHolder.cjjg = (TextView) view.findViewById(R.id.cjjg);
            viewHolder.fsje = (TextView) view.findViewById(R.id.fsje);
            viewHolder.syje = (TextView) view.findViewById(R.id.syje);
            viewHolder.zy = (TextView) view.findViewById(R.id.zy);

            /*
            viewHolder.bizhong = (TextView) view.findViewById(R.id.bizhong);
            viewHolder.cjbh = (TextView) view.findViewById(R.id.cjbh);
            viewHolder.gddm = (TextView) view.findViewById(R.id.gddm);
            viewHolder.cjrq = (TextView) view.findViewById(R.id.cjrq);
            viewHolder.beizhu = (TextView) view.findViewById(R.id.beizhu);
            */

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.jsrq.setText(DateUtil.format(capitalflow.getJsrq()));
        viewHolder.zqdm.setText(capitalflow.getZqdm());
        viewHolder.zqmc.setText(capitalflow.getZqmc());
        viewHolder.cjsl.setText(String.format("%1$.0f",capitalflow.getCjsl()));
        viewHolder.cjjg.setText(String.format("%1$.2f",capitalflow.getCjjg()));
        viewHolder.fsje.setText(String.format("%1$.2f",capitalflow.getFsje()));
        viewHolder.syje.setText(String.format("%1$.2f",capitalflow.getSyje()));
        viewHolder.zy.setText(capitalflow.getZy());
    /*
        viewHolder.bizhong.setText(capitalflow.getBizhong());
        viewHolder.cjbh.setText(capitalflow.getCjbh());
        viewHolder.gddm.setText(capitalflow.getGddm());
        viewHolder.cjrq.setText(DateUtil.format(capitalflow.getCjrq()));
        viewHolder.beizhu.setText(capitalflow.getBeizhu());
    */
        setColor(capitalflow, viewHolder);

        return view;
    }

    private void setColor(Capitalflow capitalflow, ViewHolder viewHolder) {

    }

    class ViewHolder {
        TextView jsrq;
        TextView zqdm;
        TextView zqmc;
        TextView cjsl;
        TextView cjjg;
        TextView fsje;
        TextView syje;
        TextView zy;
        /*
        TextView bizhong;
        TextView cjbh;
        TextView gddm;
        TextView cjrq;
        TextView beizhu;
        */
    }
}
