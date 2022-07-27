package Adapters;



import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.myactivities.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    private SharedPreferences pref;

    List<AllClientsAdapter> dataAdapters;

    public RecyclerViewAdapter(List<AllClientsAdapter> getDataAdapter, Context context){

        super();

        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        AllClientsAdapter dataAdapter =  dataAdapters.get(position);

        viewHolder.TextViewTnames.setText(dataAdapter.getFull_name());

        viewHolder.TextViewid.setText(String.valueOf(dataAdapter.getId_no()));


        viewHolder.TextViewhaddres.setText(dataAdapter.getHome_address());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView TextViewTnames;
        public TextView TextViewid;
        public TextView TextViewhaddres;


        public ViewHolder(View itemView) {

            super(itemView);

            TextViewTnames = (TextView) itemView.findViewById(R.id.textViewname) ;
            TextViewid = (TextView) itemView.findViewById(R.id.textViewid) ;
            TextViewhaddres=(TextView)itemView.findViewById(R.id.textViewhaddress);
        }
    }
}
