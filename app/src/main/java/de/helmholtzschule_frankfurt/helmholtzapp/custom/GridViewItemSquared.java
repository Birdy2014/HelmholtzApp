package de.helmholtzschule_frankfurt.helmholtzapp.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class GridViewItemSquared extends RelativeLayout {

    public GridViewItemSquared(Context context) {
        super(context);
    }

    public GridViewItemSquared(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItemSquared(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}