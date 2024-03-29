package com.worthwhilegames.carhubmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.worthwhilegames.carhubmobile.R;

public class MenuImageAdapter extends BaseAdapter {
    private Context mContext;
    private ImageTextWrapper[] mImageTextWrappers;

    public MenuImageAdapter(Context c, ImageTextWrapper[] itw) {
        mContext = c;
        mImageTextWrappers = itw;
    }

    @Override
    public int getCount() {
        return mImageTextWrappers.length;
    }

    @Override
    public Object getItem(int position) {
        return mImageTextWrappers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout wrapper;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            wrapper = (RelativeLayout) inflater.inflate(R.layout.mainmenuitem, parent, false);
            wrapper.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        } else {
            wrapper = (RelativeLayout) convertView;
        }

        SVG svg = SVGParser.getSVGFromResource(mContext.getResources(), mImageTextWrappers[position].mImageId);
        PictureDrawable pd = svg.createPictureDrawable();

        Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        pd.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        pd.draw(canvas);

        ImageView imageView = (ImageView) wrapper.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(bitmap);

        TextView textView = (TextView) wrapper.findViewById(R.id.itemLabel);
        textView.setText(mImageTextWrappers[position].mTextId);

        return wrapper;
    }

    public static class ImageTextWrapper {
        public Integer mImageId;
        public Integer mTextId;
        public Class<?> mIntent;

        public ImageTextWrapper(Integer imageId, Integer textId, Class<?> intentClass) {
            mImageId = imageId;
            mTextId = textId;
            mIntent = intentClass;
        }
    }
}
