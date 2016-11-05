package in.sampleapp.materialdesigndemo.modules.listing.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.sampleapp.materialdesigndemo.R;
import in.sampleapp.materialdesigndemo.modules.listing.ListingView;
import in.sampleapp.materialdesigndemo.utilities.Utilities;

/**
 *
 */
public class ListingFragment extends Fragment implements ListingView
{

	@BindView(R.id.xryclrVwList)
	RecyclerView ryclrVwList;
	@BindView(R.id.xswpLayoutList)
	SwipeRefreshLayout swpLayoutList;

	ListingFragmentPresenter listingFragmentPresenter;
	boolean bCallWS = false;

	int iCurrentPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_listing, container, false);
		ButterKnife.bind(this, rootView);
		return rootView;
	}

	@Override
	public void showLoader()
	{
		Utilities.showLoader(getString(R.string.fetching_data), getActivity());
	}

	@Override
	public void hideLoader()
	{
		Utilities.dismissLoader();
	}

	@Override
	public void showMessage(String message)
	{
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		listingFragmentPresenter = new ListingFragmentPresenterImplementor(getActivity(), this);
		listingFragmentPresenter.handleUIElements(ryclrVwList, swpLayoutList);

		if (bCallWS)
		{
			listingFragmentPresenter.fetchListFromServer();
		}
	}

	public void fetchDataFromServer()
	{
		bCallWS = true;
	}

	public void setCurrentScreen(int iCurrentPage)
	{
		this.iCurrentPage = iCurrentPage;
		listingFragmentPresenter.updateList(iCurrentPage);
	}
}
