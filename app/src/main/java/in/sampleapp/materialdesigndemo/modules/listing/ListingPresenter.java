package in.sampleapp.materialdesigndemo.modules.listing;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;


public interface ListingPresenter
{
    void handleUIElements(ViewPager mViewPager, TabLayout tbLayoutSections, FragmentManager supportFragmentManager);

    void setAdapter();

    void handleAddControl(FloatingActionButton fbAdd);

    void handleDeleteAction();
}
