package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Models.Vendor;
import pos.com.pos.Activities.Models.Vendors_orders_model;
import pos.com.pos.R;

public class VendorsExplore extends Fragment {

    private OnFragmentInteractionListener mListener;

    public VendorsExplore() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vendors_explore, container, false);

        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");


        //get orders and set recycler view to make it relevant
        ArrayList<Vendor> vendor_orders = getVendorOrders(root , inflater , calligrapher);



        return root;
    }

    private ArrayList<Vendor> getVendorOrders(final View root, final LayoutInflater inflater , final Calligrapher calligrapher){

        final ArrayList<Vendor> vendors = new ArrayList<>();


        final RecyclerView vendors_explore = root.findViewById(R.id.explore_vendors);
        vendors_explore.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Get data via firebase static objects

        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.vendor_orders_holder ,parent , false) );

                //set view wide font
                calligrapher.setFont(viewHolder.itemView ,"fonts/Product Sans Bold.ttf");

                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



                //set vendor text
                holder.name.setText(vendors.get(holder.getAdapterPosition()).Name);

                //Set items
//                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(root.getContext(),android.R.layout.simple_list_item_1,
//                        vendors.get(holder.getAdapterPosition()).items);
//                holder.items.setAdapter(adapter1);




            }

            @Override
            public int getItemCount() {
                return vendors.size();
            }
        };


        FirebaseDatabase.getInstance().getReference("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot d:
                        dataSnapshots) {
                    vendors.add(d.getValue(Vendor.class));
                }

                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity() ," get orders"  + vendors.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        vendors_explore.setAdapter(adapter);


        return vendors;

    }


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

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name , address , email , rating;
        ListView items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            items = itemView.findViewById(R.id.items_order_vendor);
            name = itemView.findViewById(R.id.name_of_vendor_orders);
            rating = itemView.findViewById(R.id.rating_vendor);

        }
    }
}
