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

public class RegionActivity extends AppCompatActivity {
    private List<String> listRegion;
    private ListView listviewRegion;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        setTitle("地区");
        listRegion = new ArrayList<>();
        dbManager = Global.getDbManager(this);
        initControls();
    }

    private void initControls() {
        listviewRegion = (ListView)findViewById(R.id.listviewRegion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        loadRegions();
        adapter.addAll(listRegion);
        listviewRegion.setAdapter(adapter);
        listviewRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String region = listRegion.get(position);
                intent.putExtra("CONDITION",String.format(" where region = '%s' ",region));
                intent.putExtra("TITLE",region);
                intent.setClass(RegionActivity.this,StockActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadRegions(){
        List<String> regions = dbManager.getRegions();
        listRegion.clear();
        listRegion.addAll(regions);
    }
}
