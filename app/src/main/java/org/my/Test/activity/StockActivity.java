package org.my.Test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.my.Test.R;
import org.my.Test.adapter.StockAdapter;
import org.my.Test.db.DbManager;
import org.my.Test.stock.Stock;
import org.my.Test.utils.Global;
import org.my.Test.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    private List<Stock> stocklist = new ArrayList<Stock>();
    private DbManager dbManager;
    private Button btnQuery;
    private EditText txtCondition;
    private StockAdapter adapter;
    private String condition;
    private int pageIndex = 0;
    private int pageItemCount = 120;
    private boolean isEndOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Intent intent = this.getIntent();
        condition = intent.getStringExtra("CONDITION");
        setTitle(intent.getStringExtra("TITLE"));
        dbManager = Global.getDbManager(this);

        initControls();
        stocklist.addAll(getStocks(condition));
        adapter.notifyDataSetChanged();
    }

    private void initControls() {
        btnQuery =(Button)findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "%".concat(txtCondition.getText().toString().concat("%"));
                String condition = String.format(" where code like '%s' or name like '%s' ",data,data);
                stocklist.clear();
                stocklist.addAll(getStocks(condition));
                adapter.notifyDataSetChanged();
            }
        });

        txtCondition = (EditText)findViewById(R.id.txtCondition);

        adapter = new StockAdapter(StockActivity.this, R.layout.item_stock, stocklist);
        final MyListView listview = (MyListView) findViewById(R.id.listviewStock);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = stocklist.get(position);
                Intent intent = new Intent();
                intent.setClass(StockActivity.this,StockDailyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stock",stock);
                intent.putExtra("code",stock.getCode());
                intent.putExtra("name",stock.getName());
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(StockActivity.this, stock.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        listview.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(pageIndex==0) {
                    listview.onRefreshComplete();
                    return;
                }
                isEndOfList = false;
                List<Stock> list = dbManager.getStocks(pageItemCount,--pageIndex*pageItemCount,condition);;
                stocklist.clear();
                stocklist.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onRefreshComplete();
                listview.setSelection(listview.getCount());
            }
        });

        listview.setonLoadListener(new MyListView.OnLoadListener() {
            @Override
            public void onLoad() {
                if(isEndOfList)
                {
                    listview.onLoadComplete();
                    return;
                }

                List<Stock> list = dbManager.getStocks(pageItemCount,++pageIndex*pageItemCount,condition);

                if(list == null || list.size() < pageItemCount) {
                    isEndOfList = true;
                }else{
                    isEndOfList = false;
                }
                stocklist.clear();
                stocklist.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onLoadComplete();
            }
        });
    }

    private List<Stock> getStocks(String condition) {
        //stocklist = CsvReader.readStocks(this.getApplicationContext(),dbManager);
        return dbManager.getStocks(pageItemCount,pageIndex*pageItemCount,condition);
    }
}
