package sutdcreations.classes;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Trinh Vu Linh Chi on 12/11/2017.
 */

public class Font {
    public static final Font  MOON_BOLD             = new Font("moon_bold.otf");
    public static final Font  MOON_LIGHT            = new Font("moon_light.otf");
    public static final Font  OPENSANS_BOLD         = new Font("opensans_bold.ttf");
    public static final Font  OPENSANS_EXTRABOLD    = new Font("opensans_extrabold.ttf");
    public static final Font  OPENSANS_ITALIC       = new Font("opensans_italic.ttf");
    public static final Font  OPENSANS_REGULAR      = new Font("opensans_regular.ttf");
    public static final Font  OPENSANS_LIGHT        = new Font("opensans_light.ttf");
    public static final Font  OPENSANS_SEMIBOLD     = new Font("opensans_semibold.ttf");
    private final String      assetName;
    private volatile Typeface typeface;

    private Font(String assetName) {
        this.assetName = assetName;
    }

    public void apply(Context context, TextView textView) {
        if (typeface == null) {
            synchronized (this) {
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(context.getAssets(), assetName);
                }
            }
        }
        textView.setTypeface(typeface);
    }
}