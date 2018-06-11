package com.zhangyue.iReader.search.ui.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by wangqingfeng on 2018/6/7.
 */
public class TagTextView extends TextView {
    public TagTextView(Context context) {
        super(context);
    }

    public TagTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TagTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 修改textSize防止view的宽高改变
     * @param size
     */
    public void setTextSizeNotChangeViewWidth(float size) {
        float newSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getContext().getResources().getDisplayMetrics());
        if (getTextSize() != newSize) {
            Paint paint = new Paint();
            Rect rect = new Rect();
            String txt = getText().toString();

            //获取文本原来宽高
            paint.setTextSize(getTextSize());
            paint.getTextBounds(txt, 0, txt.length(), rect);
            int oldWidth = rect.right - rect.left;
            int oldHeight = rect.bottom - rect.top;

            //改变字体大小之后的宽高
            paint.setTextSize(newSize);
            paint.getTextBounds(txt, 0, txt.length(), rect);
            int newWidth = rect.right - rect.left;
            int newHeight = rect.bottom - rect.top;

            /* ------start  重新计算padding，防止整体宽高发生改变 ---------*/
            float k;
            if ((newWidth - oldWidth) % 2 != 0) {
                if (newWidth > oldWidth) {
                    k = 1.5f;
                } else {
                    k = -1.5f;
                }
            } else {
                k = 0f;
            }
            //左右 padding
            int newPaddingLR = getPaddingLeft() - (int) ((newWidth - oldWidth) / 2 + k);

            if ((newHeight - oldHeight) % 2 != 0) {
                if (newHeight > oldHeight) {
                    k = 1.5f;
                } else {
                    k = -1.5f;
                }
            } else {
                k = 0f;
            }
            //上下padding
            int newPaddingTB = getPaddingTop() - (int) ((newHeight - oldHeight) / 2 + k);
            /* ------end  重新计算padding，防止整体宽高发生改变 ---------*/

            Class superClass = TagTextView.class.getSuperclass().getSuperclass();
            try {
                Field mPaddingLeft = superClass.getDeclaredField("mPaddingLeft");
                Field mPaddingRight = superClass.getDeclaredField("mPaddingRight");
                Field mPaddingTop = superClass.getDeclaredField("mPaddingTop");
                Field mPaddingBottom = superClass.getDeclaredField("mPaddingBottom");
                mPaddingLeft.setAccessible(true);
                mPaddingLeft.set(this, newPaddingLR);
                mPaddingRight.setAccessible(true);
                mPaddingRight.set(this, newPaddingLR);
                mPaddingTop.setAccessible(true);
                mPaddingTop.set(this, newPaddingTB);
                mPaddingBottom.setAccessible(true);
                mPaddingBottom.set(this, newPaddingTB);

                //设置textSize
                setTextSize(size);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
