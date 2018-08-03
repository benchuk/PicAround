package WebHelpers;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import Interfaces.IWebRequestResultHandler;

public class HttpRequestParameters
{
	public  String url;
	public ArrayList<NameValuePair> NameValuePairs;
	public IWebRequestResultHandler webRequestHandler;
}
