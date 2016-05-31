package depton.trailme.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by win on 5/27/2016.
 */
public class BertholdTextView extends EditText {
    public BertholdTextView(Context context) {
        super(context);
        SetFont(context);
    }

    public BertholdTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetFont(context);
    }

    public BertholdTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SetFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BertholdTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void SetFont(Context context){
       /* AssetManager assetManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, "/fonts/ARCENA.ttf");
        setTypeface(typeface);*/
    }
}
