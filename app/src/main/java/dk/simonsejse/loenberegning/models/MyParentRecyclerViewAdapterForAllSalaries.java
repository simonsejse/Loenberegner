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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.exceptions.ExplosionException;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;
import dk.simonsejse.loenberegning.utilities.DateUtil;

public class MyParentRecyclerViewAdapterForAllSalaries extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum VIEW_TYPES{
        MONTHS(0),
        SHIFTS(1);

        private final int num;
        VIEW_TYPES(int num){
            this.num = num;
        }
    }

    private final List<Section> sections;

    public MyParentRecyclerViewAdapterForAllSalaries(List<Section> sections) {
        super();
        this.sections = sections;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);


        final Optional<VIEW_TYPES> first = Arrays
                .stream(VIEW_TYPES.values())
                .filter(enumValue -> enumValue.num == viewType)
                .findFirst();

        if (first.isPresent()) {
            final VIEW_TYPES view_types = first.get();
            switch (view_types) {
                case MONTHS:
                    final View inflatedView = layoutInflater.inflate(R.layout.section_rows_for_all_salaries, parent, false);
                    return new MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder(inflatedView);
                case SHIFTS:
                    final View view = layoutInflater.inflate(R.layout.item_row_for_all_salaries, parent, false);
                    return new MyShiftViewHolder(view);
            }
        }
        throw new ExplosionException("spdokas");
    }


    @Override
    public int getItemViewType(int position) {
        //Section 1
        //  *Shift 1
        //  *Shift 2
        //  *Shift 3
        //Section 2
        //  *Shift 1
        //  *Shift 2
        //  *Shift 3
        int counter = 0;
        if (sections.size() == 0) return 0;
        for(int i = 0; i < sections.size(); i++){
            for(int j = 0; j < sections.get(i).getShiftList().size(); j++){
                if(++counter == position) return VIEW_TYPES.SHIFTS.ordinal();
            }
            if (++counter == position) return VIEW_TYPES.MONTHS.ordinal();
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return this.sections.stream().map(section -> 1+section.getShiftList().size()).reduce(Integer::sum).get();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentRecyclerViewHolder){
            ParentRecyclerViewHolder parentRecyclerViewHolder = (ParentRecyclerViewHolder) holder;
            final Section section = this.sections.get(position);
            parentRecyclerViewHolder.onBind(section);
        }else if (holder instanceof MyShiftViewHolder){
            System.out.println("Hi mf");
            MyShiftViewHolder myShiftViewHolder = (MyShiftViewHolder) holder;
            int counter = 0;
            for(int i = 0; i<sections.size(); i++){
                for(int j = 0; j < sections.get(i).getShiftList().size(); j++){
                    System.out.println(j);
                    System.out.println(counter);
                    if (counter++ == position) myShiftViewHolder.onBind(sections.get(i).getShiftList().get(j));
                }
            }
        }
    }

    public static class ParentRecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView sectionTitle, salaryPerMonth, totalAmountOfHoursMonthlyTextView;

        public ParentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.sectionTitle = itemView.findViewById(R.id.section_title_text_view);
            this.salaryPerMonth = itemView.findViewById(R.id.total_salary_for_month_text_view);
            this.totalAmountOfHoursMonthlyTextView = itemView.findViewById(R.id.totalAmountOfHoursMonthlyTextView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onBind(Section section) {
            final String sectionName = section.getSectionName();
            final List<Shift> shiftList = section.getShiftList();

            final Integer hoursWorked = shiftList.stream()
                    .map(shift -> Math.abs(shift.getShiftEndsAt().getHour() - shift.getShiftStartAt().getHour()))
                    .reduce(0, Integer::sum);

            final int minutesWorked = shiftList
                    .stream()
                    .map(shift -> Math.abs(shift.getShiftEndsAt().getMinute() - shift.getShiftStartAt().getMinute()))
                    .reduce(0, Integer::sum);

            int salary = hoursWorked * 114; //TODO: this.salaryPerHour;

            this.sectionTitle.setText(sectionName);

            this.salaryPerMonth.setText(String.format("%d,-", salary));
            this.totalAmountOfHoursMonthlyTextView.setText(String.format("%dt og %dmin.", hoursWorked, minutesWorked));
            System.out.println("Am i ran?");
        }
    }


    public class MyShiftViewHolder extends RecyclerView.ViewHolder {
        private final TextView cardDate, hoursWorked, workStart, workEnd, id;
        private final TextView salaryPerShift, extraAddition, additionCount, salarySum;
        private final ImageView imageView;
        private final ConstraintLayout constraintLayout;

        public MyShiftViewHolder(@NonNull View itemView) {
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

            //int hourlyRate = PreferenceManagerUtil.getHourlyRate(context);
            int hourlyRate=160;


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
