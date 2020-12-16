package com.example.mycodebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mycodebook.adapters.MainPageListViewAdapter;
import com.example.mycodebook.pojo.DataItem;
import com.example.mycodebook.servers.SQLiteService;
import com.example.mycodebook.util.TokenUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    /**
     * 设置一个参量，确定是否已验证
     *
     * Boolean is_pass
     */

    String token;
    Context mContext;
    SQLiteService sqLiteService;
    LinkedList<DataItem> dataItemList;

    View content_main,content_delete,content_check;


    MainPageListViewAdapter<DataItem> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        sqLiteService=SQLiteService.getInstance(mContext);
        dataItemList=new LinkedList<>();

        content_main=findViewById(R.id.content_view);
        content_delete=findViewById(R.id.content_delete);
        content_check=findViewById(R.id.content_check);

        content_delete.setVisibility(View.GONE);
        content_main.setVisibility(View.GONE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialogBuilder();

            }
        });


        init_content_main();
        init_content_delete();
        init_content_check();
    }

    private void init_content_check(){
        final EditText editText_token=(EditText) findViewById(R.id.checkPwd);
        Button button_check=(Button) findViewById(R.id.button_checkout);

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=editText_token.getText().toString();
                if(!input.isEmpty()){
                    if(TokenUtil.getToken(mContext)==null){
                        TokenUtil.setToken(mContext,input);
                        token=input;
                        content_main.setVisibility(View.GONE);
                        content_check.setVisibility(View.VISIBLE);
                        content_delete.setVisibility(View.VISIBLE);
                        flash();
                        Toast.makeText(mContext,"验证成功！！！",Toast.LENGTH_LONG).show();

                    }else if(input.equals(TokenUtil.getToken(mContext))){
                        token=input;
                        content_main.setVisibility(View.GONE);
                        content_check.setVisibility(View.VISIBLE);
                        content_delete.setVisibility(View.VISIBLE);
                        flash();
                        Toast.makeText(mContext,"验证成功！！！",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(mContext,"验证失败！！！",Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


    }


    private void init_content_main(){
        ListView listView=findViewById(R.id.list_view);
        listViewAdapter=new MainPageListViewAdapter<DataItem>(mContext,dataItemList);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog(position);
            }
        });
    }

    private void init_content_delete(){
        ListView listView=findViewById(R.id.list_view_delete);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDialog(position);
            }
        });
    }




    private boolean checkOut(String token){
        if(token!=null&&token.equals(TokenUtil.getToken(mContext))){
            return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:

            case R.id.action_check:

                content_main.setVisibility(View.GONE);
                content_check.setVisibility(View.VISIBLE);
                content_delete.setVisibility(View.GONE);
                break;

            case R.id.action_delete:
                content_main.setVisibility(View.GONE);
                content_check.setVisibility(View.GONE);
                content_delete.setVisibility(View.VISIBLE);
                break;
            case R.id.action_view:
                content_delete.setVisibility(View.GONE);
                content_check.setVisibility(View.GONE);
                content_main.setVisibility(View.VISIBLE);
                default:break;


        }
        flash();

        return super.onOptionsItemSelected(item);
    }


    private void flash(){

        if(checkOut(token))dataItemList=sqLiteService.getAllItem();
        listViewAdapter.updateData(dataItemList);
    }



    /**
     * 弹窗
     */

    //添加——弹窗
    private void addDialogBuilder(){
         AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
         final View layout=View.inflate(mContext,R.layout.dialog_add,null);
         builder.setTitle("添加新账号密码：")
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "添加操作已取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = ((EditText)layout.findViewById(R.id.text_num)).getText().toString();
                        String info =((EditText)layout.findViewById(R.id.text_info)).getText().toString();
                        String pwd = ((EditText)layout.findViewById(R.id.text_pwd)).getText().toString();
                        DataItem item=new DataItem(num,info,pwd);
                        sqLiteService.save(item);/*
                        listViewAdapter.notifyAdd(item );*/
                        flash();
                    }
                })
                 .create()
         .show();

    }


    //查看密码——弹窗
    private void dialog(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        final DataItem inputItem=(DataItem)listViewAdapter.getItem(position);
        builder.setTitle("查看密码：")
                .setMessage("账号："+inputItem.getNum()+"\n 密码："+inputItem.getPwd())
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDialog(inputItem);
                    }
                })
                .create()
                .show();
    }

    //修改——弹窗
    private void updateDialog(final DataItem oldItem){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        final View layout=View.inflate(mContext,R.layout.dialog_add,null);
        ((EditText)layout.findViewById(R.id.text_num)).setText(oldItem.getNum());
        ((EditText)layout.findViewById(R.id.text_info)).setText(oldItem.getInfo());
        ((EditText)layout.findViewById(R.id.text_pwd)).setText(oldItem.getPwd());
        builder.setTitle("修改账号信息：")
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "修改操作已取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = ((EditText)layout.findViewById(R.id.text_num)).getText().toString();
                        String info =((EditText)layout.findViewById(R.id.text_info)).getText().toString();
                        String pwd = ((EditText)layout.findViewById(R.id.text_pwd)).getText().toString();
                        DataItem item=new DataItem(oldItem.getId(),num,info,pwd);
                        sqLiteService.update(item);/*
                        listViewAdapter.notifyUpdate(oldItem,item );*/

                        flash();
                    }
                })
                .create()
                .show();

    }

    private void deleteDialog(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        final DataItem inputItem=(DataItem)listViewAdapter.getItem(position);
        builder.setTitle("删除：")
                .setMessage("账号："+inputItem.getNum()+"\n密码："+inputItem.getPwd()+"备注："+inputItem.getInfo())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteService.delete(inputItem.getId());
                        //listViewAdapter.notifyDelete(inputItem );

                        flash();

                    }
                })
                .create()
                .show();
    }

}
