package dk.simonsejse.loenberegning.models;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.utilities.DateUtil;

public class MyRecyclerViewAdapterForExtraAddition extends RecyclerView.Adapter<MyRecyclerViewAdapterForExtraAddition.MyViewHolder> {

    private final List<Extra> extraList;

    public MyRecyclerViewAdapterForExtraAddition(List<Extra> extraList){
        this.extraList = extraList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context content = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(content);
        final View view = layoutInflater.inflate(R.layout.item_row_for_extra_addition, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Extra extra = extraList.get(position);

        holder.extraAdditionTime.setText(String.format("%s-%s", extra.start.format(DateUtil.TIME), extra.end.format(DateUtil.TIME)));
        holder.extraAddition.setText(String.valueOf(extra.integer));
    }

    @Override
    public int getItemCount() {
        return extraList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView extraAdditionTime;
        private final TextView extraAddition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            extraAdditionTime = itemView.findViewById(R.id.extra_addition_time_text_view);
            extraAddition = itemView.findViewById(R.id.extra_addition_text_view);
        }
    }
}
