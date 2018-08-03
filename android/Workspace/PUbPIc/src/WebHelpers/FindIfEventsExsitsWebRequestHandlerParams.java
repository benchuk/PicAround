package WebHelpers;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import Interfaces.IEventsResultHandler;
import Interfaces.IFindIfEventsExistsResultHandler;
import Interfaces.IWebRequestResultHandler;
import org.apache.http.HttpResponse;
public class FindIfEventsExsitsWebRequestHandlerParams
{
	public  HttpResponse httpResponse;
	public IFindIfEventsExistsResultHandler eventsResultHandler;
}

