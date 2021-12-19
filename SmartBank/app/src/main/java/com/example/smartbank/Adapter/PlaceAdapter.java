package com.example.smartbank.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbank.NearLocationInterface;
import com.example.smartbank.PlaceModel;
import com.example.smartbank.databinding.PlaceItemLayoutBinding;
import com.example.smartbank.R;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<PlaceModel> placeModels;
    private com.example.smartbank.NearLocationInterface nearLocationInterface;

    public PlaceAdapter(NearLocationInterface nearLocationInterface) {
        this.nearLocationInterface = nearLocationInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.place_item_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (placeModels != null) {
            PlaceModel placeModel = placeModels.get(position);
            holder.binding.setPlaceModel(placeModel);
            holder.binding.setListener(nearLocationInterface);
        }

    }

    @Override
    public int getItemCount() {
        if (placeModels != null)
            return placeModels.size();
        else
            return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPlaceModels(List<PlaceModel> placeModels) {
        this.placeModels = placeModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PlaceItemLayoutBinding binding;

        public ViewHolder(@NonNull PlaceItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

