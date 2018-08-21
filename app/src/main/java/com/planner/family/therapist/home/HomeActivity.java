package com.planner.family.therapist.home;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.ChildrenDatabase;
import com.planner.family.therapist.data.ChildrenRepository;
import com.planner.family.therapist.info.ChildInfoFragment;
import com.planner.family.therapist.nearby.NearbyFragment;
import com.planner.family.therapist.network.NetworkFragment;
import com.planner.family.therapist.network.NetworkViewModel;
import com.planner.family.therapist.reflections.ReflectionsFragment;
import com.planner.family.therapist.reflections.ReflectionsViewModel;
import com.planner.family.therapist.resources.ResourcesFragment;

public class HomeActivity extends AppCompatActivity implements ChildrenDrawerFragment.OnDrawerItemClicked {

    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 25;

    private DrawerLayout mDrawerLayout;

    private ReflectionsFragment mReflectionsFragment;
    private ChildInfoFragment mChildInfoFragment;
    private NearbyFragment mNearbyFragment;
    private ResourcesFragment mResourcesFragment;
    private NetworkFragment mNetworkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setUpActionBar();
        checkPermissions();

        ChildrenRepository childrenRepository =
                ChildrenRepository.getInstance(ChildrenDatabase.getChildDatabase(this));
        ViewModelFactory viewModelFactory =
                new ViewModelFactory(childrenRepository);

        initFragments(viewModelFactory);
    }

    private void initFragments(ViewModelFactory viewModelFactory) {

        ChildrenViewModel sharedViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(ChildrenViewModel.class);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.app_name,
                R.string.app_name
        ) {
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ChildrenDrawerFragment childrenDrawerFragment =
                (ChildrenDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawerContentFrame);
        if (childrenDrawerFragment == null) {

            childrenDrawerFragment = ChildrenDrawerFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.drawerContentFrame, childrenDrawerFragment);
            transaction.commit();
        }

        childrenDrawerFragment.setViewModel(sharedViewModel);

        mReflectionsFragment = ReflectionsFragment.newInstance();
        ReflectionsViewModel reflectionsViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(ReflectionsViewModel.class);
        mReflectionsFragment.setViewModel(reflectionsViewModel);
        mReflectionsFragment.setSharedViewModel(sharedViewModel);

        mChildInfoFragment = ChildInfoFragment.newInstance();
        mChildInfoFragment.setViewModel(sharedViewModel);

        mNearbyFragment = NearbyFragment.newInstance();

        mResourcesFragment = ResourcesFragment.newInstance();

        mNetworkFragment = NetworkFragment.newInstance();
        NetworkViewModel networkViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(NetworkViewModel.class);
        mNetworkFragment.setViewModel(networkViewModel);
        mNetworkFragment.setSharedViewModel(sharedViewModel);
    }

    @Override
    public void onDrawerItemClick(View buttonView) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (buttonView.getId()) {
            case R.id.reflections_button:
                fragmentManager.beginTransaction().replace(R.id.homeContentFrame, mReflectionsFragment).commit();
                break;
            case R.id.child_info_button:
                fragmentManager.beginTransaction().replace(R.id.homeContentFrame, mChildInfoFragment).commit();
                break;
            case R.id.resources_button:
                fragmentManager.beginTransaction().replace(R.id.homeContentFrame, mResourcesFragment).commit();
                break;
            case R.id.network_button:
                fragmentManager.beginTransaction().replace(R.id.homeContentFrame, mNetworkFragment).commit();
                break;
            case R.id.nearby_button:
                fragmentManager.beginTransaction().replace(R.id.homeContentFrame, mNearbyFragment).commit();
                break;
        }

        setTitle(buttonView.getTag().toString());
        mDrawerLayout.closeDrawers();
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
