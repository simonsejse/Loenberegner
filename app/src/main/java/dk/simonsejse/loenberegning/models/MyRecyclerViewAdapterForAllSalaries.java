package dk.simonsejse.loenberegning.models;

import android.annotation.SuppressLint;
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
import dk.simonsejse.loenberegning.utilities.PreferenceManagerUtil;

public class MyRecyclerViewAdapterForAllSalaries extends RecyclerView.Adapter<MyRecyclerViewAdapterForAllSalaries.MyRecyclerViewHolderForAllSalaries> {

    private final List<Section> sectionList;
    private int salaryPerHour;

    public MyRecyclerViewAdapterForAllSalaries(final List<Section> sectionList){
        this.sectionList = sectionList;

    }

    @NonNull
    @Override
    public MyRecyclerViewHolderForAllSalaries onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        this.salaryPerHour = PreferenceManagerUtil.getHourlyRate(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View inflatedView = layoutInflater.inflate(R.layout.section_rows_for_all_salaries, parent, false);
        return new MyRecyclerViewHolderForAllSalaries(inflatedView);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolderForAllSalaries holder, int position) {
        Section section = sectionList.get(position);

        final String sectionName = section.getSectionName();
        final List<Shift> shiftList = section.getShiftList();

        final Integer hoursWorked = shiftList.stream()
                .map(shift -> Math.abs(shift.getShiftEndsAt().getHour() - shift.getShiftStartAt().getHour()))
                .reduce(0, Integer::sum);

        final int minutesWorked = shiftList
                .stream()
                .map(shift -> Math.abs(shift.getShiftEndsAt().getMinute() - shift.getShiftStartAt().getMinute()))
                .reduce(0, Integer::sum);

        int salary = hoursWorked * this.salaryPerHour;

        holder.sectionTitle.setText(sectionName);

        final MyChildRecyclerViewAdapter adapter = new MyChildRecyclerViewAdapter(shiftList);
        holder.childRecyclerView.setAdapter(adapter);

        holder.salaryPerMonth.setText(String.format("%d,-", salary));
        holder.totalAmountOfHoursMonthlyTextView.setText(String.format("%dt og %dmin.", hoursWorked, minutesWorked));
    }

    @Override
    public int getItemCount() {
        return this.sectionList.size();
    }

    public class MyRecyclerViewHolderForAllSalaries extends RecyclerView.ViewHolder{

        private final TextView sectionTitle, salaryPerMonth, totalAmountOfHoursMonthlyTextView;
        private final RecyclerView childRecyclerView;

        public MyRecyclerViewHolderForAllSalaries(@NonNull View itemView) {
            super(itemView);
            this.sectionTitle = itemView.findViewById(R.id.section_title_text_view);
            this.childRecyclerView = itemView.findViewById(R.id.section_row_items_child_recycler_view);
            this.salaryPerMonth = itemView.findViewById(R.id.total_salary_for_month_text_view);
            this.totalAmountOfHoursMonthlyTextView = itemView.findViewById(R.id.totalAmountOfHoursMonthlyTextView);
        }
    }

}
