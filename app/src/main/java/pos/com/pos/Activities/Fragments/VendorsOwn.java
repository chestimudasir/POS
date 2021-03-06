package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Models.Vendor;
import pos.com.pos.R;


public class VendorsOwn extends Fragment {

    private OnFragmentInteractionListener mListener;

    public VendorsOwn() {
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
        View root = inflater.inflate(R.layout.fragment_vendors_own, container, false);


        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");

        ArrayList<Vendor> vendors = getVendors();
        RecyclerView.Adapter adapter = setUpOwnVendors(root , inflater , calligrapher , vendors);



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


    private ArrayList<Vendor> getVendors(){

        final ArrayList<Vendor> vendors = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Businesses")
                .child(FirebaseAuth.getInstance().getUid()).child("Vendor_info")
                .child("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();for (DataSnapshot d:
                        snapshots) {
                    vendors.add(d.getValue(Vendor.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return vendors ;
    }

    //MIGHT NEED THIS MEHTOD OVERLOADING
    private void getVendors(final RecyclerView.Adapter adapter){

        final ArrayList<Vendor> vendors = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Businesses")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("Vendor_info")
                .child("Vendors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vendors.clear();

                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();for (DataSnapshot d:
                        snapshots) {
                    vendors.add(d.getValue(Vendor.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private RecyclerView.Adapter setUpOwnVendors(View root, final LayoutInflater inflater
            , final Calligrapher calligrapher, final ArrayList<Vendor> vendors ){

        final RecyclerView own_vendors = root.findViewById(R.id.your_vendors);
        own_vendors.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(inflater.inflate(R.layout.vendor_holder ,parent , false) );
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                calligrapher.setFont(own_vendors,"fonts/Product Sans Bold.ttf");

                //set vendor text
                holder.name.setText(vendors.get(holder.getAdapterPosition()).Name);
                calligrapher.setFont(holder.name,"fonts/Product Sans Bold.ttf");

            }

            @Override
            public int getItemCount() {
                return vendors.size();
            }
        };

        calligrapher.setFont(own_vendors,"fonts/Product Sans Bold.ttf");

        own_vendors.setAdapter(adapter);

        return adapter;

    }
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name , address , email , rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textView17);
            rating = itemView.findViewById(R.id.rating_vendor);

        }
    }

}
