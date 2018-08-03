package com.android.PubPIc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

// this NumericKeyboard works only for EditTexts which 
// have a tag with "usesNumericKeyboard" key and value of "true"
// use tag with "ignoreMeForNumericKeyboardTouchListener" key and 
// value "true" for controls you do not want to set its touchListener 
public class GridviewPaImage extends FrameLayout
{
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private View mainView = null;
	ImageView _image;
	ImageView _imageOk;
	ProgressBar _progressBar;
	View mLayoutView;
	String _pathOnSd;
	public GridviewPaImage(Context context, String pathOnSd) {
		super(context);
		mContext = context;
		_pathOnSd = pathOnSd;
		initialize();
	}

	public GridviewPaImage(Context context, AttributeSet attrs, String pathOnSd) {
		super(context, attrs);
		mContext = context;
		_pathOnSd = pathOnSd;
		initialize();
		
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		mLayoutView.requestLayout();
	    invalidate();
	}

	public String GetImageSdPath()
	{
		return _pathOnSd;
	}
	protected void initialize()
	{
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayoutView = mLayoutInflater.inflate(R.layout.gallery_pa_image, this);

		_image = (ImageView) mLayoutView.findViewById(R.id.imageView1);
		_progressBar = (ProgressBar) mLayoutView.findViewById(R.id.progressBar1);
		_progressBar.setVisibility(View.INVISIBLE);
		_imageOk = (ImageView) mLayoutView.findViewById(R.id.okimagecontrol);
		_imageOk.setVisibility(View.INVISIBLE);
		// if (mainView == null && !this.isInEditMode()) {
		// setTouchListenerForChildViews();
		// this.setVisibility(GONE);
		// }
	}

	public void SetOkVisiable()
	{
		_imageOk.setVisibility(View.VISIBLE);
		//ImageView im = new ImageView(mContext);
		//return im;
	}
	
	
	public ImageView GetImageControl()
	{
		return ((ImageView)(mLayoutView.findViewById(R.id.imageView1)));
		//ImageView im = new ImageView(mContext);
		//return im;
	}
	
	public ProgressBar GetProgressControl()
	{
		return ((ProgressBar)(mLayoutView.findViewById(R.id.progressBar1)));
		//ImageView im = new ImageView(mContext);
		//return im;
	}
}