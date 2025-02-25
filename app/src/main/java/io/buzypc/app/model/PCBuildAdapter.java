package io.buzypc.app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.buzypc.app.R;
import io.buzypc.app.data.PCModel;


public class PCBuildAdapter extends RecyclerView.Adapter<PCBuildAdapter.MyViewHolder>{
    Context context;
    ArrayList<io.buzypc.app.data.PCModel> pcModels;
    public PCBuildAdapter(Context context, ArrayList<PCModel> pcModels) {
        this.context = context;
        this.pcModels = pcModels;
    }

    @NonNull
    @Override
    public PCBuildAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_build_card , parent,false);
        return new PCBuildAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PCBuildAdapter.MyViewHolder holder, int position) {
        holder.textview.setText(pcModels.get(position).getPcName());
    }

    @Override
    public int getItemCount() {
        return pcModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textview;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textview = itemView.findViewById(R.id.textView_pcName);
        }
    }
}