package org.my.Test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.my.Test.R;
import org.my.Test.adapter.StockDailyAdapter;
import org.my.Test.db.DbManager;
import org.my.Test.reader.HttpReader;
import org.my.Test.stock.Stock;
import org.my.Test.stock.StockDaily;
import org.my.Test.utils.DateUtil;
import org.my.Test.utils.Global;
import org.my.Test.widget.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockDailyActivity extends AppCompatActivity {
    private DbManager dbManager;
    private Stock stock;
    private StockDailyAdapter adapter;
    private List<StockDaily> stockdailylist = new ArrayList<StockDaily>();
    private int pageIndex = 0;
    private int pageItemCount = 120;
    private boolean isEndOfList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case Global.STOCK_DOWNLOAD_SUCCESS:
                    stockdailylist.clear();
                    initStockDaily(stock.getCode());
                    adapter.notifyDataSetChanged();
                    break;
                case Global.STOCK_DOWNLOAD_FAILED:
                    Toast.makeText(StockDailyActivity.this, "日线数据更新失败！", Toast.LENGTH_LONG).show();
                    //stockdailylist.clear();
                    //adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_daily);
        dbManager = Global.getDbManager(this);
        initControls();
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
            case R.id.menuUpdate:
                stock.updateStockDaily(dbManager,handler);
                Toast.makeText(StockDailyActivity.this,"数据更新完毕!",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuMACD:
                stock.calcStockMACD(dbManager,handler);
                Toast.makeText(StockDailyActivity.this,"MACD计算完毕!",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuRefresh:
                HttpReader reader = new HttpReader();
                reader.setHandler(handler);
                reader.setDbManager(dbManager);
                try {
                    Date end = new Date();
                    Date start = DateUtil.getFirstDate(end);
                    reader.downloadStockDaily(stock,start,end);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.menuClear:
                dbManager.clearStockDaily();
                Toast.makeText(StockDailyActivity.this, "日线数据已经清空！", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initControls() {
        Intent intent = this.getIntent();
        stock = (Stock) intent.getSerializableExtra("stock");

        initStockDaily(stock.getCode());

        setTitle(stock.getName().concat(" ").concat(stock.getCode()));

        initListView();
    }

    private void initListView(){
        adapter = new StockDailyAdapter(StockDailyActivity.this, R.layout.item_stock_daily, stockdailylist);

        final MyListView listview = (MyListView) findViewById(R.id.listview_stockdaily);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            StockDaily stockdaily = stockdailylist.get(position);
            Toast.makeText(StockDailyActivity.this, stockdaily.toString(), Toast.LENGTH_SHORT).show();
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
                List<StockDaily> list = dbManager.getStockDailys(stock.getCode(),pageItemCount, pageItemCount*--pageIndex);
                stockdailylist.clear();
                stockdailylist.addAll(list);
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

                List<StockDaily> list = dbManager.getStockDailys(stock.getCode(),pageItemCount, pageItemCount*++pageIndex);

                if(list == null || list.size() < pageItemCount) {
                    isEndOfList = true;
                }else{
                    isEndOfList = false;
                }

                stockdailylist.clear();
                stockdailylist.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onLoadComplete();
            }
        });
    }

    private void initStockDaily(String code) {
        stockdailylist.addAll(dbManager.getStockDailys(code,pageItemCount, pageItemCount*pageIndex));
    }
}
