package dk.simonsejse.loenberegning.models;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.PreferenceManagerUtil;

public class MyChildRecyclerViewAdapter extends RecyclerView.Adapter<MyChildRecyclerViewAdapter.MyViewHolder> {

    private final List<Shift> shiftList;
    private Context context;

    public MyChildRecyclerViewAdapter(List<Shift> shiftList){
        this.shiftList = shiftList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.item_row_for_all_salaries, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Shift shift = shiftList.get(position);

        holder.cardDate.setText(shift.getShiftStartAt().format(DateUtil.DATE_FORMAT_WITH_DASH));
        final LocalDateTime shiftStartAt = shift.getShiftStartAt();
        final LocalDateTime shiftEndsAt = shift.getShiftEndsAt();

        final Duration between = Duration.between(shiftStartAt, shiftEndsAt);

        int hourlyRate = PreferenceManagerUtil.getHourlyRate(context);

        int salaryPerShift = (int) (between.toHours() * hourlyRate) + hourlyRate * ((shiftEndsAt.getMinute() - shiftStartAt.getMinute())/60);

        holder.salaryPerShift.setText(String.format("%d", salaryPerShift));
        holder.workStart.setText(String.format("%s", shiftStartAt.format(DateUtil.DATE_TIME_FORMATTER)));
        holder.workEnd.setText(String.format("%s", shiftEndsAt.format(DateUtil.DATE_TIME_FORMATTER)));
        holder.hoursWorked.setText(String.format("%s timer og %s minutter", between.toHours(), Math.abs(shiftEndsAt.getMinute() - shiftStartAt.getMinute())));
        holder.imageView.setImageResource(ImageViewResourcesForAdapter.ICONS_RESOURCES[Math.toIntExact(between.toHours())]);
        holder.id.setText(String.valueOf(shift.getId()));
        holder.constraintLayout.setOnClickListener((view) -> {
            //TODO: fix when navigating to edit shift we have to use bundle
            final NavController navController = Navigation.findNavController(view);
            final Bundle bundle = new Bundle();
            bundle.putSerializable(EnumDateVarchar.WORK_START.getText(), shift.getShiftStartAt());
            bundle.putSerializable(EnumDateVarchar.WORK_END.getText(), shift.getShiftEndsAt());
            navController.navigate(R.id.navigateFromAllSalariesToEditShift, bundle);
        });
    }

    @Override
    public int getItemCount() {

        return shiftList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cardDate, hoursWorked, salaryPerShift, workStart, workEnd, id;
        private ImageView imageView;

        private ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardDate = itemView.findViewById(R.id.card_date);
            this.hoursWorked = itemView.findViewById(R.id.hours_worked);
            this.imageView = itemView.findViewById(R.id.clock_logo);
            this.salaryPerShift = itemView.findViewById(R.id.salary_per_shift_textview);
            this.workStart = itemView.findViewById(R.id.worked_start);
            this.workEnd = itemView.findViewById(R.id.worked_end);
            this.id = itemView.findViewById(R.id.id_tw);
            this.constraintLayout = itemView.findViewById(R.id.layout);
        }
    }
}
