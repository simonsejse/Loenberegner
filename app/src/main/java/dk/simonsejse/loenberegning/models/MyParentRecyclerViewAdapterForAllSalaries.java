package dk.simonsejse.loenberegning.models;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyParentRecyclerViewAdapterForAllSalaries extends RecyclerView.Adapter<MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder> {

    private final List<Section> sections;

    public MyParentRecyclerViewAdapterForAllSalaries(List<Section> sections) {
        this.sections = sections;
    }

    @NonNull
    @Override
    public MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ParentRecyclerViewHolder extends RecyclerView.ViewHolder{
        public ParentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
