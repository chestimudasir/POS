package pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MenuItem.class}, version = 2, exportSchema = false)

public abstract class MenuDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "MenuDB";
    private static final Object LOCK = new Object();
    private static MenuDataBase db;

    public static MenuDataBase getInstance(Context context){
        if (db == null){
            synchronized (LOCK) {
                db = Room.databaseBuilder(context,
                        MenuDataBase.class, MenuDataBase.DATABASE_NAME)
                        .allowMainThreadQueries().build();
            }
        }
        return db;
    }

    // to add a order dao we will add an abstract function that returns it
    public abstract MenuDOA MenuDOA();
}
