package com.vitbit.toss_lime;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vitbit.toss_lime.extension.LimeShadowCircleView;


public class StartFragment extends Fragment {

    Bundle argsLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        AppCompatButton btnStart = view.findViewById(R.id.main_start_btn_get_started);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----заменяем фрагмент---->>>>>>>>>>>>>>>>
                Fragment fragment = new LoginFragment();
                fragment.setArguments(argsLogo);
                FragmentManager fm = getParentFragmentManager();     //TODO правильно ли я получаю менеджер?
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_container_for_fragment, fragment, null)
                        .commit();
                //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                //-----стартовая анимация заднего фона с кругами----->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                ViewGroup ltBackCircles = view.findViewById(R.id.main_start_lt_back);
                LimeShadowCircleView imgCircleInCenter = view.findViewById(R.id.main_start_back_ring_blur);

                //определяем смещение фона за границы экрана
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ltBackCircles.getLayoutParams();
                int dXLayout = params.leftMargin;
                int dYLayout = params.topMargin;

                //оределяем центр вращения
                int[] pivot = new int[2];
                imgCircleInCenter.getLocationOnScreen(pivot);
                pivot[0] += imgCircleInCenter.getWidth() / 2;
                pivot[1] += imgCircleInCenter.getHeight() / 2;

                //вызываем метод из this класса
                animationBackground( ltBackCircles, pivot[0] - dXLayout, pivot[1] - dYLayout);
                //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


                //-----для анимации logo на следующем фрагменте----->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                ImageView imgLogo = view.findViewById(R.id.main_start_img_logo);

                //получаем координату расположения лого на экране
                int[] point = new int[2];
                imgLogo.getLocationOnScreen(point);

                //передаём для следующего фрагмента
                transferForAnimationLogo(point[0], point[1]);
                //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
        });
    }


    private void transferForAnimationLogo( int x, int y) {
        argsLogo = new Bundle();
        argsLogo.putInt("previousXLogo", x);
        argsLogo.putInt("previousYLogo", y);
        argsLogo.putBoolean("firstInit", true);
    }


    /**
     * Склейка из 3х анимаций в 1 сет и запуск на View.
     * @param view для работы анимации с её полотном
     * @param pivotX центр вращения по X
     * @param pivotY центр вращения по Y
     */
    private void animationBackground( View view, int pivotX, int pivotY) {
        AnimationSet animSet = new AnimationSet(false);

        Animation animZoom = new ScaleAnimation( 0.45f, 1.0f, 0.45f, 1.0f, pivotX, pivotY);
        animZoom.setDuration(2400);
        animSet.addAnimation(animZoom);

        Animation animRotate = new RotateAnimation( 180, 0, pivotX, pivotY);
        animRotate.setDuration(2400);
        animSet.addAnimation(animRotate);

        Animation animAlpha = new AlphaAnimation( -0.1f, 1.0f);
        animAlpha.setDuration(1600);
        animSet.addAnimation(animAlpha);

        view.startAnimation(animSet);
    }
}