package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.graphics.Typeface;
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

public class VendorOrders extends Fragment {

    private OnFragmentInteractionListener mListener;

    public VendorOrders() {
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
        View root =  inflater.inflate(R.layout.fragment_vendor_orders, container, false);


        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");


        //get orders and set recycler view to make it relevant
        ArrayList<Vendors_orders_model> vendor_orders = getVendorOrders(root , inflater , calligrapher);


        return root;
    }


    //This one method gets the data and updates the rv on any change call this once to get orders

    private ArrayList<Vendors_orders_model> getVendorOrders(final View root, final LayoutInflater inflater , final Calligrapher calligrapher){

        final ArrayList<Vendors_orders_model> orders = new ArrayList<>();


        final RecyclerView own_vendors = root.findViewById(R.id.order_vendors);
        own_vendors.setLayoutManager(new LinearLayoutManager(getActivity()));


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


                calligrapher.setFont(own_vendors,"fonts/Product Sans Bold.ttf");

                //set vendor text
                holder.name.setText(orders.get(holder.getAdapterPosition()).uid_vendor);

                //Set items
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(root.getContext(),android.R.layout.simple_list_item_1,
                        orders.get(holder.getAdapterPosition()).items);
                holder.items.setAdapter(adapter1);

                calligrapher.setFont(own_vendors,"fonts/Product Sans Bold.ttf");



            }

            @Override
            public int getItemCount() {
                return orders.size();
            }
        };


        FirebaseDatabase.getInstance().getReference("Businesses").child(FirebaseAuth.getInstance().getUid())
                .child("Vendors_info").child("Vendor_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot d:
                     dataSnapshots) {
                    orders.add(d.getValue(Vendors_orders_model.class));
                }

                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity() ," get orders"  + orders.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        own_vendors.setAdapter(adapter);


        return orders;

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
}
