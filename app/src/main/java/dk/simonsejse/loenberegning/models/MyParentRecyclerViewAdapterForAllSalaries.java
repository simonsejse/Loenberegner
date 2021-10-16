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
import dk.simonsejse.loenberegning.database.Shift;

public class MyParentRecyclerViewAdapterForAllSalaries extends RecyclerView.Adapter<MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder> {

    private final List<Section> sections;

    public MyParentRecyclerViewAdapterForAllSalaries(List<Section> sections) {
        super();
        this.sections = sections;
        System.out.println("THIS RUNS");
    }

    @NonNull
    @Override
    public MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View inflatedView = layoutInflater.inflate(R.layout.section_rows_for_all_salaries, parent, false);
        return new MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder(inflatedView);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyParentRecyclerViewAdapterForAllSalaries.ParentRecyclerViewHolder holder, int position) {
        System.out.println("THIS CODE NEVER RUNS?");
        final Section section = this.sections.get(position);
        holder.onBind(section);
    }

    @Override
    public int getItemCount() {
        System.out.println("dsda");
        return this.sections.size();
    }

    public static class ParentRecyclerViewHolder extends RecyclerView.ViewHolder{
        private final TextView sectionTitle, salaryPerMonth, totalAmountOfHoursMonthlyTextView;
        private final RecyclerView childRecyclerView;

        public ParentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.sectionTitle = itemView.findViewById(R.id.section_title_text_view);
            this.salaryPerMonth = itemView.findViewById(R.id.total_salary_for_month_text_view);
            this.totalAmountOfHoursMonthlyTextView = itemView.findViewById(R.id.totalAmountOfHoursMonthlyTextView);
            this.childRecyclerView = itemView.findViewById(R.id.section_row_items_child_recycler_view);
            this.childRecyclerView.setHasFixedSize(true);
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

            final MyChildViewAdapter adapter = new MyChildViewAdapter(shiftList);
            this.childRecyclerView.setAdapter(adapter);

            this.salaryPerMonth.setText(String.format("%d,-", salary));
            this.totalAmountOfHoursMonthlyTextView.setText(String.format("%dt og %dmin.", hoursWorked, minutesWorked));
            System.out.println("Am i ran?");
        }
    }
}
