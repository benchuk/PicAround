package WebHelpers;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import Interfaces.IEventsResultHandler;
import Interfaces.IFeedEventsResultHandler;
import Interfaces.IWebRequestResultHandler;
import org.apache.http.HttpResponse;
public class FeedEventsWebRequestHandlerParams
{
	public  HttpResponse httpResponse;
	public IFeedEventsResultHandler feedEventsResultHandler;
}

