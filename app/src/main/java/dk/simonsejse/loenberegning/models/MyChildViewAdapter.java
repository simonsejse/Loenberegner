package dk.simonsejse.loenberegning.models;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Optional;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.ShiftApplication;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.PreferenceManagerUtil;

public class MyChildViewAdapter extends RecyclerView.Adapter<MyChildViewAdapter.MyViewHolder> {

    private final List<Shift> shiftList;

    private Context context;

    public MyChildViewAdapter(List<Shift> shiftList){
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
        holder.onBind(shift);
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView cardDate, hoursWorked, workStart, workEnd, id;
        private final TextView salaryPerShift, extraAddition, additionCount, salarySum;
        private final ImageView imageView;
        private final ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardDate = itemView.findViewById(R.id.card_date);
            this.hoursWorked = itemView.findViewById(R.id.hours_worked);
            this.imageView = itemView.findViewById(R.id.clock_logo);
            this.workStart = itemView.findViewById(R.id.worked_start);
            this.workEnd = itemView.findViewById(R.id.worked_end);
            this.id = itemView.findViewById(R.id.id_tw);
            this.constraintLayout = itemView.findViewById(R.id.layout);
            this.salaryPerShift = itemView.findViewById(R.id.salary_text_view);
            this.extraAddition = itemView.findViewById(R.id.extra_addition_money_tw);
            this.additionCount = itemView.findViewById(R.id.extra_addition_count_tw);
            this.salarySum = itemView.findViewById(R.id.sum_salary_text_view);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onBind(Shift shift) {
            this.cardDate.setText(shift.getShiftStartAt().format(DateUtil.DATE_FORMAT_WITH_DASH));
            final LocalDateTime shiftStartAt = shift.getShiftStartAt();
            final LocalDateTime shiftEndsAt = shift.getShiftEndsAt();

            final Duration between = Duration.between(shiftStartAt, shiftEndsAt);

            int hourlyRate = PreferenceManagerUtil.getHourlyRate(context);

            int salaryPerShift = (int) (between.toHours() * hourlyRate) + hourlyRate * ((shiftEndsAt.getMinute() - shiftStartAt.getMinute())/60);

            Optional<Double> extraAdditionSalaryOptional = shift.getExtras()
                    .stream()
                    .map(extra -> {
                        final double additionMoneyPerSecond = (extra.integer / 60f) / 60f;
                        final double durationInSeconds = Duration.between(extra.start, extra.end).getSeconds();
                        return additionMoneyPerSecond * durationInSeconds;
                    })
                    .reduce(Double::sum);

            double extraAdditionSalary = extraAdditionSalaryOptional.isPresent() ? extraAdditionSalaryOptional.get() : 0;
            double sumSalary = salaryPerShift + extraAdditionSalary;

            this.workStart.setText(String.format("%s", shiftStartAt.format(DateUtil.DATE_TIME_FORMATTER)));
            this.workEnd.setText(String.format("%s", shiftEndsAt.format(DateUtil.DATE_TIME_FORMATTER)));
            this.hoursWorked.setText(String.format("%s timer og %s minutter", between.toHours(), Math.abs(shiftEndsAt.getMinute() - shiftStartAt.getMinute())));
            this.imageView.setImageResource(ImageViewResourcesForAdapter.ICONS_RESOURCES[Math.toIntExact(between.toHours())]);
            this.id.setText(String.valueOf(shift.getId()));

            this.salaryPerShift.setText(String.format("%d", salaryPerShift));
            this.extraAddition.setText(String.format("%.2f", extraAdditionSalary));
            this.additionCount.setText(String.valueOf(shift.getExtras().size()));
            this.salarySum.setText(String.format("%.2f", sumSalary));
            this.constraintLayout.setOnClickListener((view) -> {
                //TODO: fix when navigating to edit shift we have to use bundle
                final NavController navController = Navigation.findNavController(view);
                final Bundle bundle = new Bundle();
                bundle.putSerializable(BundleStoringKeyEnum.WORK_START.key, shift.getShiftStartAt());
                bundle.putSerializable(BundleStoringKeyEnum.WORK_END.key, shift.getShiftEndsAt());
                navController.navigate(R.id.navigateFromAllSalariesToEditShift, bundle);
            });
        }
    }
}
