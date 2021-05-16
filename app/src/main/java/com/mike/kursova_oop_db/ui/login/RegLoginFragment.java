package com.mike.kursova_oop_db.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.ui.viewmodel.AuthViewModel;

public class RegLoginFragment extends BaseLoginFragment {

    private TextInputLayout nameParent, emailParent, passParent;
    private EditText passView, emailView, nameView;
    private Button login, toAnother;

    private AuthViewModel viewModel;

    protected OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            viewModel.fallback();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_reg, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        viewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        initView(v);
        initAction();

        return v;
    }

    private void initView(View v){
        nameParent = v.findViewById(R.id.nameParent);
        emailParent = v.findViewById(R.id.emailParent);
        passParent = v.findViewById(R.id.msgParent);
        nameView = v.findViewById(R.id.nameView);
        passView = v.findViewById(R.id.enterPassView);
        emailView = v.findViewById(R.id.emailView);
        login = v.findViewById(R.id.loginBtn);
        toAnother = v.findViewById(R.id.regBtn);
    }

    private void initAction(){
        login.setOnClickListener(v -> {
            boolean error = false;
            if(!viewModel.validEmpty(nameView)){
                nameParent.setError("error!");
                error = true;
            }
            if(!viewModel.validEmpty(emailView)){
                emailParent.setError("error!");
                error = true;
            }
            if(!viewModel.validEmpty(passView)){
                passParent.setError("error!");
                error = true;
            }
            if(!error){
                // попытка регистрации + вход
                viewModel.reg(getContext(),
                        nameView.getText().toString(),
                        emailView.getText().toString(),
                        passView.getText().toString()
                );
            }
        });

        toAnother.setOnClickListener(v -> {
            viewModel.navTo(R.id.nav_auth);
        });
    }
}