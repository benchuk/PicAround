package com.android.PubPIc;

import java.util.ArrayList;

import Interfaces.IPlacesResult;
import Services.PlacesParams;



public class FacebookPlacesResult implements IPlacesResult
{
	public FacebookPlacesResult(ArrayList<PlacesParams> placesResults)
	{
		_places = placesResults;
	}
	private ArrayList<PlacesParams> _places;
	
	@Override
	public ArrayList<PlacesParams> GetPlaces()
	{
		return _places;
	}
}
