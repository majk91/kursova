package com.mike.kursova_oop_db.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.ui.viewmodel.AuthViewModel;

public class AuthLoginFragment extends BaseLoginFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_auth, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        viewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        initView(v);
        initAction();

        return v;
    }

    private void initView(View v){
        emailParent = v.findViewById(R.id.emailParent);
        passParent = v.findViewById(R.id.msgParent);
        passView = v.findViewById(R.id.enterPassView);
        emailView = v.findViewById(R.id.emailView);
        login = v.findViewById(R.id.loginBtn);
        toAnother = v.findViewById(R.id.regBtn);
    }

    private void initAction(){
        login.setOnClickListener(v -> {
            boolean error = false;
            if(!viewModel.validEmpty(emailView)){
                emailParent.setError("error!");
                error = true;
            }
            if(!viewModel.validEmpty(passView)){
                passParent.setError("error!");
                error = true;
            }
            if(!error){
                viewModel.auth(getContext(), emailView.getText().toString(), passView.getText().toString());
            }
        });

        toAnother.setOnClickListener(v -> {
            viewModel.navTo(R.id.nav_reg);
            if(viewModel.dUser!=null){
                viewModel.dUser.dispose();
                viewModel.dUser=null;
            }
        });
    }
}