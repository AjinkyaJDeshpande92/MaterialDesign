package in.sampleapp.materialdesigndemo.modules.listing.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import in.sampleapp.materialdesigndemo.entities.TaskResponseWrapper;


public interface ListingFragmentPresenter
{
    void handleUIElements (RecyclerView rclrVwList , SwipeRefreshLayout swpLytListRefresh);

    void fetchListFromServer();

    void onSuccess(TaskResponseWrapper taskResponseWrapper);

    void onFailure(String message);

    void updateList(int iCurrentPage);
}
