package dk.simonsejse.loenberegning.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FragmentExtraAdditionBinding;
import dk.simonsejse.loenberegning.models.Extra;
import dk.simonsejse.loenberegning.models.MyRecyclerViewAdapterForExtraAddition;

public class ExtraAdditionFragment extends Fragment{

    private FragmentExtraAdditionBinding binding;
    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExtraAdditionBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        Shift shift = (Shift) getArguments().getSerializable("shift");
        System.out.println(shift.getWorkDate() +"det virker!");

        this.binding.floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = new ContextThemeWrapper(getContext(), R.style.MyOwnCustomMadeStyle_PopupMenu);
                PopupMenu popupMenu = new PopupMenu(context, ExtraAdditionFragment.this.binding.floatingActionButton);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                popupMenu.setForceShowIcon(true);
                menuInflater.inflate(R.menu.extra_addition_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this::onMenuItemClicked);
                popupMenu.show();
            }

            private boolean onMenuItemClicked(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.add_addition_menu_item:
                        DialogFragment dialogFragment = new DialogFragment(fromDialogFragmentModel -> {
                            System.out.println("Tilføjede et nyt tilæg med dataen!");
                            System.out.println(String.format("%s", fromDialogFragmentModel.toString()));
                        }, new Shift());
                        dialogFragment.show(getParentFragmentManager(), "signature");

                        return true;
                    default:
                        return false;
                }
            }
        });

        final List<Extra> extras = Arrays.asList(new Extra(LocalTime.now(), LocalTime.now().minusHours(20), 10), new Extra(LocalTime.now(), LocalTime.now().minusHours(3), 10));
        final MyRecyclerViewAdapterForExtraAddition adapter = new MyRecyclerViewAdapterForExtraAddition(extras);
        binding.recyclerViewForExtraAdditions.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        this.binding.recyclerViewForExtraAdditions.setAdapter(adapter);

        return binding.getRoot();
    }


}