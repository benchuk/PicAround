package com.android.PubPIc;

import Interfaces.UploadEvents;

import com.android.PubPIc.CountingMultipartRequestEntity.ProgressListener;



public class FileUpProgressListener implements ProgressListener
{
	private long _total;
	private UploadEvents _uploadEvents;
	public FileUpProgressListener(long total, UploadEvents uploadEvents)
	{
		_total = total;
		_uploadEvents = uploadEvents;
	}
	@Override
	public void transferred(long num)
	{
		_uploadEvents.UploadedProgress(num, _total);
		
	}

}
