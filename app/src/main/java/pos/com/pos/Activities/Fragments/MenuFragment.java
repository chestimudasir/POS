package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pos.com.pos.Activities.Database.OrdersDatabase.MenuDatabase.MenuDataBase;
import pos.com.pos.Activities.DialogFragments.MenuItemAdder;
import pos.com.pos.R;

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

        //Get ALL Menu items
        getMenu();

        RecyclerView menu = root.findViewById(R.id.menuRv);
        menu.setLayoutManager(new LinearLayoutManager(getActivity()));

        //SET ADAPTER
        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.menu_holder, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                    holder.item_name.setText(menuItems[holder.getAdapterPosition()].item_name);
                    holder.item_price.setText(String.valueOf(menuItems[holder.getAdapterPosition()].item_price));

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

        public TextView item_name;
        public TextView item_price;

        public ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.name_of_item);
            item_price = itemView.findViewById(R.id.price_of_item);
        }
    }

    void getMenu(){
        menuItems = MenuDataBase.getInstance(getActivity()).MenuDOA().getMenu();
    }


}
