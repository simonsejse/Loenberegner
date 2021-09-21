package dk.simonsejse.loenberegning.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.room.Room;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.AppDatabase;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FragmentAllSalariesBinding;
import dk.simonsejse.loenberegning.models.MyRecyclerViewAdapterForAllSalaries;
import dk.simonsejse.loenberegning.models.Section;

public class AllSalariesFragment extends Fragment {

    private static final String TAG = "AllSalariesFragment";

    private FragmentAllSalariesBinding fragmentAllSalariesBinding;
    private NavController navController;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentAllSalariesBinding = FragmentAllSalariesBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        final CompletableFuture<List<Section>> completableFuture = CompletableFuture.supplyAsync(() -> {
            AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
            return db.shiftDao().getAllShifts().stream()
                    .collect(Collectors.groupingBy(shift -> shift.getShiftStartAt().getMonth()))
                    .entrySet()
                    .stream()
                    .sorted((m1, m2) -> m1.getKey().compareTo(m2.getKey()))
                    .map(monthListEntry -> new Section(monthListEntry.getKey().name(), monthListEntry.getValue()))
                    .collect(Collectors.toList());
        });

        try {
            final List<Section> sections = completableFuture.get();

            final MyRecyclerViewAdapterForAllSalaries adapter = new MyRecyclerViewAdapterForAllSalaries(sections);

            this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.setAdapter(adapter);
            this.fragmentAllSalariesBinding.sectionRecyclerViewForAllSalaries.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.fragmentAllSalariesBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAllSalariesBinding = null;
    }
}