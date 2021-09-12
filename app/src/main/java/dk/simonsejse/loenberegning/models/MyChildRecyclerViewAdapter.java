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
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;

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

        holder.cardDate.setText(shift.getWorkDate().atStartOfDay(TimeZone.getDefault().toZoneId()).format(DateUtil.DATES));
        final LocalTime shiftStartAt = shift.getShiftStartAt();
        final LocalTime shiftEndsAt = shift.getShiftEndsAt();

        final Duration between = Duration.between(shiftStartAt, shiftEndsAt);

        int hourlyRate = PreferenceManagerUtil.getHourlyRate(context);

        int salaryPerShift = (int) (between.toHours() * hourlyRate) + hourlyRate * ((shiftEndsAt.getMinute() - shiftStartAt.getMinute())/60);

        holder.salaryPerShift.setText(String.format("%d", salaryPerShift));
        holder.workFromTo.setText(String.format("%s » %s", shiftStartAt.toString(), shiftEndsAt.toString()));
        holder.hoursWorked.setText(String.format("%s timer og %s minutter", between.toHours(), Math.abs(shiftEndsAt.getMinute() - shiftStartAt.getMinute())));
        holder.imageView.setImageResource(ImageViewResourcesForAdapter.ICONS_RESOURCES[Math.toIntExact(between.toHours())]);

        holder.constraintLayout.setOnClickListener((view) -> {
            //TODO: fix when navigating to edit shift we have to use bundle
            final NavController navController = Navigation.findNavController(view);
            final Bundle bundle = new Bundle();
            bundle.putSerializable(EnumDateVarchar.WORK.getText(), shift.getWorkDate());
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
        private TextView cardDate, hoursWorked, salaryPerShift, workFromTo;
        private ImageView imageView;

        private ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardDate = itemView.findViewById(R.id.card_date);
            this.hoursWorked = itemView.findViewById(R.id.hours_worked);
            this.imageView = itemView.findViewById(R.id.clock_logo);
            this.salaryPerShift = itemView.findViewById(R.id.salary_per_shift_textview);
            this.workFromTo = itemView.findViewById(R.id.worked_from_to);
            this.constraintLayout = itemView.findViewById(R.id.layout);
        }
    }
}
