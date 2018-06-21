package pos.com.pos.Activities.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class DashBoard extends Fragment {


    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter<Viewholder> horizontal;
    private RecyclerView.Adapter<Viewholder> vertical;

    public DashBoard() {
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
        View root = inflater.inflate(R.layout.fragment_dash_board, container, false);

        //Initialize Starting Views and processes
        init(root);

        //Set up RVs
        setUpRvHorizontal(root , inflater);
        setUpVerticalRv(root , inflater);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void setUpRvHorizontal(View root , final LayoutInflater inflater){
        RecyclerView recyclerView = root.findViewById(R.id.horizontalRvDash);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        horizontal = new RecyclerView.Adapter<Viewholder>() {
            @NonNull
            @Override
            public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Viewholder(inflater.inflate(R.layout.dashboard_horizontal , parent , false));
            }

            @Override
            public void onBindViewHolder(@NonNull Viewholder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 5;
            }
        };

        recyclerView.setAdapter(horizontal);
    }

    void setUpVerticalRv(View root , final LayoutInflater inflater){
        RecyclerView recyclerView = root.findViewById(R.id.veritcalRvDash);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        vertical = new RecyclerView.Adapter<Viewholder>() {
            @NonNull
            @Override
            public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Viewholder(inflater.inflate(R.layout.dashboard_vertical , parent , false));
            }

            @Override
            public void onBindViewHolder(@NonNull Viewholder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 5;
            }
        };

        recyclerView.setAdapter(vertical);
    }
    void refreshHorizontal(){
        horizontal.notifyDataSetChanged();
    }

    void init(View root){

        UserConfig.init(getActivity());

        UserConfig userConfig = new UserConfig();
        if (userConfig.getSetUpStatus() == 1){
            root.findViewById(R.id.complete_profile).setVisibility(View.GONE);
        }

        TextView tx = root.findViewById(R.id.textView10);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");
        tx.setText("Good Morning, Books and Bricks");
        tx.setTypeface(custom_font);

        TextView tx1 = root.findViewById(R.id.textView11);

        Typeface custom_font2 = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Product Sans Regular.ttf");
        tx1.setTypeface(custom_font2);
    }


    class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(View itemView) {
            super(itemView);
        }
    }
}
