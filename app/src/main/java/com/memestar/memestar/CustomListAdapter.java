package com.memestar.memestar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CustomListAdapter extends ArrayAdapter<String>
{

	private final Activity context;


	private final String[] imgid;

	public CustomListAdapter(Activity context,String[] imgid)
	{
		super(context, R.layout.mylist,imgid);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.imgid = imgid;
	}

	public View getView(int position, View view, ViewGroup parent)
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.mylist, null, true);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setPadding(16, 16, 16, 16);

		imageView.setImageBitmap(
			decodeSampledBitmapFromResource(imgid[position], 250, 250));
		return rowView;

	};
	public static int calculateInSampleSize(
		BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
				&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(String path,
																											 int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

}
