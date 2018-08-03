package Interfaces;

import org.apache.http.HttpResponse;
public interface IWebRequestResultHandler
{
	void OnWebRequestCompleted(HttpResponse httpResponse);
}
