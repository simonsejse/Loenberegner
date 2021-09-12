package dk.simonsejse.loenberegning.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.databinding.FragmentCalcWithHoursBinding;
import dk.simonsejse.loenberegning.databinding.FragmentHomeBinding;
import dk.simonsejse.loenberegning.utilities.PreferenceUtil;

public class CalcWithHoursFragment extends Fragment {

    private FragmentCalcWithHoursBinding fragmentCalcWithHoursBinding;
    private NavController navController;

    private int salary;
    private double skatInProcent;
    private int fradrag;

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentCalcWithHoursBinding = FragmentCalcWithHoursBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);
        try{
            salary = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PreferenceUtil.hourlyRate.getText(), "1"));
            skatInProcent = Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PreferenceUtil.skatInProcent.getText(), "1"));
            fradrag = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(PreferenceUtil.fradrag.getText(), "1"));
        }catch(NumberFormatException e){
            this.navController.popBackStack();
            //Snackbar.make(, "Du har indtastet værdier som ikke er tal", Snackbar.LENGTH_LONG).show();
        }
        this.fragmentCalcWithHoursBinding.salaryTextView2.setText(String.format("%d%s", salary, getString(R.string.moneySymbol)));
        this.fragmentCalcWithHoursBinding.skatTextView2.setText(String.format("%s%s", skatInProcent, getString(R.string.skatInProcentSymbol)));
        this.fragmentCalcWithHoursBinding.fradragTextView2.setText(String.format("%d%s", fradrag, getString(R.string.moneySymbol)));

        this.fragmentCalcWithHoursBinding.calculateMoneyButton.setOnClickListener(this::onCalculateSalaryButtonClickEvent);

        return fragmentCalcWithHoursBinding.getRoot();
    }

    private void onCalculateSalaryButtonClickEvent(View view){
        this.fragmentCalcWithHoursBinding.progressCircular.setVisibility(View.VISIBLE);
        final int success_calculate_salary_text_1 = R.string.success_calculate_salary_text_1;
        String hours = this.fragmentCalcWithHoursBinding.amountOfHoursTextInputField.getEditText().getText().toString();
        int hoursWorked;

        try{
            hoursWorked = Integer.parseInt(hours);
        }catch(NumberFormatException nfe){
            this.fragmentCalcWithHoursBinding.progressCircular.setVisibility(View.INVISIBLE);
            return;
        }

        final int i = hoursWorked * salary;
        final int minusFradrag = i - fradrag;
        final double subtractSkat = minusFradrag * (skatInProcent / 100);
        double finalSalary = fradrag + minusFradrag - subtractSkat;

        new Handler().postDelayed(() -> {
            this.fragmentCalcWithHoursBinding.successCalculateSalaryTextView.setText(success_calculate_salary_text_1);
            this.fragmentCalcWithHoursBinding.successCalculateSalaryTextView2.setText(String.valueOf(finalSalary));
            this.fragmentCalcWithHoursBinding.progressCircular.setVisibility(View.INVISIBLE);
        },1000*3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentCalcWithHoursBinding = null;
    }
}