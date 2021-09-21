package dk.simonsejse.loenberegning.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.simonsejse.loenberegning.databinding.FragmentInfoBinding;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.UrlUtil;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding fragmentInfoBinding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentInfoBinding = FragmentInfoBinding.inflate(inflater, container, false);

        this.fragmentInfoBinding.ageTextView.setText(String.format("%s år gammel", DateUtil.getSimonCurrentAge()));
        this.fragmentInfoBinding.goToGithubButton.setOnClickListener(this::goToGithubPageListener);
        this.fragmentInfoBinding.goToMailButton.setOnClickListener(this::goToMailPageListener);


        return this.fragmentInfoBinding.getRoot();
    }

    private void goToGithubPageListener(View view) {
        Uri uri = Uri.parse(UrlUtil.GITHUB_ADDRESS);
        Intent githubPageIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(githubPageIntent);
    }

    private void goToMailPageListener(View view){
        Uri uri = Uri.parse(UrlUtil.MAIL_ADDRESS);
        Intent mailPageIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(mailPageIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentInfoBinding = null;
    }
}