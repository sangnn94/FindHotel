package com.sang.findhotel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by sang on 08/12/2015.
 */
public class CustomPagerAdapter extends CustomFragmentStatePagerAdapter {
  private Context mContext;
  public static final String TAG = "CustomPagerAdapter";
  private final int PAGE_COUNT = 3;
  private int icons[] = {R.drawable.ic_city, R.drawable.ic_hotel, R.drawable.ic_login};
  private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};

  public CustomPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        Log.i(TAG, "List City Fragment");
        return ListCityFragment.newInstance();
      case 1:
        Log.i(TAG, "List Hotel Fragment");
        return ListHotelFragment.newInstance();
      case 2:
        if (ParseUser.getCurrentUser() != null) {
          Log.i(TAG, "User Management Fragment");
          return UserManagementFragment.newInstance();
        }
        else {
          Log.i(TAG, "Log In Fragment");
          return LogInFragment.newInstance();
        }
      default:
        return null;
    }
  }
  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    Drawable image = ContextCompat.getDrawable(mContext, icons[position]);
    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
    SpannableString sb = new SpannableString(" ");
    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return sb;
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

}
