package org.my.Test.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.my.Test.R;
import org.my.Test.db.DbManager;
import org.my.Test.reader.CsvReader;
import org.my.Test.stock.Stock;
import org.my.Test.utils.Global;

import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity {
    private DbManager dbManager;
    private ListView listviewMain;
    private String[] listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionGen.with(MainActivity.this).addRequestCode(100).permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).request();

        setTitle("my stock");

        initControls();
    }

    @PermissionSuccess(requestCode = 100)
    public void getStorage(){
        dbManager = Global.getDbManager(this);
    }

    @PermissionFail(requestCode = 100)
    public void failStorage() {
        Toast.makeText(this, "Storage permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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
                dbManager.clearStocks();
                List<Stock> stocks = CsvReader.readStocks();
                dbManager.addStocks(stocks);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initControls() {
        listviewMain = (ListView)findViewById(R.id.listviewMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        listItems = new String[]{"沪深股市","沪市","深市","创业板","行业","地区","最近","历史成交","资金流水"};
        adapter.addAll(listItems);

        listviewMain.setAdapter(adapter);
        listviewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.putExtra("CONDITION"," ");
                        intent.putExtra("TITLE","沪深股市");
                        intent.setClass(MainActivity.this,StockActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("CONDITION"," where market = 'SH' ");
                        intent.putExtra("TITLE","沪市");
                        intent.setClass(MainActivity.this,StockActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("CONDITION"," where market = 'SZ' ");
                        intent.putExtra("TITLE","深市");
                        intent.setClass(MainActivity.this,StockActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("CONDITION"," where code like '3%' ");
                        intent.putExtra("TITLE","创业板");
                        intent.setClass(MainActivity.this,StockActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this,TradeActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(MainActivity.this,RegionActivity.class);
                        startActivity(intent);
                        break;
                    case 6:

                        break;
                    case 7:
                        intent.setClass(MainActivity.this,TransactionActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent.setClass(MainActivity.this,CapitalflowActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }
}
