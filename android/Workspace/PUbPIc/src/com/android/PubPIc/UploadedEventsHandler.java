package com.android.PubPIc;

import java.math.BigInteger;
import java.security.SecureRandom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;
import Interfaces.UploadEvents;

public class UploadedEventsHandler implements UploadEvents
{
	private Context _context;
	private NotificationManager _notificationManager;
	private Notification _notification;
	private SecureRandom random = new SecureRandom();
	private int _notificationID = 12358;
	private Messenger _messanger;

	public UploadedEventsHandler(Context context, NotificationManager notificationManager, Messenger messanger) {
		_context = context;
		_notificationManager = notificationManager;
		// BigInteger inte = new BigInteger(130, random);
		// _notificationID = inte.intValue();
		_notificationID = 12358;
		_messanger = messanger;
	}

	@Override
	public void UploadedStartd()
	{
		showNotificationUploadStarted();
	}

	@Override
	public void UploadedEnded(int result)
	{
		showNotificationUploadEnded(result);
	}

	private void showNotificationUploadStarted()
	{
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = "Uploading";

		// Set the icon, scrolling text and timestamp
		// Notification notification = new
		// Notification(R.drawable.ic_stat_upload, text,
		// System.currentTimeMillis());
		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, new Intent(_context, PUbPIcActivity.class), 0);

		// configure the notification
		_notification = new Notification(R.drawable.ic_stat_upload, text, System.currentTimeMillis());
		_notification.flags = _notification.flags | Notification.FLAG_ONGOING_EVENT;
		_notification.contentView = new RemoteViews(_context.getPackageName(), R.layout.upload_progress);
		_notification.contentIntent = contentIntent;
		_notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_stat_upload);
		_notification.contentView.setTextViewText(R.id.status_text, "Uploading...");
		_notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);

		// Set the info for the views that show in the notification panel.
		// _notification.setLatestEventInfo(_context, text, text,
		// contentIntent);

		// Send the notification.
		_notificationManager.notify(_notificationID, _notification);
	}

	@Override
	public void UploadedProgress(long bytesUploded, long totalBytes)
	{
		int prog;
		if (totalBytes != 0)
		{

			prog = (int) (((double) bytesUploded / (double) totalBytes) * 100);
		}
		else
		{
			prog = 50;
		}
		if (bytesUploded % 25 == 0)
		{
			_notification.contentView.setProgressBar(R.id.status_progress, 100, prog, false);
		}
		// inform the progress bar of updates in progress
		_notificationManager.notify(_notificationID, _notification);
		Message msg = Message.obtain();
		msg.arg2 = prog;
		try
		{
			_messanger.send(msg);
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showNotificationUploadEnded(int result)
	{
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text;
		if (result == Activity.RESULT_OK)
		{
			text = "Uploading Completed";
		}
		else
		{
			text = "Error Uploading";
		}

		// // Set the icon, scrolling text and timestamp
		// Notification notification = new
		// Notification(R.drawable.ic_stat_upload, text,
		// System.currentTimeMillis());
		//
		// // The PendingIntent to launch our activity if the user selects this
		// // notification
		// PendingIntent contentIntent = PendingIntent.getActivity(_context, 0,
		// new Intent(_context, PUbPIcActivity.class), 0);
		//
		// // Set the info for the views that show in the notification panel.
		// notification.setLatestEventInfo(_context, text, text, contentIntent);
		//
		// // Send the notification.
		// _notificationManager.notify(123456, notification);
		_notification.contentView.setProgressBar(R.id.status_progress, 100, 100, false);
		_notification.contentView.setTextViewText(R.id.status_text, "Done");
		// inform the progress bar of updates in progress
		_notificationManager.notify(_notificationID, _notification);
		_notificationManager.cancel(_notificationID);

	}
}
