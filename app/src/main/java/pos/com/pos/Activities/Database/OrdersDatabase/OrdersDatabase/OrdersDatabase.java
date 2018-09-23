package pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {OrderEntry.class}, version = 1, exportSchema = false)

public abstract class OrdersDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "OrdersDB";
    private static final Object LOCK = new Object();
    private static OrdersDatabase db;

    public static OrdersDatabase getInstance(Context context){
        if (db == null){
            synchronized (LOCK) {
                db = Room.databaseBuilder(context,
                        OrdersDatabase.class, OrdersDatabase.DATABASE_NAME)
                        .allowMainThreadQueries().build();
            }
        }
        return db;
    }

    // to add a order dao we will add an abstract function that returns it
    public  abstract OrdersDAO ordersDao();
}
