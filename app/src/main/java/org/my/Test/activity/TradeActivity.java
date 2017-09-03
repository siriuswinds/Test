package org.my.Test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.my.Test.R;
import org.my.Test.db.DbManager;
import org.my.Test.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class TradeActivity extends AppCompatActivity {
    private ListView listviewTrade;
    private ArrayAdapter<String> adapter;
    private List<String> tradeList;
    private DbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        setTitle("行业");
        tradeList = new ArrayList<>();
        dbManager = Global.getDbManager(this);

        initControls();
    }

    private void loadTrades() {
        List<String> trades = dbManager.getTrades();
        tradeList.clear();
        tradeList.addAll(trades);
    }

    private void initControls() {
        listviewTrade = (ListView)findViewById(R.id.listviewTrade);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        loadTrades();
        adapter.addAll(tradeList);
        listviewTrade.setAdapter(adapter);
        listviewTrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String trade = tradeList.get(position);
                intent.putExtra("CONDITION",String.format(" where trade='%s' ",trade));
                intent.putExtra("TITLE",trade);
                intent.setClass(TradeActivity.this,StockActivity.class);
                startActivity(intent);
            }
        });
    }
}
