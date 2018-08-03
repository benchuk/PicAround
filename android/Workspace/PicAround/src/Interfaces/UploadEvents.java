package Interfaces;

public interface UploadEvents
{
	  void UploadedStartd();
	  void UploadedEnded(int result);
	  void UploadedProgress(long bytesUploded, long totalBytes);
}
