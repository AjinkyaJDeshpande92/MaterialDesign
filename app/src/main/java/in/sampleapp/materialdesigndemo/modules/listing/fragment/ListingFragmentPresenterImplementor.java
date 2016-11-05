package in.sampleapp.materialdesigndemo.modules.listing.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import in.sampleapp.materialdesigndemo.R;
import in.sampleapp.materialdesigndemo.adapters.MultiSelectRecyclerViewAdapter;
import in.sampleapp.materialdesigndemo.databasemanager.DatabaseManager;
import in.sampleapp.materialdesigndemo.datamanager.DataManager;
import in.sampleapp.materialdesigndemo.entities.TaskResponse;
import in.sampleapp.materialdesigndemo.entities.TaskResponseWrapper;
import in.sampleapp.materialdesigndemo.modules.listing.ListingView;
import in.sampleapp.materialdesigndemo.utilities.DialogListener;
import in.sampleapp.materialdesigndemo.utilities.Utilities;

public class ListingFragmentPresenterImplementor implements ListingFragmentPresenter, SwipeRefreshLayout.OnRefreshListener,
		MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener, DialogListener
{
	Context context;
	ListingView listingView;
	RecyclerView rclrVwList;
	SwipeRefreshLayout swpLytListRefresh;
	DataManager dataManager;
	int iCurrentPage;
	Utilities utilities;
	int iSelectedPosition;
	private MultiSelectRecyclerViewAdapter mAdapter;

	ListingFragmentPresenterImplementor(Context context, ListingView listingView)
	{
		this.context = context;
		this.listingView = listingView;
		dataManager = DataManager.getInstance(context);
		utilities = new Utilities(context);
	}

	@Override
	public void handleUIElements(RecyclerView rclrVwList, SwipeRefreshLayout swpLytListRefresh)
	{
		this.rclrVwList = rclrVwList;
		this.swpLytListRefresh = swpLytListRefresh;

		swpLytListRefresh.setOnRefreshListener(this);
	}

	@Override
	public void fetchListFromServer()
	{
		if (listingView != null)
		{
			listingView.showLoader();
			ListingFragmentInteractor listingFragmentInteractor = new ListingFragmentInteractorImplementor(context, this);
			listingFragmentInteractor.fetchDataFromServer(false);
		}
	}

	@Override
	public void onSuccess(TaskResponseWrapper taskResponseWrapper)
	{
		DatabaseManager databaseManager = new DatabaseManager(context, TaskResponse.class);
		databaseManager.deleteAll();
		dataManager.getTasks().clear();
		for (TaskResponse taskResponse : taskResponseWrapper.getTasklist())
		{
			databaseManager.addData(taskResponse);
		}
		dataManager.setTasks((ArrayList<TaskResponse>) databaseManager.getRecords());

		updateList();

		if (listingView != null)
		{
			listingView.hideLoader();
			swpLytListRefresh.setRefreshing(false);
		}
	}

	private void updateList()
	{
		rclrVwList.setHasFixedSize(true);
		rclrVwList.setLayoutManager(new LinearLayoutManager(context));
		mAdapter = new MultiSelectRecyclerViewAdapter(context, dataManager.getTasks(), this, iCurrentPage);
		rclrVwList.setAdapter(mAdapter);

	}

	@Override
	public void onFailure(String message)
	{
		if (listingView != null)
		{
			listingView.hideLoader();
			swpLytListRefresh.setRefreshing(false);
			listingView.showMessage(message);
		}
	}

	@Override
	public void updateList(int iCurrentPage)
	{
		this.iCurrentPage = iCurrentPage;
		updateList();
	}

	@Override
	public void onRefresh()
	{
		ListingFragmentInteractor listingFragmentInteractor = new ListingFragmentInteractorImplementor(context, this);
		listingFragmentInteractor.fetchDataFromServer(true);
	}

	@Override
	public void onItemClicked(int position, boolean bChkBox)
	{
		iSelectedPosition = position;
		if (bChkBox)
		{
			toggleSelection(position);
		}
		else
		{
			if (iCurrentPage == 1)
			{
				// Pending Section
				// Item is clicked..We need to move it to Done Section
				utilities.showMultipleOptionsDialog(context.getString(R.string.add_to_done), context.getString(R.string.yes),
						context.getString(R.string.no), this);
			}
		}
	}

	private void toggleSelection(int position)
	{
		mAdapter.toggleSelection(position);
	}

	@Override
	public void onPositiveButtonClicked(String message)
	{
		// User has selected to delete the records
		for (int i = 0; i < dataManager.getTasks().size(); i++)
		{

			if (i == iSelectedPosition)
			{
				// added to Completed
				dataManager.getTasks().get(i).setState(1);
				break;
			}

		}

		DatabaseManager databaseManager = new DatabaseManager(context, TaskResponse.class);
		databaseManager.deleteAll();

		for (TaskResponse taskResponse : dataManager.getTasks())
		{
			databaseManager.addData(taskResponse);
		}
		dataManager.getTasks().clear();
		dataManager.setTasks((ArrayList<TaskResponse>) databaseManager.getRecords());

		updateList(iCurrentPage);
	}

	@Override
	public void onNegativeButtonClicked(String message)
	{

	}
}
