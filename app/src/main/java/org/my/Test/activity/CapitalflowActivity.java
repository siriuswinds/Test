package org.my.Test.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.my.Test.R;
import org.my.Test.adapter.CapitalflowAdapter;
import org.my.Test.db.DbManager;
import org.my.Test.reader.CsvReader;
import org.my.Test.stock.Capitalflow;
import org.my.Test.utils.Global;
import org.my.Test.widget.MyListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CapitalflowActivity extends AppCompatActivity {
    private DbManager dbManager;
    private List<Capitalflow> capitalflowList;
    private CapitalflowAdapter adapter;
    private int pageIndex = 0;
    private int pageItemCount = 40;
    private boolean isEndOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitalflow);
        setTitle("资金流水");
        capitalflowList = new ArrayList<>();
        dbManager = Global.getDbManager(this);
        initControls();
        loadCapitalflowsFromDB();
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
                dbManager.clearCapitalflows();
                capitalflowList.clear();
                capitalflowList.addAll(loadCapitalflowsFromCsv());
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCapitalflowsFromDB() {
        capitalflowList.clear();
        capitalflowList.addAll(dbManager.getCapitalflows(pageItemCount,pageIndex*pageItemCount));
        adapter.notifyDataSetChanged();
    }

    private void initControls() {
        adapter = new CapitalflowAdapter(CapitalflowActivity.this, R.layout.item_capitalflow, capitalflowList);
        final MyListView listview = (MyListView)findViewById(R.id.listviewCapitalflow);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Capitalflow capitalflow = capitalflowList.get(position);
                Intent intent = new Intent();
                intent.setClass(CapitalflowActivity.this,StockTransactionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("CAPITALFLOW",capitalflow);
                intent.putExtras(bundle);
                startActivity(intent);
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
                List<Capitalflow> list = dbManager.getCapitalflows(pageItemCount,--pageIndex*pageItemCount);
                capitalflowList.clear();
                capitalflowList.addAll(list);
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

                List<Capitalflow> list = dbManager.getCapitalflows(pageItemCount,++pageIndex*pageItemCount);

                if(list == null || list.size() < pageItemCount) {
                    isEndOfList = true;
                }else{
                    isEndOfList = false;
                }

                capitalflowList.clear();
                capitalflowList.addAll(list);
                adapter.notifyDataSetChanged();
                listview.onLoadComplete();
            }
        });
    }

    private Collection<? extends Capitalflow> loadCapitalflowsFromCsv() {
        return CsvReader.readCapitalflows(dbManager);
    }
}
