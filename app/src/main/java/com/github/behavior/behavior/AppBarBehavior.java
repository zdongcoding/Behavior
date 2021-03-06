package com.github.behavior.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.behavior.R;

import static android.content.ContentValues.TAG;

/**
 * Created by zoudong on 2017/3/28.
 */

public class AppBarBehavior extends AppBarLayout.Behavior {

    private static final float TARGET_HEIGHT = 500; // 最大滑动距离
    private float mTotalDy;     // 总滑动的像素数
    private float mLastScale;   // 最终放大比例
    private int mLastBottom;    // AppBarLayout的最终Bottom值
    private int targetId=R.id.background;
    private int mParentHeight;
    private View mTargetView;
    private String tag = "img";
    private int mTargetViewHeight;
    private boolean isAnimate;

    public AppBarBehavior() {
    }

    public AppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.e("zoudong", "onStartNestedScroll====" + "parent = [" + parent + "], child = [" + child + "], directTargetChild = [" + directTargetChild + "], target = [" + target + "], nestedScrollAxes = [" + nestedScrollAxes + "]");
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        if (mParentHeight==0) {
            mParentHeight=child.getHeight();
        }
        isAnimate=true;
        Log.e("zoudong", "onNestedScrollAccepted===="+mParentHeight);
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        // 如果触发了快速滚动且垂直方向上速度大于100，则禁用动画
        if (velocityY > 100) {
            isAnimate = false;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        Log.e("zoudong", "onNestedPreScroll====" + "coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], dx = [" + dx + "], dy = [" + dy + "], consumed = [" + consumed + "]");
        // 1.mTargetView不为null
        // 2.是向下滑动，dy<0表示向下滑动
        // 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
//        if (mTargetView != null && dy < 0 && child.getBottom() >= mParentHeight) {
//            // 累加垂直方向上滑动的像素数
//            mTotalDy += -dy;
//            // 不能大于最大滑动距离
//            mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
//            // 计算目标View缩放比例，不能小于1
//            mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
//            // 缩放目标View
//            ViewCompat.setScaleX(mTargetView, mLastScale);
//            ViewCompat.setScaleY(mTargetView, mLastScale);
//            // 计算目标View放大后增加的高度
//            mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
//            // 修改AppBarLayout的高度
//            child.setBottom(mLastBottom);
//        } else {
//            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        }
     // 二
        if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight) || (dy > 0 && child.getBottom() > mParentHeight))) {
            scale(child, target, dy);
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("zoudong", "onNestedScroll====" + "coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], dxConsumed = [" + dxConsumed + "], dyConsumed = [" + dyConsumed + "], dxUnconsumed = [" + dxUnconsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
    private void scale(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);
        mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);
    }
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        // 如果触发了快速滚动且垂直方向上速度大于100，则禁用动画
        if (velocityY > 100) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private void recovery(final AppBarLayout abl) {
        if (mTotalDy > 0) {
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        ViewCompat.setScaleX(mTargetView, value);
                        ViewCompat.setScaleY(mTargetView, value);
                        abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
                    }
                });
                anim.start();
            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentHeight);
            }
        }
    }

//    @Override
//    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
//        Log.e("zoudong", "onNestedFling====" + "coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "], consumed = [" + consumed + "]");
//        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
//    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, AppBarLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
//        Log.e("zoudong", "onMeasureChild====" + "parent = [" + parent + "], child = [" + child + "], parentWidthMeasureSpec = [" + parentWidthMeasureSpec + "], widthUsed = [" + widthUsed + "], parentHeightMeasureSpec = [" + parentHeightMeasureSpec + "], heightUsed = [" + heightUsed + "]");
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        Log.e("zoudong", "onLayoutChild====" + "parent = [" + parent + "], abl = [" + abl + "], layoutDirection = [" + layoutDirection + "]");

        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        // 需要在调用过super.onLayoutChild()方法之后获取
        if (mTargetView == null) {
            mTargetView = parent.findViewWithTag(tag);
            if (mTargetView != null) {
                initial(abl);
            }
        }
        return handled;
    }
    private void initial(AppBarLayout abl) {
        // 必须设置ClipChildren为false，这样目标View在放大时才能超出布局的范围
        abl.setClipChildren(false);
        mParentHeight = abl.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
    }
}

