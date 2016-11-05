package in.sampleapp.materialdesigndemo.modules.listing.fragment;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import android.content.Context;

import in.sampleapp.materialdesigndemo.application.MaterialApplication;
import in.sampleapp.materialdesigndemo.databasemanager.DatabaseManager;
import in.sampleapp.materialdesigndemo.entities.TaskResponse;
import in.sampleapp.materialdesigndemo.entities.TaskResponseWrapper;
import in.sampleapp.materialdesigndemo.webservicemanager.WebserviceManager;
import in.sampleapp.materialdesigndemo.webservicemanager.WebserviceResponse;


public class ListingFragmentInteractorImplementor implements ListingFragmentInteractor, WebserviceResponse
{
	Context context;
	ListingFragmentPresenter listingFragmentPresenter;

	ListingFragmentInteractorImplementor(Context context, ListingFragmentPresenter listingFragmentPresenter)
	{
		this.context = context;
		this.listingFragmentPresenter = listingFragmentPresenter;
		MaterialApplication.getInstance().getBus().register(this);
	}

	@Override
	public void fetchDataFromServer(boolean bDeletePreviousRecords)
	{
		if (bDeletePreviousRecords)
		{
			WebserviceManager webserviceManager = new WebserviceManager(context);
			webserviceManager.setWebResponseListener(this);
			webserviceManager.fetchListFromServer();
		}
		else
		{
			DatabaseManager databaseManager = new DatabaseManager(context, TaskResponse.class);
			if (databaseManager.getRecords().isEmpty())
			{
				WebserviceManager webserviceManager = new WebserviceManager(context);
				webserviceManager.setWebResponseListener(this);
				webserviceManager.fetchListFromServer();
			}
			else
			{
				TaskResponseWrapper taskResponseWrapper = new TaskResponseWrapper();
				taskResponseWrapper.setTasklist((ArrayList<TaskResponse>) databaseManager.getRecords());
				if (listingFragmentPresenter != null)
				{
					listingFragmentPresenter.onSuccess(taskResponseWrapper);
				}
			}
		}
	}

	@Override
	public void onFailure(String message)
	{
		if (listingFragmentPresenter != null)
		{
			listingFragmentPresenter.onFailure(message);
		}
	}

	@Subscribe
	public void onResponse(Object response)
	{
		if (response != null)
		{
			Gson gson = new Gson();
			TaskResponseWrapper taskResponseWrapper = gson.fromJson(response.toString(), TaskResponseWrapper.class);

			if (listingFragmentPresenter != null)
			{
				listingFragmentPresenter.onSuccess(taskResponseWrapper);
			}
		}
	}
}
