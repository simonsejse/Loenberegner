package dk.simonsejse.loenberegning.fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.ShiftApplication;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FragmentAllSalariesBinding;
import dk.simonsejse.loenberegning.models.INotifyAdapterChangeCallback;
import dk.simonsejse.loenberegning.models.MyParentRecyclerViewAdapterForAllSalaries;
import dk.simonsejse.loenberegning.models.Section;
import dk.simonsejse.loenberegning.utilities.ParseUtil;

public class AllSalariesFragment extends Fragment implements INotifyAdapterChangeCallback {

    private FragmentAllSalariesBinding fragmentAllSalariesBinding;
    private NavController navController;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);

    //Parent Recycler View with sections

    private final MyParentRecyclerViewAdapterForAllSalaries parentAdapter = new MyParentRecyclerViewAdapterForAllSalaries(
            new ArrayList<>()
    );

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentAllSalariesBinding = FragmentAllSalariesBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        this.loadingDialog.startLoadingDialog();


        ShiftApplication.THREAD_1.submit(() -> {
            final List<Shift> shifts = new LinkedList<>();

            for(int i = 0; i<200; i++){
                LocalDateTime start = ParseUtil.parseToLocalDateTime("03/10-1999 10:40");
                LocalDateTime end = ParseUtil.parseToLocalDateTime("03/10-1999 16:40");
                final Shift shift = new Shift(start, end);
                shifts.add(shift);
            }

            final Map<Month, List<Shift>> sectionsAsMap = new TreeMap<>(
                    shifts.stream().collect(Collectors.groupingBy(shift -> shift.getShiftStartAt().getMonth()))
            );
            final List<Section> sections = new LinkedList<>();

            for(Map.Entry<Month, List<Shift>> monthListEntry : sectionsAsMap.entrySet()){
                sections.add(new Section(monthListEntry.getKey().name(), monthListEntry.getValue()));
            }
            this.performSectionUpdate(sections);

            /*
            final Stream<Shift> allShiftStream = shifts.stream();
            // final Stream<Shift> allShiftStream = MyApplication.database.shiftDao().getAllShifts().stream();
            final Stream<Map.Entry<Month, List<Shift>>> stream = allShiftStream
                    .collect(Collectors.groupingBy(shift -> shift.getShiftStartAt().getMonth()))
                    .entrySet()
                    .stream();
            final List<Section> collect = stream
                    //.sorted((m1, m2) -> m1.getKey().compareTo(m2.getKey()))
                    .map(monthListEntry -> new Section(monthListEntry.getKey().name(), monthListEntry.getValue()))
                    .collect(Collectors.toList());
             */
        });
        return this.fragmentAllSalariesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.setAdapter(this.parentAdapter);
        this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAllSalariesBinding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void performSectionUpdate(List<Section> sections) {
        this.parentAdapter.addSections(sections);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(parentAdapter::notifyDataSetChanged);
    }


}