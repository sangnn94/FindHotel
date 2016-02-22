package com.sang.findhotel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 08/12/2015.
 */
public class FindHotelActivity extends AppCompatActivity implements ListCityFragment.CallBacks, LogInFragment.CallBacks {
  private Toolbar mToolbar;
  private ViewPager mViewPager;
  private TabLayout mTabLayout;
  private CustomPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_hotel);

    mToolbar = (Toolbar) findViewById(R.id.tool_bar);
    setSupportActionBar(mToolbar);

    mViewPager = (ViewPager) findViewById(R.id.view_pager);
    adapter = new CustomPagerAdapter(getSupportFragmentManager(), FindHotelActivity.this);
    mViewPager.setAdapter(adapter);

    mTabLayout = (TabLayout) findViewById(R.id.sliding_tab);
    mTabLayout.setupWithViewPager(mViewPager);

    mViewPager.setOffscreenPageLimit(2);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    CityList.get(getApplicationContext()).getCities().clear();
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sign_out_menu:
        if (ParseUser.getCurrentUser() != null) {
          final ProgressDialog dialog = new ProgressDialog(FindHotelActivity.this);
          dialog.setTitle("Please Wait");
          dialog.setMessage(getString(R.string.log_out));
          dialog.show();
          ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
              dialog.dismiss();
              refreshAdapter();
            }
          });
        }
        return true;
      case R.id.map_menu:
        startActivity(new Intent(FindHotelActivity.this, MapsActivity.class));
        return true;
      case R.id.about_menu:
        startActivity(AboutActivity.newIntent(FindHotelActivity.this));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onCitySelected(String city) {
    ListHotelFragment listHotelFragment = (ListHotelFragment) adapter.getRegisteredFragment(1);
    listHotelFragment.updateCity(city);
    mViewPager.setCurrentItem(1);
  }

  @Override
  public void onLogInListener() {
    refreshAdapter();
  }

  public void refreshAdapter() {
    adapter.notifyDataSetChanged();
  }


}
