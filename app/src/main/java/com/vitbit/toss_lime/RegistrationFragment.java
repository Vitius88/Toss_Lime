package com.vitbit.toss_lime;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class RegistrationFragment extends Fragment {

    EditText edTxtLogin;
    EditText edTxtName;
    EditText edTxtLastName;
    EditText edTxtPass;
    AppCompatButton btnReg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        initView( view);
        setListeners();
        return view;
    }

    private void initView( View view) {
        //----------------------------------------------------------------------------------------
        edTxtLogin = view.findViewById( R.id.main_reg_edit_text_login);
        edTxtName = view.findViewById( R.id.main_reg_edit_text_name);
        edTxtLastName = view.findViewById( R.id.main_reg_edit_text_last_name);
        edTxtPass = view.findViewById( R.id.main_reg_edit_text_password);
        btnReg = view.findViewById( R.id.main_reg_btn_reg);
        //----------------------------------------------------------------------------------------
    }

    private void setListeners() {

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO проверки на правильность введённых данных

                //сохраняем данные в базу

                //-----возвращаемся на предыдущий фрагмент для авторизации----->>>>>>>>
                FragmentManager fm = getParentFragmentManager();
                fm.popBackStack();
                //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
        });
    }
}