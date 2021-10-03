package dk.simonsejse.loenberegning.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FragmentExtraAdditionBinding;
import dk.simonsejse.loenberegning.directions.NavigateFromExtraToAdd;
import dk.simonsejse.loenberegning.models.MyRecyclerViewAdapterForExtraAddition;
import dk.simonsejse.loenberegning.utilities.AlertUtil;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;
import dk.simonsejse.loenberegning.utilities.IconUtil;
import dk.simonsejse.loenberegning.utilities.MessagesUtil;

public class ExtraAdditionFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    private FragmentExtraAdditionBinding fragmentExtraAdditionBinding;
    private NavController navController;

    private MyRecyclerViewAdapterForExtraAddition adapter;
    private Shift shift;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentExtraAdditionBinding = FragmentExtraAdditionBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);

        if (getArguments() == null){
            AlertUtil.send(getView(),"Fejl ved data", "Der mangler noget data for at kunne gå herind!", IconUtil.ERROR);
            this.navController.popBackStack();
        }else {
            this.shift = (Shift) getArguments().getSerializable(BundleStoringKeyEnum.SHIFT_KEY.key);

            this.fragmentExtraAdditionBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = new ContextThemeWrapper(getContext(), R.style.MyOwnCustomMadeStyle_PopupMenu);
                    PopupMenu popupMenu = new PopupMenu(context, ExtraAdditionFragment.this.fragmentExtraAdditionBinding.floatingActionButton);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    popupMenu.setForceShowIcon(true);
                    menuInflater.inflate(R.menu.extra_addition_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(ExtraAdditionFragment.this::onMenuItemClick);
                    popupMenu.show();
                }
            });

            this.adapter = new MyRecyclerViewAdapterForExtraAddition(shift.getExtras());
            fragmentExtraAdditionBinding.recyclerViewForExtraAdditions.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            this.fragmentExtraAdditionBinding.recyclerViewForExtraAdditions.setAdapter(adapter);
        }
        return fragmentExtraAdditionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.fragmentExtraAdditionBinding.addAdditionButton.setOnClickListener(this::addAdditionEvent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add_addition_menu_item:
                DialogFragment dialogFragment = new DialogFragment(fromDialogFragmentModel -> {
                    this.shift = fromDialogFragmentModel.shift;
                    ExtraAdditionFragment.this.adapter.notifyItemChanged(0);
                    AlertUtil.send(getView(),"Tilføjet tillæg", MessagesUtil.ADDITION_ADDED, IconUtil.ADDED);
                }, this.shift);
                dialogFragment.show(getParentFragmentManager(), "signature");
                return true;
            default:
                return false;
        }
    }

    protected void addAdditionEvent(View view){
        this.navController.navigate(new NavigateFromExtraToAdd(this.shift));
    }
}
