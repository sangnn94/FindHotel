package com.sang.findhotel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by sang on 08/12/2015.
 */
public class ListCityFragment extends Fragment {
  private RecyclerView mRecyclerView;
  CallBacks mCallbacks;
  public static final String TAG = "CityListFragment";

  public interface CallBacks {
    void onCitySelected(String city);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mCallbacks = (CallBacks) activity;
  }


  public static Fragment newInstance() {

    ListCityFragment listCityFragment = new ListCityFragment();
    return listCityFragment;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list_city, container, false);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_city_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setAdapter(new CityAdapter(CityList.get(getActivity()).getCities()));
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  private class CityHolder extends RecyclerView.ViewHolder implements CardView.OnClickListener {
    ImageView mImageView;
    TextView mTextView;
    CardView mCardView;
    City mCity;

    public CityHolder(View itemView) {
      super(itemView);
      mCardView = (CardView) itemView.findViewById(R.id.card_view);
      mImageView = (ImageView) itemView.findViewById(R.id.card_view_image_view);
      mTextView = (TextView) itemView.findViewById(R.id.city_name);
      mCardView.setOnClickListener(this);
    }

    public void bindCity(City city) {
      mCity = city;
      mTextView.setText(mCity.getName());
      if (mCity.getUrl() != null)
        Glide.with(getActivity())
                .load(mCity.getUrl())
                .centerCrop()
                .crossFade()
                .into(mImageView);
      else
        Glide.clear(mImageView);
    }

    @Override
    public void onClick(View v) {
      mCallbacks.onCitySelected(mCity.getName());
    }
  }

  private class CityAdapter extends RecyclerView.Adapter<CityHolder> {
    private int previousPosition = 0;
    private List<City> mCityList;

    public CityAdapter(List<City> list) {
      mCityList = list;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View view = inflater.inflate(R.layout.card_view_city_item, parent, false);
      CityHolder holder = new CityHolder(view);
      return holder;
    }

    @Override
    public void onBindViewHolder(final CityHolder holder, final int position) {
      City city = mCityList.get(position);
      holder.bindCity(city);
      if (position > previousPosition)
        AnimationUtils.recyclerViewAnimate(holder, true);
      else
        AnimationUtils.recyclerViewAnimate(holder, false);
      previousPosition = position;
    }

    @Override
    public int getItemCount() {
      return mCityList.size();
    }
  }
}
