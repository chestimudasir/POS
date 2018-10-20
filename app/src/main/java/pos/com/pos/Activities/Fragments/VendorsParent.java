package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.R;

public class VendorsParent extends Fragment {

    private OnFragmentInteractionListener mListener;

    public VendorsParent() {
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
        View root =  inflater.inflate(R.layout.fragment_vendors_parent, container, false);

        Calligrapher calligrapher = new Calligrapher(container.getContext());
        calligrapher.setFont(root , "fonts/Product Sans Bold.ttf");

        RecyclerView.Adapter adapter = setUpOwnVendors(root , inflater , calligrapher);



        return root;
    }

    RecyclerView.Adapter setUpOwnVendors(View root, final LayoutInflater inflater , final Calligrapher calligrapher){

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

            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };


        own_vendors.setAdapter(adapter);

        return adapter;

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
