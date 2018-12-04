package com.hencoder.hencoderpracticedraw7.practice.practice00;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw7.R;

/**
 * Created by huangyong on 2018/12/4
 * 谷歌图片三维动画效果（通过翻转camera以及移动画布来实现）
 *
 * 思路：将图片分为左右两个部分，左边为不翻转，右边为翻转
 * （所有操作均在画布中心点平移到原点后进行的操作，因此操作完后需要将画布平移回来；
 * 如果画布还有别的改变状态的操作，如旋转等，则需要在结束后将画布旋转回来）
 * 1、首先将右半部分向上翻转，左半部分不翻转
 * 2、将右半部分旋转-270°（绕Z轴原点，即中心点），左半部分做相应的旋转（即将画布旋转270°）
 * 3、旋转270度后，此时左半部分在上面，将左半部分向上翻转，右半部分不动
 *
 * 备注：旋转其实是画布在旋转，而图片并没有旋转，通过画布的旋转来达到分别绘制图片不同部分的动画效果
 */
public class PracticeMyAnimatorView extends View {
    private Paint paint;
    private Bitmap bitmap;
    private Camera camera;
    private Rect rectNoRotate;
    private Rect rectRotate;

    //camera翻转的角度（Y轴方向翻转）
    private int degreeY;
    //canvas旋转的角度（Z轴方向旋转）
    private int degreeZ;
    //最后图片上半部分翻转角度（X轴方向翻转）
    private int degreeX;

    private AnimatorSet animatorSet;

    public PracticeMyAnimatorView(Context context) {
        this(context, null);
    }

    public PracticeMyAnimatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PracticeMyAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        rectNoRotate = new Rect();
        rectRotate = new Rect();

        camera = new Camera();

        //起始右侧翻转角度45°
        ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "degreeY", 0, -45);
        //右侧旋转270°，左侧也配合旋转
        ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "degreeZ", 0, -270);
        //图片上半部分翻转45°
        ObjectAnimator animator3 = ObjectAnimator.ofInt(this, "degreeX", 0, -30);
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(1200);
        animatorSet.playSequentially(animator1, animator2, animator3);
    }

    public void start() {
        animatorSet.start();
    }

    public void end() {
        animatorSet.end();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;
        int x = width / 2 - bitmap.getWidth() / 2;
        int y = height / 2 - bitmap.getHeight() / 2;

        //备注：这里的绘制一定要严格注意画布的平移以及画布的旋转的先后顺序

        //绘制不翻转部分
        canvas.save();
        camera.save();
        //将画布中心平移到原点
        canvas.translate(centerX, centerY);
        //旋转画布
        canvas.rotate(degreeZ);
        //备注：由于画布旋转了-270度，此时Y轴的正方向为水平正方向，因此最后图片上半部分翻转45°应该将相机沿Y轴旋转
        //此时Y轴的正向旋转方向与X轴的正向旋转方向相反，需要对旋转的度数取反
        camera.rotateY(-degreeX);
        rectNoRotate.set(-centerX, -centerY, 0, centerY);
        canvas.clipRect(rectNoRotate);
        camera.applyToCanvas(canvas);
        //将画布旋转回来（该操作代码必须要在将画布移回原位代码之前，若放在后面，则旋转时需要调用带旋转中心点参数的方法rotate(degree,x,y)）
        canvas.rotate(-degreeZ);
        //将画布移到原位置
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        //绘制翻转部分
        canvas.save();
        camera.save();
        //将画布中心移到原点
        canvas.translate(centerX, centerY);
        //旋转画布
        canvas.rotate(degreeZ);
        camera.rotateY(degreeY);
        rectRotate.set(0, -centerY, centerX, centerY);
        canvas.clipRect(rectRotate);
        camera.applyToCanvas(canvas);
        //将画布旋转回来
        canvas.rotate(-degreeZ);
        //将画布移回原位置
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }

    public int getDegreeY() {
        return degreeY;
    }

    public void setDegreeY(int degreeY) {
        this.degreeY = degreeY;
        invalidate();
    }

    public int getDegreeZ() {
        return degreeZ;
    }

    public void setDegreeZ(int degreeZ) {
        this.degreeZ = degreeZ;
        invalidate();
    }

    public int getDegreeX() {
        return degreeX;
    }

    public void setDegreeX(int degreeX) {
        this.degreeX = degreeX;
        invalidate();
    }
}
