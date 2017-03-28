//package com.github.behavior;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
///**
// * Created by zoudong on 2017/3/25.
// * 1、某个view监听另一个view的状态变化，例如大小、位置、显示状态
// *      需要重写layoutDependsOn和onDependentViewChanged方法
// * activity_custom_behavior2、某个view监听CoordinatorLayout内NestedScrollingChild的接口实现类的滑动状态
// *     重写onStartNestedScroll和onNestedPreScroll方法。
// *     注意：是监听实现了NestedScrollingChild的接口实现类的滑动状态，这就可以解释为什么不能用ScrollView而用NestScrollView来滑动了
// */
//
//public class SimpleBehavior extends CoordinatorLayout.Behavior<View> {
//
//    private  int targetId;
//
//    public SimpleBehavior() {
//    }
//
//    public SimpleBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.simplebehavior);
//        for (int i = 0; i < a.getIndexCount(); i++) {
//            int attr = a.getIndex(i);
//            if(a.getIndex(i) == R.styleable.simplebehavior_target){
//                targetId = a.getResourceId(attr, -1);
//            }
//        }
//        a.recycle();
//    }
//
//    //  child    指的是 app:layout_behavior
//    //  dependency   指的是 需要 执行Behavior
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        return dependency.getId()==targetId;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        Log.e("zoudong", "onDependentViewChanged====" + "parent = [" + parent + "], child = [" + child + "], dependency = [" + dependency + "]");
////        child.setY(dependency.getY()+dependency.getHeight());
//        child.setTranslationY(dependency.getY()+dependency.getHeight());
//        return true;
//    }
//    private  float startY=-1;
//    /**
//     * child   指的是 CoordinatorLayout子布局  指的是 app:layout_behavior
//     * directTargetChild   指的是 CoordinatorLayout子布局  指的是 app:layout_behavior
//     * target  在滑动的View
//     */
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        Log.e("zoudong", "onStartNestedScroll====" + "child = [" + child + "], directTargetChild = [" + directTargetChild + "], target = [" + target + "], nestedScrollAxes = [" + nestedScrollAxes + "]");
//        if(startY == -1){
//            startY = child.getY();
//        }
//        Log.e("zoudong", "startY="+startY);
//        return (nestedScrollAxes& ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
//    }
//
//    /**
//     * @param child  同上
//     * @param target   同上
//     * @param dx     target  X轴上滑动的距离   往左滑>0  往右滑 <0
//     * @param dy      target  Y轴上滑动的距离   往上滑>0  往下滑 <0
//     * @param consumed  使用[0]应该设置为dx的距离所消耗,消耗[1]应该设置为dy所消耗的距离
//
//     */
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//        Log.e("zoudong", " consumed = [" + consumed[0] +"-----"+consumed[1]+ "]-->startY="+startY+"==="+target.getY());
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//        Log.e("zoudong", "target->"+child.getTranslationY());
////        consumed[1]=dy;
//        if (child.getTranslationY()!=0) {
//            consumed[1]=dy;
//        }
//     }
//    int offsetTotal = 0;
//    boolean scrolling = false;
//    public void offset(View child,int dy){
//        int old = offsetTotal;
//        int top = offsetTotal - dy;
//        top = Math.max(top, -child.getHeight());
//        top = Math.min(top, 0);
//        offsetTotal = top;
//        if (old == offsetTotal){
//            scrolling = false;
//            return;
//        }
//        int delta = offsetTotal-old;
//        child.setTranslationY(child.getTranslationY()+delta);
//        scrolling = true;
//    }
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//         super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//        offset(child,dyConsumed);
//    }
//}
