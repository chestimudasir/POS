package pos.com.pos.Activities.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserConfig {

    private static SharedPreferences userConfig;
     static Context c;
    private static SharedPreferences table_data;

    public static void init(Context context){
        c = context;
        table_data = context.getSharedPreferences("tables", Context.MODE_PRIVATE);
        userConfig = context.getSharedPreferences("userconfig" , Context.MODE_PRIVATE);
    }

    public void setTableNumber(int num_tables){
        userConfig.edit().putInt("no_tables" , num_tables).apply();
    }

    public void setName(String name){
        userConfig.edit().putString("name_of_business" , name).apply();
    }

    public int getTableCount(){
        return userConfig.getInt("no_tables",0);
    }

    public String getName(){
        return userConfig.getString("name_of_business" , "no_name");
    }

    public SharedPreferences getUserConfig(){
        return userConfig;
    }

    public void setSetUpStatus(int status){
        userConfig.edit().putInt("set_up_status" , status).apply();

    }

    public int getSetUpStatus(){
        return userConfig.getInt("set_up_status" , 0);

    }

    public void setAdminPassword(String password){
        userConfig.edit().putString("admin_password" , password).apply();
    }

    public String getAdminPassword(){
        return userConfig.getString("admin_password" , null);
    }

    public void setupTables(){
        //ADD TABLES TO SHARED PREFS
        for (int i = 1; i <= getTableCount() ; i++) {
            addTable("table_"+i ,0);
        }
    }

    public void addTable(String table, int data){
        table_data.edit().putInt(table , data).apply();
    }

    public void setOrderToTable(int pos , int order_no){
        table_data.edit().putInt("table_"+pos , order_no).apply();
        Log.d("table_"+pos,String.valueOf(getOrderNumberFromTable(pos)));

    }

    public int getOrderNumberFromTable(int pos){
        return table_data.getInt("table_"+pos,0);
    }

    public void clear(){
        setupTables();
    }


}
