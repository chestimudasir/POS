package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Models.Vendor;
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
                ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.holder_explore ,parent , false) );

                //set view wide font
                calligrapher.setFont(viewHolder.itemView ,"fonts/Product Sans Bold.ttf");

                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {



                //set vendor text
                holder.category.setText(vendors.get(holder.getAdapterPosition()).category);


                //sub recycler view to show max of 3 items
                holder.items.setLayoutManager(new LinearLayoutManager(getActivity()));
                holder.items.setHasFixedSize(true);
                holder.items.setAdapter(new RecyclerView.Adapter<ViewHolder_sublist>() {
                    @NonNull
                    @Override
                    public ViewHolder_sublist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        ViewHolder_sublist viewHolder_sublist = new ViewHolder_sublist(inflater.inflate(R.layout.vendor_explore_sub_item_list , parent , false));
                        calligrapher.setFont(viewHolder_sublist.itemView ,"fonts/Product Sans Bold.ttf");
                        return viewHolder_sublist;

                    }

                    @Override
                    public void onBindViewHolder(@NonNull ViewHolder_sublist holder_sub, int position) {

                        holder_sub.name.setText(vendors.get(holder.getAdapterPosition()).vendors.get(holder_sub.getAdapterPosition()).Name);


                    }

                    @Override
                    public int getItemCount() {
                        return vendors.get(holder.getAdapterPosition()).vendors.size();
                    }
                });



            }

            @Override
            public int getItemCount() {
                return vendors.size();
            }
        };


        //get data from the database and sent notify the recycler view of the update once it's done
        FirebaseDatabase.getInstance().getReference("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Vendor vendor;
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot d:
                        dataSnapshots) {

                    //get vendor out of
                    vendor = d.getValue(Vendor.class);
                    vendor.category = d.getKey();

                    Iterable<DataSnapshot> vendors_sublist = d.getChildren();
                    vendor.vendors = new ArrayList<>();
                    for (DataSnapshot d1:
                         vendors_sublist) {

                        vendor.vendors.add(d1.getValue(Vendor.class));

                    }

                    vendors.add(vendor);
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

        TextView category;
        public RecyclerView items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            items = itemView.findViewById(R.id.vendors_explore_sub_list);
            category = itemView.findViewById(R.id.category);

        }
    }

    class ViewHolder_sublist extends RecyclerView.ViewHolder{

        TextView name , desc;

        public ViewHolder_sublist(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView10);
            desc = itemView.findViewById(R.id.textView18);


        }
    }
}
