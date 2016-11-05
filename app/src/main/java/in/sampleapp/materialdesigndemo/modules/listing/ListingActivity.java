package in.sampleapp.materialdesigndemo.modules.listing;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.sampleapp.materialdesigndemo.R;

public class ListingActivity extends AppCompatActivity implements ListingView
{

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	@BindView(R.id.container)
	ViewPager viewPagerContainer;
	@BindView(R.id.fab)
	FloatingActionButton fbAdd;
	@BindView(R.id.main_content)
	CoordinatorLayout crdLayoutContainer;
	@BindView(R.id.tabs)
	TabLayout tbLayoutSections;

	ListingPresenter listingPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		listingPresenter = new ListingPresenterImplementor(this, this);
		listingPresenter.handleUIElements(viewPagerContainer, tbLayoutSections, getSupportFragmentManager());
		listingPresenter.handleAddControl(fbAdd);
		listingPresenter.setAdapter();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_listing, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			listingPresenter.handleDeleteAction();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void showLoader()
	{

	}

	@Override
	public void hideLoader()
	{

	}

	@Override
	public void showMessage(String message)
	{

	}

}
