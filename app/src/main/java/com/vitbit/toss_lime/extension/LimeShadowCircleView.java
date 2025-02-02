package com.vitbit.toss_lime.extension;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.vitbit.toss_lime.R;

public class LimeShadowCircleView extends View {

    private Paint blurPaint;
    private int circleRadius;
    private final int blurRadius = pxInDp(15);
    private final int dYShadow = pxInDp(4);

    public LimeShadowCircleView(Context context) {
        super(context);
        createBlurPaint(context);
    }

    public LimeShadowCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createBlurPaint(context);
    }

    public LimeShadowCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createBlurPaint(context);
    }


    /**
     * Создание и настройка кисти для рисования цветной тени
     * @param context для взятия цвета из папки res
     */
    private void createBlurPaint(Context context) {
        //создаём кисть
        blurPaint = new Paint();

        //устанавливаем цвет из папки ресурсов
        blurPaint.setColor(ContextCompat.getColor( context, R.color.lime));

        //устанавливаем прозрачность рисованя 40% как в проекте из figma
        blurPaint.setAlpha((255 / 100) * 40);

        // Установка фильтра размытия
        blurPaint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL));
    }

    /**
     * Переопределяем метод для получения ширины view и рассчёта circleRadius.
     * @param w новая ширина view.
     * @param h новая высота view.
     * @param oldw предыдущая ширина view.
     * @param oldh предыдущая высота view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        circleRadius = w / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //получаем картинку тени
        Bitmap shadowBitmap = drawShadow();

        //рисуем тень на канвасе view
        canvas.drawBitmap(shadowBitmap, -blurRadius, -blurRadius + dYShadow, new Paint());

        //рисуем установленный background
        super.onDraw(canvas);
    }

    /**
     * Рисование цветной тени.
     * dYShadow - смещение тени вниз
     * @return Bitmap - картинка с тенью
     */
    private Bitmap drawShadow() {
        //общая длина стороны, увеличенная из за тени
        int newSide = (circleRadius + blurRadius) * 2;

        //создаём растровое изображение
        Bitmap shadowBitmap = Bitmap.createBitmap(newSide, newSide + dYShadow, Bitmap.Config.ARGB_8888);
        Canvas shadowCanvas = new Canvas(shadowBitmap);

        //рисуем размытый круг
        shadowCanvas.drawCircle(newSide / 2, newSide / 2, circleRadius, blurPaint);

        //кисть для стирания
        Paint clearPaint = new Paint();
        clearPaint.setColor(Color.TRANSPARENT);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //вырезаем внутренний круг
        shadowCanvas.drawCircle(newSide / 2, (newSide / 2) - dYShadow, circleRadius, clearPaint);

        return shadowBitmap;
    }

    /**
     * Перевод пикселей в пиксели на плотность экрана
     * @param px пиксели
     * @return dp
     */
    private int pxInDp(float px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }
}
