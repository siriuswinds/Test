package org.my.Test.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.my.Test.R;
import org.my.Test.adapter.TransactionAdapter;
import org.my.Test.db.DbManager;
import org.my.Test.reader.CsvReader;
import org.my.Test.stock.Transaction;
import org.my.Test.utils.Global;
import org.my.Test.widget.MyListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private DbManager dbManager;
    private List<Transaction> transactionList;
    private TransactionAdapter adapter;
    private int pageIndex = 0;
    private int pageItemCount = 40;
    private boolean isEndOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        setTitle("历史成交");

        transactionList = new ArrayList<>();
        dbManager = Global.getDbManager(this);

        initControls();
        loadTransactionsFromDB();
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
                dbManager.clearTransactions();
                transactionList.clear();
                transactionList.addAll(loadTransactionsFromCsv());
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadTransactionsFromDB() {
        transactionList.clear();
        transactionList.addAll(dbManager.getTransactions(pageItemCount,pageIndex*pageItemCount));
        adapter.notifyDataSetChanged();
    }

    private void initControls() {
        adapter = new TransactionAdapter(TransactionActivity.this, R.layout.item_transaction, transactionList);
        final MyListView listview = (MyListView)findViewById(R.id.listviewTransaction);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                List<Transaction> list = dbManager.getTransactions(pageItemCount, pageItemCount*--pageIndex);
                transactionList.clear();
                transactionList.addAll(list);
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
                List<Transaction> list = dbManager.getTransactions(pageItemCount, pageItemCount*++pageIndex);
                if(list == null || list.size() < pageItemCount) {
                    isEndOfList = true;
                }else{
                    isEndOfList = false;
                }
                transactionList.clear();
                transactionList.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onLoadComplete();
            }
        });
    }

    private Collection<? extends Transaction> loadTransactionsFromCsv() {
        return CsvReader.readTransactions(dbManager);
    }
}
