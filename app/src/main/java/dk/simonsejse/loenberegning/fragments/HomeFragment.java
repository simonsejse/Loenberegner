package dk.simonsejse.loenberegning.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding fragmentHomeBinding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        this.fragmentHomeBinding.addShiftButton.setOnClickListener(this::navigateToAddShiftsFragment);
        this.fragmentHomeBinding.infoButton.setOnClickListener(this::navigateToInfoFragment);
        this.fragmentHomeBinding.calculateWithHoursButton.setOnClickListener(this::navigateToCalculateWithHours);
        this.fragmentHomeBinding.settingsButton.setOnClickListener(this::navigateToSettingsFragment);
        this.fragmentHomeBinding.salaryHistoryButton.setOnClickListener(this::navigateToShiftHistoryFragment);
        return fragmentHomeBinding.getRoot();
    }

    private void navigateToInfoFragment(View view){
        this.navController.navigate(R.id.navigateFromHomeToInfo);
    }
    private void navigateToCalculateWithHours(View view) {
        this.navController.navigate(R.id.navigateFromHomeToCalcWithHours);
    }
    private void navigateToSettingsFragment(View v){
        this.navController.navigate(R.id.navigateFromHomeToSettings);
    }
    private void navigateToAddShiftsFragment(View v){
        this.navController.navigate(R.id.navigateFromHomeToAddShifts);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void navigateToShiftHistoryFragment(View v){
        this.navController.navigate(R.id.navigateFromHomeToAllSalaries);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }
}