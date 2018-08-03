package com.socialcamera.PicAround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import Interfaces.UploadEvents;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class UploadingFiles
{
	//private Activity _view;
	private UploadEvents _uploadEvents;

	public int uploadFile(File file, UploadEvents uploadEvents, UploadFileParams uploadFileParams) throws HttpException, IOException
	{
		_uploadEvents = uploadEvents;

		uploadEvents.UploadedStartd();
		int status = Activity.RESULT_CANCELED;
		String baseUrl = String.format("http://picaround.azurewebsites.net/Upload/UploadFile?lat=%s&lon=%s&alt=%s&heading=%s&userid=%s&eventid=%s",uploadFileParams.Latitude,uploadFileParams.Longtitude,uploadFileParams.Altitude,uploadFileParams.Heading,uploadFileParams.UserID,uploadFileParams.EventID);
		String encodedPlace = URLEncoder.encode(uploadFileParams.Place,"UTF-8");
		String encodedEventLabel = URLEncoder.encode(uploadFileParams.EventLabel,"UTF-8");
		String encodedParams = String.format("&place=%s&eventLabel=%s",encodedPlace,encodedEventLabel);
		PostMethod filePost = new PostMethod(baseUrl + encodedParams);
//		filePost.setParameter("lat", "32.5");
//		filePost.setParameter("lon", "35.4");
		Part[] parts = null;
		filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		try
		{
			parts = new Part[1];
			parts[0] = new FilePart(file.getName(), file);
		}
		catch (FileNotFoundException e)
		{

		}
		MultipartRequestEntity mp = new MultipartRequestEntity(parts, filePost.getParams());
		FileUpProgressListener listener = new FileUpProgressListener(file.length(),uploadEvents);
		CountingMultipartRequestEntity cmr = new CountingMultipartRequestEntity(mp, listener);
		filePost.setRequestEntity(cmr);
		
		HttpClient client = new HttpClient();
		
		client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		try
		{
			int webStatus = client.executeMethod(filePost);
			if (webStatus == 200 || webStatus==302)
			{
				status = Activity.RESULT_OK;
			}
			else
			{
				status = Activity.RESULT_CANCELED;
			}
		}
		catch (HttpException e)
		{
			status = Activity.RESULT_CANCELED;
			e.printStackTrace();
		}
		catch (IOException e)
		{
			status = Activity.RESULT_CANCELED;
			e.printStackTrace();
		}
		uploadEvents.UploadedEnded(status);
		return status;
	}

	private Integer status = Activity.RESULT_CANCELED;

	class UploadImageTask extends AsyncTask<File, Integer, Integer>
	{
		ProgressDialog _dialog;

		protected Integer doInBackground(File... files)
		{

			for (File file : files)
			{
				PostMethod filePost = new PostMethod("http://picaround.azurewebsites.net/Upload/UploadFile");
				Part[] parts = null;
				filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
				try
				{
					parts = new Part[1];
					parts[0] = new FilePart(file.getName(), file);
				}
				catch (FileNotFoundException e)
				{

				}
				filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
				try
				{
					client.executeMethod(filePost);
					status = Activity.RESULT_OK;
				}
				catch (HttpException e)
				{
					status = Activity.RESULT_CANCELED;
					e.printStackTrace();
				}
				catch (IOException e)
				{
					status = Activity.RESULT_CANCELED;
					e.printStackTrace();
				}
			}
			return status;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			_uploadEvents.UploadedStartd();

		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			// setProgressPercent(progress[0]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			super.onPostExecute(result);
			_uploadEvents.UploadedEnded(status);
			// showDialog("Uploaded " + result + " bytes");
			// _dialog.dismiss();
		}
	}

}

// HttpClient client = new DefaultHttpClient(new BasicHttpParams());
// HttpPost httpPost = new
// HttpPost("http://picapp.yosarc.com/Upload/UploadFile");
// httpPost.setEntity(new FileEntity(file, "image/jpeg"));
// HttpResponse res = client.execute(httpPost);

// HttpClient client = new DefaultHttpClient(new BasicHttpParams());
// HttpPost httpPost = new
// HttpPost("http://picapp.yosarc.com/Upload/UploadFile");
// MultipartEntity mpEntity = new MultipartEntity();
// ContentBody cbFile = new FileBody(file, "image/jpeg");
// mpEntity.addPart("userfile", cbFile);
// httpPost.setEntity(mpEntity);
// System.out.println("executing request " + httpPost.getRequestLine());
// HttpResponse response = client.execute(httpPost);
// HttpEntity res = response.getEntity();
// ShowDialog(res.toString());
// _view = view;

// public void ShowDialog(String text)
// {
// AlertDialog.Builder builder = new AlertDialog.Builder(_view);
// builder.setMessage(text).setCancelable(false).setPositiveButton("Close", new
// DialogInterface.OnClickListener()
// {
// public void onClick(DialogInterface dialog, int id)
// {
//
// }
// });
// AlertDialog alert = builder.create();
// alert.show();
// }

// public void Upload(String fileName, Activity view)
// {
// HttpURLConnection connection = null;
// DataOutputStream outputStream = null;
// DataInputStream inputStream = null;
// _view = view;
// String pathToOurFile = fileName;
// String urlServer = "http://localhost:2805/api/FilesUpload";
// String lineEnd = "\r\n";
// String twoHyphens = "--";
// String boundary = "*****";
//
// int bytesRead, bytesAvailable, bufferSize;
// byte[] buffer;
// int maxBufferSize = 1 * 1024 * 1024;
// try
// {
// File file = new File(pathToOurFile);
// FileInputStream fileInputStream = new FileInputStream(file);
//
// URL url = new URL(urlServer);
// connection = (HttpURLConnection) url.openConnection();
//
// // Allow Inputs & Outputs
// connection.setDoInput(true);
// connection.setDoOutput(true);
// connection.setUseCaches(false);
//
// // Enable POST method
// connection.setRequestMethod("POST");
//
// connection.setRequestProperty("Connection", "Keep-Alive");
// connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="
// + boundary);
// OutputStream cs = connection.getOutputStream();
// outputStream = new DataOutputStream(cs);
// outputStream.writeBytes(twoHyphens + boundary + lineEnd);
// outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
// + pathToOurFile + "\"" + lineEnd);
// outputStream.writeBytes(lineEnd);
//
// bytesAvailable = fileInputStream.available();
// bufferSize = Math.min(bytesAvailable, maxBufferSize);
// buffer = new byte[bufferSize];
//
// // Read file
// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
// while (bytesRead > 0)
// {
// outputStream.write(buffer, 0, bufferSize);
// bytesAvailable = fileInputStream.available();
// bufferSize = Math.min(bytesAvailable, maxBufferSize);
// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
// }
//
// outputStream.writeBytes(lineEnd);
// outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
// // Responses from the server (code and message)
// // serverResponseCode = connection.getResponseCode();
// // serverResponseMessage = connection.getResponseMessage();
//
// fileInputStream.close();
// outputStream.flush();
// outputStream.close();
// }
// catch (Exception ex)
// {
// //ShowDialog(ex.getMessage());
// }
//
// }
