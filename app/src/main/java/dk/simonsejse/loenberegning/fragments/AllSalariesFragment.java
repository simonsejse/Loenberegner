package dk.simonsejse.loenberegning.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;

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
import dk.simonsejse.loenberegning.models.MyParentRecyclerViewAdapterForAllSalaries;
import dk.simonsejse.loenberegning.models.Section;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;
import dk.simonsejse.loenberegning.utilities.ParseUtil;

public class AllSalariesFragment extends Fragment {

    private FragmentAllSalariesBinding fragmentAllSalariesBinding;
    private NavController navController;

    private MyParentRecyclerViewAdapterForAllSalaries parentAdapter;
    private List<Section> sections;



    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentAllSalariesBinding = FragmentAllSalariesBinding.inflate(inflater, container, false);

        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        this.sections = new ArrayList<>();
        this.parentAdapter = new MyParentRecyclerViewAdapterForAllSalaries(this.sections);

        final List<Shift> shifts = new ArrayList<>();

        for(int i = 0; i<200; i++){
            LocalDateTime start = ParseUtil.parseToLocalDateTime("03/10-1999 10:40");
            LocalDateTime end = ParseUtil.parseToLocalDateTime("03/10-1999 16:40");
            final Shift shift = new Shift(start, end);
            shifts.add(shift);
        }

        final Map<Month, List<Shift>> sectionsAsMap = new TreeMap<>(
                shifts.stream()
                        .collect(Collectors.groupingBy(shift -> shift.getShiftStartAt().getMonth()))
        );
        final List<Section> sections = new LinkedList<>();

        for(Map.Entry<Month, List<Shift>> monthListEntry : sectionsAsMap.entrySet()){
            sections.add(new Section(monthListEntry.getKey().name(), monthListEntry.getValue()));
        }
        this.sections.addAll(sections);
        this.parentAdapter.notifyDataSetChanged();


        /*if (getArguments() != null){
            this.sections = (List<Section>) getArguments().getSerializable(BundleStoringKeyEnum.ALL_SECTIONS_LIST.key);
        }

         */

        /**
         * Initialise our parentAdapter passing through our section list
         * This parent adapter is for the main recyclerview, that we only holds a recyclerview inside the layout
         * where each row is a layout file containing another RecyclerView
         * so each row basically represent a section with a title and a RecyclerView
         * And inside that Child Recyclerview we then have a ChildViewAdapter.class for our last item
         */


        return this.fragmentAllSalariesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println(fragmentAllSalariesBinding);
        System.out.println(fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries);

        this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.setAdapter(this.parentAdapter);
        this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.setLayoutManager(new LinearLayoutManager(getContext()));
        this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAllSalariesBinding = null;
    }

}