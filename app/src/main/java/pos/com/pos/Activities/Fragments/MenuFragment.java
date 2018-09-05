package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuDataBase;
import pos.com.pos.Activities.DialogFragments.MenuItemAdder;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.R;

/*In menu what we need to achieve is the following :
* 1: Show parent category (General etc)
* 2: Show bottom bar when clicked on a parent category
* 3: Inside the bottom bar when clicked on a child category change the recycler view adapter
* 4: Show timed events that will show them that it is visible and understandable in a new bottom bar
* 5: Show child category and children in a different view
* 6: Sync the menu database with the online fire tree*/

public class MenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuItem[] menuItems;

    public MenuFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        final View bottomSheet = root.findViewById(R.id.bottomSheet);


        //Get ALL Menu items
        getMenu();

        //Set menu title
        final Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Bold.ttf");

        //Menu.. TITLE
        TextView orders = root.findViewById(R.id.orders);
        orders.setTypeface(custom_font);

        final RecyclerView menu = root.findViewById(R.id.menuRv);
        menu.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Make and SET ADAPTER
        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.menu_holder, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                holder.parent_category.setTypeface(custom_font);
                holder.parent_category.setText(menuItems[holder.getAdapterPosition()].parent_category);
                holder.enter.setTypeface(custom_font);
                holder.enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });


            }

            @Override
            public int getItemCount() {
                return menuItems.length;
            }
        };
        menu.setAdapter(adapter);


        //LAUNCH ADDER
        root.findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MenuItemAdder().show(getActivity().getFragmentManager(), "");
            }
        });

        //Test
        root.findViewById(R.id.add_item).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new FirebaseAssistant().syncMenu();
                return true;
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView parent_category;
        public TextView item_price;
        public Button enter;

        public ViewHolder(View itemView) {
            super(itemView);
            parent_category = itemView.findViewById(R.id.name_of_item);
            enter = itemView.findViewById(R.id.button4);
        }
    }

    void getMenu(){
        menuItems = MenuDataBase.getInstance(getActivity()).MenuDOA().getMenu();
    }


}
