package org.my.Test.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.my.Test.R;
import org.my.Test.adapter.StockTransactionAdapter;
import org.my.Test.db.DbManager;
import org.my.Test.reader.HttpReader;
import org.my.Test.stock.Capitalflow;
import org.my.Test.stock.Stock;
import org.my.Test.utils.DateUtil;
import org.my.Test.utils.Global;
import org.my.Test.utils.StockUtil;
import org.my.Test.widget.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockTransactionActivity extends AppCompatActivity {
    private DbManager dbManager;
    private Capitalflow capitalflow;
    private StockTransactionAdapter adapter;
    private List<Capitalflow> listStockTransaction;
    private int pageIndex = 0;
    private int pageItemCount = 40;
    private boolean isEndOfList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case Global.STOCK_DOWNLOAD_SUCCESS:
                    loadStockTransactions();
                    adapter.notifyDataSetChanged();
                    break;
                case Global.STOCK_DOWNLOAD_FAILED:
                    listStockTransaction.clear();
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_transaction);

        dbManager = Global.getDbManager(this);
        Intent intent = getIntent();
        capitalflow = (Capitalflow)intent.getSerializableExtra("CAPITALFLOW");
        setTitle(capitalflow.getZqmc().concat(" ").concat(capitalflow.getZqdm()));
        listStockTransaction = new ArrayList<>();

        initControls();
        loadStockTransactions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_stock,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuRefresh:
                RefreshStockDaily();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RefreshStockDaily() {
        HttpReader reader = new HttpReader();
        reader.setHandler(handler);
        reader.setDbManager(dbManager);
        try {
            Stock stock = dbManager.getStock(capitalflow.getZqdm());
            Date end = new Date();
            Date start = DateUtil.getFirstDate(listStockTransaction.get(0).getJsrq());
            reader.downloadStockDaily(stock,start,end);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStockTransactions() {
        listStockTransaction.clear();
        listStockTransaction.addAll(StockUtil.computeProfits(dbManager.getStockTransactions(capitalflow,pageItemCount,pageIndex*pageItemCount)));
        adapter.notifyDataSetChanged();
    }

    private void initControls() {
        final MyListView listview = (MyListView)findViewById(R.id.listviewStockTransaction);
        adapter = new StockTransactionAdapter(StockTransactionActivity.this,R.layout.item_stock_transaction,listStockTransaction);
        listview.setAdapter(adapter);

        listview.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(pageIndex==0) {
                    listview.onRefreshComplete();
                    return;
                }
                isEndOfList = false;
                List<Capitalflow> list = dbManager.getStockTransactions(capitalflow,pageItemCount, --pageIndex * pageItemCount);
                listStockTransaction.clear();
                listStockTransaction.addAll(list);
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

                List<Capitalflow> list = dbManager.getStockTransactions(capitalflow,pageItemCount, ++pageIndex * pageItemCount);

                if(list == null || list.size() < pageItemCount) {
                    isEndOfList = true;
                }else{
                    isEndOfList = false;
                }

                listStockTransaction.clear();
                listStockTransaction.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onLoadComplete();
            }
        });
    }
}
