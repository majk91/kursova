package com.mike.kursova_oop_db.ui.login;

import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.mike.kursova_oop_db.ui.viewmodel.AuthViewModel;

public abstract class BaseLoginFragment extends Fragment {

    protected TextInputLayout emailParent, passParent;
    protected EditText passView, emailView;
    protected Button login, toAnother;

    protected AuthViewModel viewModel;

    protected OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            viewModel.fallback();
        }
    };
}
