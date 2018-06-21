package pos.com.pos.Activities.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class UserConfig {

    private static SharedPreferences userConfig;

    public static void init(Context context){
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
}
