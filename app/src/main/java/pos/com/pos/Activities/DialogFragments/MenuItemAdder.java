package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuDataBase;
import pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem;
import pos.com.pos.R;

public class MenuItemAdder extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.menu_adder_dialog, container, false);

        final EditText item_name = root.findViewById(R.id.item_name),
                item_price = root.findViewById(R.id.item_price),
        category = root.findViewById(R.id.category);

        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!item_name.getText().toString().equals("") && !item_price.getText().toString().equals("")) {

                    MenuDataBase dataBase = MenuDataBase.getInstance(getActivity());
                    MenuItem menuItem = new MenuItem(item_name.getText().toString(),
                            Integer.parseInt(item_price.getText().toString()),
                            0,
                            category.getText().toString(),
                            1,
                            0, "General");

                    dataBase.MenuDOA().insertOrder(menuItem);

                }
            }
        });

        root.findViewById(R.id.next).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MenuDataBase dataBase = MenuDataBase.getInstance(getActivity());
                dataBase.MenuDOA().deleteAll();
                return true;
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
