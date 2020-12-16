package com.example.mycodebook.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public  class TokenUtil {


    public static boolean setToken(Context mContext,String token){
        try{
            SharedPreferences sp = mContext.getSharedPreferences("myCodeBook", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("psw", token);
            editor.apply();
            String s=sp.getString("psw",null);
            if(!token.equals(s))throw new Exception();

        }catch (Exception e){
            Toast.makeText(mContext,"fail!!!-error9999",Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

    public static String getToken(Context mContext){
        String s;
        try{
            SharedPreferences sp = mContext.getSharedPreferences("myCodeBook", Context.MODE_PRIVATE);
            s=sp.getString("psw",null);
            if(s == null){
                throw new Exception();
            }

        }catch (Exception e){
            Toast.makeText(mContext,"fail!!!-error9999",Toast.LENGTH_LONG).show();

            return null;
        }
        return s;
    }


}
