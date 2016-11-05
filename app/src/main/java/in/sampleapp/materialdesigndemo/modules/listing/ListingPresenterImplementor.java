package in.sampleapp.materialdesigndemo.modules.listing;

import java.util.ArrayList;
import java.util.Random;

import com.rengwuxian.materialedittext.MaterialEditText;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import in.sampleapp.materialdesigndemo.R;
import in.sampleapp.materialdesigndemo.adapters.SectionsPagerAdapter;
import in.sampleapp.materialdesigndemo.databasemanager.DatabaseManager;
import in.sampleapp.materialdesigndemo.datamanager.DataManager;
import in.sampleapp.materialdesigndemo.entities.TaskResponse;
import in.sampleapp.materialdesigndemo.modules.listing.fragment.ListingFragment;
import in.sampleapp.materialdesigndemo.utilities.DialogListener;
import in.sampleapp.materialdesigndemo.utilities.Utilities;


public class ListingPresenterImplementor implements ListingPresenter, View.OnClickListener, ViewPager.OnPageChangeListener, DialogListener
{
	Context context;
	ListingView listingView;
	ViewPager mViewPager;
	TabLayout tbLayoutSections;
	FragmentManager supportFragmentManager;
	FloatingActionButton fbAdd;
	SectionsPagerAdapter mSectionsPagerAdapter;
	DataManager dataManager;
	Utilities utilities;

	ListingPresenterImplementor(Context context, ListingView listingView)
	{
		this.context = context;
		this.listingView = listingView;
		dataManager = DataManager.getInstance(context);
		utilities = new Utilities(context);

	}

	@Override
	public void handleUIElements(ViewPager mViewPager, TabLayout tbLayoutSections, FragmentManager supportFragmentManager)
	{
		this.mViewPager = mViewPager;
		this.tbLayoutSections = tbLayoutSections;
		this.supportFragmentManager = supportFragmentManager;
	}

	@Override
	public void setAdapter()
	{
		mSectionsPagerAdapter = new SectionsPagerAdapter(supportFragmentManager);
		mSectionsPagerAdapter.addFrag(new ListingFragment(), "DONE");
		mSectionsPagerAdapter.addFrag(new ListingFragment(), "PENDING");
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tbLayoutSections.setupWithViewPager(mViewPager);
		mViewPager.addOnPageChangeListener(this);

		ListingFragment currentFragment = (ListingFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

		if (currentFragment != null)
		{
			currentFragment.fetchDataFromServer();
		}

	}

	@Override
	public void handleAddControl(FloatingActionButton fbAdd)
	{
		this.fbAdd = fbAdd;
		this.fbAdd.setOnClickListener(this);
		this.fbAdd.setVisibility(View.GONE);
	}

	@Override
	public void handleDeleteAction()
	{
		boolean bRecordsSelected = false;

		for (TaskResponse taskResponse : dataManager.getTasks())
		{
			if (taskResponse.isbSoftDelete())
			{
				bRecordsSelected = true;
				break;
			}

		}
		if (bRecordsSelected)
		{
			utilities.showMultipleOptionsDialog(context.getString(R.string.do_you_want_to_delete), context.getString(R.string.yes),
					context.getString(R.string.no), this);
		}
	}

	@Override
	public void onClick(View view)
	{
		if (view == fbAdd)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			final android.app.AlertDialog.Builder ConfirmationDialog = new android.app.AlertDialog.Builder(context);
			View dialogView = inflater.inflate(R.layout.add_new_task, null);
			ConfirmationDialog.setView(dialogView);
			ConfirmationDialog.setMessage(context.getString(R.string.add_task));
			ConfirmationDialog.setCancelable(false);
			final MaterialEditText edtxtTaskName = (MaterialEditText) dialogView.findViewById(R.id.edtxtTaskName);
			ConfirmationDialog.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});
			ConfirmationDialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

				}
			});
			final AlertDialog dialog = ConfirmationDialog.create();
			dialog.show();
			// Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (edtxtTaskName.getText().toString().trim().equalsIgnoreCase(""))
					{
						return;
					}
					addInDBandUpdateUI(edtxtTaskName.getText().toString().trim());
					updateScreen(mViewPager.getCurrentItem());
					dialog.dismiss();
				}
			});

			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});

		}
	}

	private void addInDBandUpdateUI(String taskName)
	{
		DatabaseManager databaseManager = new DatabaseManager(context, TaskResponse.class);
		TaskResponse taskResponse = new TaskResponse();
		taskResponse.setState(0);
		taskResponse.setID(new Random().nextInt(100000));
		taskResponse.setName(taskName);
		databaseManager.addData(taskResponse);
		dataManager.getTasks().clear();
		dataManager.setTasks((ArrayList<TaskResponse>) databaseManager.getRecords());
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		updateScreen(position);
		if (position == 0)
		{
			// Done section
			this.fbAdd.setVisibility(View.GONE);
		}
		else
		{
			// Pending Section
			this.fbAdd.setVisibility(View.VISIBLE);
		}
	}

	private void updateScreen(int position)
	{
		ListingFragment currentFragment = (ListingFragment) mSectionsPagerAdapter.getItem(position);
		if (currentFragment != null)
		{
			currentFragment.setCurrentScreen(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	@Override
	public void onPositiveButtonClicked(String message)
	{
		ArrayList<TaskResponse> arrlstUpdatedList = new ArrayList<>();
		// User has selected to delete the records
		for (TaskResponse taskResponse : dataManager.getTasks())
		{

			if (!taskResponse.isbSoftDelete())
			{
				arrlstUpdatedList.add(taskResponse);
			}
		}

		DatabaseManager databaseManager = new DatabaseManager(context, TaskResponse.class);
		databaseManager.deleteAll();

		for (TaskResponse taskResponse : arrlstUpdatedList)
		{
			databaseManager.addData(taskResponse);
		}
		dataManager.getTasks().clear();
		dataManager.setTasks((ArrayList<TaskResponse>) databaseManager.getRecords());

		updateScreen(mViewPager.getCurrentItem());
	}

	@Override
	public void onNegativeButtonClicked(String message)
	{

	}
}
