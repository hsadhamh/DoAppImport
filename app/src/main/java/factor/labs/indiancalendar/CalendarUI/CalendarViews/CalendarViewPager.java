package factor.labs.indiancalendar.CalendarUI.CalendarViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hassanhussain on 9/29/2015.
 */
public class CalendarViewPager extends ViewPager {

    public CalendarViewPager(Context context) {
        super(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static enum TransformType {
        FLOW,
        DEPTH,
        ZOOM,
        SLIDE_OVER
    }


    public void setAnimationType(TransformType type){
        this.setPageTransformer(true, new DepthPageTransformer(type));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // find the first child view
        View view = getChildAt(0);
        if (view != null) {
            // measure the first child view with the specified measure spec
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    class DepthPageTransformer implements ViewPager.PageTransformer {

        private final TransformType mTransformType;

        DepthPageTransformer(TransformType transformType) {
            mTransformType = transformType;
        }

        private static final float MIN_SCALE_DEPTH = 0.75f;
        private static final float MIN_SCALE_ZOOM = 0.85f;
        private static final float MIN_ALPHA_ZOOM = 0.5f;
        private static final float SCALE_FACTOR_SLIDE = 0.85f;
        private static final float MIN_ALPHA_SLIDE = 0.35f;

        public void transformPage(View page, float position) {
            final float alpha;
            final float scale;
            final float translationX;

            switch (mTransformType) {
                case FLOW:
                    page.setRotationY(position * -30f);
                    return;

                case SLIDE_OVER:
                    if (position < 0 && position > -1) {
                        // this is the page to the left
                        scale = Math.abs(Math.abs(position) - 1) * (1.0f - SCALE_FACTOR_SLIDE) + SCALE_FACTOR_SLIDE;
                        alpha = Math.max(MIN_ALPHA_SLIDE, 1 - Math.abs(position));
                        int pageWidth = page.getWidth();
                        float translateValue = position * -pageWidth;
                        if (translateValue > -pageWidth) {
                            translationX = translateValue;
                        } else {
                            translationX = 0;
                        }
                    } else {
                        alpha = 1;
                        scale = 1;
                        translationX = 0;
                    }
                    break;

                case DEPTH:
                    if (position > 0 && position < 1) {
                        // moving to the right
                        alpha = (1 - position);
                        scale = MIN_SCALE_DEPTH + (1 - MIN_SCALE_DEPTH) * (1 - Math.abs(position));
                        translationX = (page.getWidth() * -position);
                    } else {
                        // use default for all other cases
                        alpha = 1;
                        scale = 1;
                        translationX = 0;
                    }
                    break;

                case ZOOM:
                    if (position >= -1 && position <= 1) {
                        scale = Math.max(MIN_SCALE_ZOOM, 1 - Math.abs(position));
                        alpha = MIN_ALPHA_ZOOM +
                                (scale - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM) * (1 - MIN_ALPHA_ZOOM);
                        float vMargin = page.getHeight() * (1 - scale) / 2;
                        float hMargin = page.getWidth() * (1 - scale) / 2;
                        if (position < 0) {
                            translationX = (hMargin - vMargin / 2);
                        } else {
                            translationX = (-hMargin + vMargin / 2);
                        }
                    } else {
                        alpha = 1;
                        scale = 1;
                        translationX = 0;
                    }
                    break;

                default:
                    return;
            }

            page.setAlpha(alpha);
            page.setTranslationX(translationX);
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
    }

}
