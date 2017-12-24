package www.gzyi.com.baseitemview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  @项目名：  utel 
 *  @包名：    com.szvetron.utel.widget
 *  @文件名:   EaseItemView
 *  @创建者:   King
 *  @创建时间:  2017/6/27 15:16
 *  @描述：    快速 item布局
 *  @功能：    可在XML中进行快速对Item 进行布局，customizedLayout可接受自定义布局
 */
public class BaseItemView
        extends LinearLayout {

    private ImageView mItemIcon;
    private TextView mItemText;
    private TextView mItemSonText;
    private LinearLayout mCustomizedLayout;
    private ViewGroup itemRootLayout;

    public BaseItemView(Context context) {
        this(context, null);
    }

    public BaseItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseStyle(context, attrs);
    }


    private void init(Context context, @LayoutRes int id) {
        View view = LayoutInflater.from(context).inflate(id, this);
        View root = view.findViewById(R.id.base_item_view_root_layout);
        if (root == null) {
            removeView(view);
            init(context, R.layout.view_base_item_default);
            return;
        }
        itemRootLayout = (ViewGroup) root;
        mItemIcon = (ImageView) view.findViewById(R.id.base_item_view_icon);
        mItemText = (TextView) view.findViewById(R.id.base_item_view_text);
        mItemSonText = (TextView) view.findViewById(R.id.base_item_view_son_text);
        mCustomizedLayout = (LinearLayout) view.findViewById(R.id.base_item_view_customized_layout);
    }


    private void parseStyle(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseItemView);
            int resourceId = ta.getResourceId(R.styleable.BaseItemView_styleType, R.layout.view_base_item_default);
            init(context, resourceId);
            parseTextStyle(ta);
            parseIconStyle(ta);
            mCustomizedLayout.setOrientation(getOrientation());
        } else {
            init(context, R.layout.view_base_item_default);
        }
    }

    private void parseTextStyle(TypedArray ta) {
        setText(ta.getText(R.styleable.BaseItemView_text));
        setSonText(ta.getText(R.styleable.BaseItemView_sonText));

        int color = ta.getColor(R.styleable.BaseItemView_textColor, mItemText.getCurrentTextColor());
        int sonColor = ta.getColor(R.styleable.BaseItemView_sonTextColor, color);


        float dim = ta.getDimension(R.styleable.BaseItemView_textSize, sp2px(14));
        float sonDim = ta.getDimension(R.styleable.BaseItemView_sonTextSize, dim - sp2px(2));

        setTextColor(color);
        setSonTextColor(sonColor);
        mItemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, dim);
        mItemSonText.setTextSize(TypedValue.COMPLEX_UNIT_PX, sonDim);
    }


    private void parseIconStyle(TypedArray ta) {
        Drawable drawable = ta.getDrawable(R.styleable.BaseItemView_icon);
        if (drawable != null) {
            mItemIcon.setImageDrawable(drawable);
            mItemIcon.setVisibility(VISIBLE);
        }
        int size = (int) ta.getDimension(R.styleable.BaseItemView_iconSize, 0);
        if (size != 0) {
            ViewGroup.LayoutParams params = mItemIcon.getLayoutParams();
            params.height = size;
            params.width = size;
            mItemIcon.setLayoutParams(params);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        while (getChildCount() > 1) {
            View view = getChildAt(1);
            removeView(view);
            mCustomizedLayout.addView(view);
        }
    }

    public ImageView getItemIcon() {
        return mItemIcon;
    }

    public TextView getItemText() {
        return mItemText;
    }

    public TextView getItemSonText() {
        return mItemSonText;
    }

    public LinearLayout getCustomizedLayout() {
        return mCustomizedLayout;
    }

    private void setText(TextView view, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            view.setText(text);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }


    public BaseItemView setText(CharSequence text) {
        setText(mItemText, text);
        return this;
    }

    public BaseItemView setTextColor(int color) {
        mItemText.setTextColor(color);
        return this;
    }

    public BaseItemView setTextSize(float size) {
        mItemText.setTextSize(size);
        return this;
    }

    public BaseItemView setSonText(CharSequence text) {
        setText(mItemSonText, text);
        return this;
    }

    public BaseItemView setSonTextColor(int color) {
        mItemSonText.setTextColor(color);
        return this;
    }

    public BaseItemView setSonTextSize(float size) {
        mItemSonText.setTextSize(size);
        return this;
    }

    public BaseItemView setIcon(@DrawableRes int res) {
        mItemIcon.setImageResource(res);
        return this;
    }

    public BaseItemView setIconSize(final int width, final int height) {
        ViewGroup.LayoutParams params = mItemIcon.getLayoutParams();

        if (height > 0) {
            params.height = dip2px(height);
        }

        if (width > 0) {
            params.width = dip2px(width);
        }
        return this;
    }

    public BaseItemView setCompoundOnClickListener(OnClickListener clickListener) {
        mItemIcon.setOnClickListener(clickListener);
        mItemText.setOnClickListener(clickListener);
        mItemSonText.setOnClickListener(clickListener);
        mCustomizedLayout.setOnClickListener(clickListener);
        return this;
    }

    private int dip2px(int i) {
        return (int) density2px(i, true);
    }

    private int sp2px(int i) {
        return (int) density2px(i, false);
    }

    private float density2px(float v, boolean isdip) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float density = isdip ? metrics.density : metrics.scaledDensity;
        return density * v + 0.5f;
    }

}
