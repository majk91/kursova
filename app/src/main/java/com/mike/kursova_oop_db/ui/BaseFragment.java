package com.mike.kursova_oop_db.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.mike.kursova_oop_db.ui.viewmodel.BaseViewModel;

public abstract class BaseFragment extends Fragment {

    protected BaseViewModel viewModel;

    protected OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            viewModel.fallback();
        }
    };
}
