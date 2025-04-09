package com.example.try2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Utility class for efficient bitmap loading and handling
 * to prevent "Canvas: trying to draw too large bitmap" errors
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    /**
     * Load a drawable resource into an ImageView with size optimization
     * @param context Application context
     * @param imageView Target ImageView
     * @param drawableResId Drawable resource ID
     */
    public static void loadDrawableIntoImageView(Context context, ImageView imageView, int drawableResId) {
        if (context == null || imageView == null) {
            Log.e(TAG, "Context or ImageView is null");
            return;
        }

        try {
            Glide.with(context)
                    .load(drawableResId)
                    .apply(new RequestOptions()
                            .override(800, 1000) // match actual view size in pixels (not too big!)
                            .fitCenter()
                            .centerInside())
                    .into(imageView);

        } catch (Exception e) {
            Log.e(TAG, "Error loading drawable: " + e.getMessage());
        }
    }

    /**
     * Calculate optimal sample size for loading bitmaps efficiently
     * @param options BitmapFactory.Options with outHeight and outWidth set
     * @param reqWidth Required width
     * @param reqHeight Required height
     * @return Sample size as power of 2
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Decode a resource to a bitmap with memory optimization
     * @param context Application context
     * @param resId Resource ID
     * @param reqWidth Required width
     * @param reqHeight Required height
     * @return Optimized bitmap or null if failed
     */
    public static Bitmap decodeSampledBitmapFromResource(Context context, int resId, int reqWidth, int reqHeight) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError decoding bitmap: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error decoding bitmap: " + e.getMessage());
            return null;
        }
    }
}
