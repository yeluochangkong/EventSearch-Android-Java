package com.example.test.test;

import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.widget.EditText;

public class Validation {

    public static boolean  isKeywordEmpty(AppCompatAutoCompleteTextView editText ) {
        String value = editText.getText().toString().trim();
        editText.setError(null);
        if(value.length() == 0) {
            return false;
        }
        return true;
    }
    public static boolean  isEmpty(EditText editText ) {
        String value = editText.getText().toString().trim();
        editText.setError(null);
        if(value.length() == 0) {
            return true;
        }
        return false;
    }
    public static String subString(String str) {
        if (str == null) return "";
        else {
            return (str.length() < 20)? str :  str.substring(0,17)+"...";
        }
    }

    public static String checkString (String str) {
        return str.equals("")? "N/A":str;
    }

    public static String followerFormat(String follower) {
        int f = Integer.parseInt(follower);
        int reminder;
        String result = "";
        String str;
        while (f > 999) {
            reminder = f % 1000;
            f = f / 1000;
            str = ","+Integer.toString(reminder);
            result =  str + result;
        }
        reminder = f % 1000;
        str = Integer.toString(reminder);
        result = str + result;
        return result;
    }
}

//    let reminder = 0
//    let l = value;
//    let  followers = "";
//    let str = "";
//    while (l > 999) {
//            reminder = l % 1000;
//            l = Math.trunc(l / 1000);
//            str = ","+reminder.toString();
//            followers = str.concat(followers);
//            }
//            reminder = l % 1000;
//            str = reminder.toString();
//            followers = str.concat(followers);
//            return followers;