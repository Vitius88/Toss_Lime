package com.vitbit.toss_lime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class LoginFragment extends Fragment {

    private ImageView imgLogo;
    private View[] circles;
    private Thread threadOpenCircles;

    private EditText edTxtLogin;
    private EditText edTxtPass;
    private AppCompatButton btnLogin;
    private TextView txtRegistration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        effectCircleFade();
        setListeners();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle argsLogo = getArguments();
        view.post(new Runnable() {
            @Override
            public void run() {
                if(argsLogo != null) {
                    if( argsLogo.getBoolean("firstInit")) {
                        //прячем все круги
                        for(View circle:circles){
                            circle.setScaleX( 0.0f);
                            circle.setScaleY( 0.0f);
                        }

                        //картинка logo
                        int previousX = argsLogo.getInt("previousXLogo");
                        int previousY = argsLogo.getInt("previousYLogo");

                        int[] point = new int[2];
                        imgLogo.getLocationOnScreen(point);

                        int x = point[0];
                        int y = point[1];

                        createAnimationLogo(previousX, previousY, x, y);

                        argsLogo.putBoolean( "firstInit", false);
                    }
                }
            }
        });
    }

    private void initView(View view) {
        imgLogo = view.findViewById(R.id.main_login_img_logo);
        circles = new View[] {
                view.findViewById( R.id.main_login_circle_1),
                view.findViewById( R.id.main_login_circle_2),
                view.findViewById( R.id.main_login_circle_3),
                view.findViewById( R.id.main_login_circle_4)
        };

        //----------------------------------------------------------------------------------------
        edTxtLogin = view.findViewById( R.id.main_login_edit_text_login);
        edTxtPass = view.findViewById( R.id.main_login_edit_text_password);
        btnLogin = view.findViewById( R.id.main_login_btn_login);         //TODO установить слушатель, метод в конце класса
        txtRegistration = view.findViewById( R.id.main_login_btn_registration);//установлен слушатель, метод в самом низу
        //----------------------------------------------------------------------------------------
    }

    //трансляция(анимация) logo
    private void createAnimationLogo( int previousX, int previousY, int x, int y) {
        int dX = previousX - x;
        int dY = previousY - y;

        Animation anim = new TranslateAnimation( dX, 0, dY, 0);
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {
                                      @Override
                                      public void onAnimationStart(Animation animation) {}
                                      @Override
                                      public void onAnimationRepeat(Animation animation) {}
                                      @Override
                                      public void onAnimationEnd(Animation animation) { threadOpenCircles.start(); }
                                  });
        imgLogo.startAnimation(anim);
    }

    private void effectCircleFade() {

        //поток поочерёдно открывающий круги
        //старт потока вызывает конец анимации logo
        threadOpenCircles = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (i < circles.length){

                    View circle = circles[i++];

                    try {
                        Thread.sleep( i != circles.length ? 60 : 360);
                    } catch (InterruptedException e) {
                        //здесь кода нет
                    }

                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circle.animate()
                                        .scaleX(1.2f)
                                        .scaleY(1.2f)
                                        .setDuration(200)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                circle.animate().scaleX(1.0f).scaleY(1.0f).setDuration(140).start();
                                                animation.cancel();
                                            }
                                        })
                                        .start();
                            }
                        });
                    }
                }
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        txtRegistration.setClickable(true);
        txtRegistration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    ((TextView) v).setTextColor(Color.BLUE);
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (getView() != null)
                        ((TextView) v).setTextColor(ContextCompat.getColor(getView().getContext(), R.color.chat_darken));

                    //-----заменяем фрагмент---->>>>>>>>>>>>>>>>
                    Fragment fragment = new RegistrationFragment();
                    FragmentManager fm = getParentFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.main_container_for_fragment, fragment, null)
                            .addToBackStack(null)
                            .commit();
                    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                }

                return false;
            }
        });

        //TODO кнопка авторизации
        //btnLogin.setOnClickListener();
        //в слушателе условия и создание нового активити
    }
}