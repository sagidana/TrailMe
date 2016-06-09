package depton.trailme.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class ARCenaTextView extends TextView {
    public ARCenaTextView(Context context) {
        super(context);
        SetFont(context);
    }

    public ARCenaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetFont(context);
    }

    public ARCenaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetFont(context);
    }

    public void SetFont(Context context){
        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "/fonts/ARCENA.ttf");
            this.setTypeface(typeface);
        }
        catch (Exception ex){
            Log.e("TrailMe", ex.getMessage());
        }
    }
}
