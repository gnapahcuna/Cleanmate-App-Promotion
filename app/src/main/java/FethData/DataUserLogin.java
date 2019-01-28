package FethData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUserLogin {
    public String ID, branchID, firstname, latname, branchGroup, sTitle, title, branchName,roleDesc;
    public DataUserLogin(Context context){
        getDataUserLogin(context);
    }
    private void getDataUserLogin(Context context){
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("ID", Activity.MODE_PRIVATE);
        Map<String, ?> entries2 = sharedPreferences2.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
            for (int i = 0; i < sharedPreferences2.getStringSet(temp, null).size(); i++) {
                getData2 = sharedPreferences2.getStringSet(temp, null).toArray(new String[sharedPreferences2.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    ID = getData2[i].substring(3);
                } else if (chk == 'b') {
                    branchID = getData2[i].substring(3);
                } else if (chk == 'c') {
                    firstname = getData2[i].substring(3);
                } else if (chk == 'd') {
                    latname = getData2[i].substring(3);
                } else if (chk == 'e') {
                    branchGroup = getData2[i].substring(3);
                } else if (chk == 'f') {
                    roleDesc = getData2[i].substring(3);
                } else if (chk == 'g') {
                    branchName = getData2[i].substring(3);
                }
            }
        }
    }
}
