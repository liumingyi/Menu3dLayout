package top.liumingyi.menu3dlayout;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 菜单布局，
 * 类似开门的动画效果。
 *
 * 具体动画包含：向右移动 & 绕Y轴的旋转 & 高度缩小
 *
 * @author liumingyi
 */
public class Menu3dLayout extends ViewGroup {

  private static final String TAG = "Menu";

  /**
   * 默认缩放比
   */
  private static final float DEFAULT_SCALING = 0.9f;

  /**
   * 默认旋转度数，-10度
   */
  private static final float DEFAULT_ROTATION_ANGLE = -10;

  /**
   * 移动距离与屏幕宽度的比例
   */
  private float slideRangePerc = 0.45f;

  /**
   * 移动距离,根据{@link #slideRangePerc}计算而来
   */
  private int slideRange;

  /**
   * 进行移动的View
   */
  private View slideView;

  /**
   * 最终缩放比例
   */
  private float scaling = DEFAULT_SCALING;

  /**
   * 最终旋转角度
   */
  private float rotationAngle = DEFAULT_ROTATION_ANGLE;

  private boolean isOpen;

  private ViewDragHelper dragHelper;

  private class DragHelperCallback extends ViewDragHelper.Callback {

    @Override public boolean tryCaptureView(View child, int pointerId) {
      //return child == slideView;
      return false;//false 表示childView均不可拖拽
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      return left;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      float slideOffset = (float) left / slideRange;
      slideView.setScaleY(1 + slideOffset * (scaling - 1));
      slideView.setRotationY(rotationAngle * slideOffset);
    }
  }

  public Menu3dLayout(Context context) {
    this(context, null);
  }

  public Menu3dLayout(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Menu3dLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    Point size = new Point();
    Display display = null;
    if (wm != null) {
      display = wm.getDefaultDisplay();
    }
    if (display != null) {
      display.getSize(size);
    }
    int screenWidth = size.x;

    slideRange = (int) (slideRangePerc * screenWidth);

    dragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    //dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    setWillNotDraw(true);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (isOpen) {
      return;
    }
    //Log.d(TAG,"------onMeasure--------");
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childView = getChildAt(i);
      measureChild(childView, widthMeasureSpec, heightMeasureSpec);
    }
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (isOpen) {
      return;
    }
    //Log.d(TAG,"------onLayout--------");
    if (slideView == null) {
      slideView = getChildAt(1);
    }
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childView = getChildAt(i);
      childView.layout(left, top, right, bottom);
    }
  }

  @Override public void computeScroll() {
    if (dragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  public void openMenu() {
    if (isOpen) {
      return;
    }
    if (dragHelper.smoothSlideViewTo(slideView, slideRange, 0)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
    isOpen = true;
  }

  public void closeMenu() {
    if (!isOpen) {
      return;
    }
    if (dragHelper.smoothSlideViewTo(slideView, 0, 0)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
    isOpen = false;
  }

  public void toggle() {
    if (isOpen) {
      closeMenu();
    } else {
      openMenu();
    }
  }

  /**
   * 设置打开的宽度和屏幕宽度的比例
   */
  public void setSlideRangePerc(float slideRangePerc) {
    this.slideRangePerc = slideRangePerc;
  }

  /**
   * 设置高度的最终缩放比
   */
  public void setScaling(float scaling) {
    this.scaling = scaling;
  }

  /**
   * 设置打开的角度
   */
  public void setRotationAngle(float rotationAngle) {
    this.rotationAngle = rotationAngle;
  }
}
