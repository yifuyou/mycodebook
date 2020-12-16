package com.example.mycodebook.servers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mycodebook.pojo.DataItem;

import java.util.LinkedList;

public class SQLiteService{
        private static SQLiteDatabase db;
        private static SQLiteService instance;
        private SQLiteService(Context mContext){
            MyDBOpenHelper myDBHelper = new MyDBOpenHelper(mContext, "my.db", null, 1);
            db=myDBHelper.getWritableDatabase();
        }

        public static SQLiteService getInstance(Context mContext) {
            if(instance == null){
                synchronized (SQLiteService.class){
                    if(instance == null){
                        instance=new SQLiteService(mContext);
                    }
                }
            }
            return instance;
        }
        public   void save(DataItem p) {
            db.execSQL("INSERT INTO passwords(num,info,pwd) values(?,?,?)",
                    new String[]{p.getNum(),p.getInfo(),p.getPwd()});
        }
        public  void update(DataItem p) {
            db.execSQL("UPDATE passwords SET num = ?,info = ? ,pwd = ? WHERE personid = ?",
                    new String[]{p.getNum(),p.getInfo(),p.getPwd(),p.getId().toString()});
        }
        public DataItem find(Integer id) {

            Cursor cursor =  db.rawQuery("SELECT * FROM passwords WHERE personid = ?",
                    new String[]{id.toString()});
            //存在数据才返回true
            if(cursor.moveToFirst())
            {
                int personid = cursor.getInt(cursor.getColumnIndex("personid"));
                String name = cursor.getString(cursor.getColumnIndex("num"));
                String info = cursor.getString(cursor.getColumnIndex("info"));
                String pwd = cursor.getString(cursor.getColumnIndex("info"));

                return new DataItem(personid,name,info,pwd);
            }
            cursor.close();
            return null;
        }
        public void delete(Integer id){
            db.delete("passwords","personid=?", new String[]{id.toString()});
        }


        public  LinkedList<DataItem> getAllItem(){
            Cursor cursor =  db.rawQuery("SELECT * FROM passwords",null);
            LinkedList<DataItem> result=new LinkedList<>();
            if(cursor.getCount()<1)return null;
            System.out.println(cursor.getCount());
            cursor.moveToFirst();
            do{
                int personid = cursor.getInt(cursor.getColumnIndex("personid"));
                String name = cursor.getString(cursor.getColumnIndex("num"));
                String info = cursor.getString(cursor.getColumnIndex("info"));
                String pwd = cursor.getString(cursor.getColumnIndex("info"));
                result.add(new DataItem(personid,name,info,pwd));
            }while (cursor.moveToNext());
            cursor.close();
            return result;
        }

    }
