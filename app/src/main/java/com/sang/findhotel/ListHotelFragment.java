package com.sang.findhotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 08/12/2015.
 */
public class ListHotelFragment extends Fragment {
	public static final String ARGS = "ARGS";
	public String mCityName = "Hà Nội";
	private RecyclerView mRecyclerView;
	private HotelAdapter mHotelAdapter;
	public static final String TAG = "ListHotelFragment";

	public static Fragment newInstance() {
		ListHotelFragment listHotelFragment = new ListHotelFragment();
		return listHotelFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void updateCity(String cityName) {
		mCityName = cityName;
		fetchData(mCityName);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_hotel, container, false);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_hotel_recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		fetchData(mCityName);
		Log.i(TAG, "OnCreateView");
		return view;
	}

	private class HotelHolder extends RecyclerView.ViewHolder implements CardView.OnClickListener {
		private ImageView mImageView;
		private TextView mHotelName;
		private TextView mHotelPrice;
		private Hotel mHotel;
		private CardView mCardView;

		public HotelHolder(View itemView) {
			super(itemView);
			mImageView = (ImageView) itemView.findViewById(R.id.hotel_photo);
			mHotelName = (TextView) itemView.findViewById(R.id.hotel_name);
			mHotelPrice = (TextView) itemView.findViewById(R.id.hotel_price);
			mCardView = (CardView) itemView.findViewById(R.id.card_view);
			mCardView.setOnClickListener(this);
		}

		private void bindHotel(Hotel hotel) {
			this.mHotel = hotel;
			mHotelName.setText(mHotel.getName());
			mHotelPrice.setText(String.valueOf(mHotel.getPriceDay()));
			if (!mHotel.getPhoto().isEmpty()) {
				Glide.with(getActivity()).load(mHotel.getPhoto().get(0).getUrl()).centerCrop().into(mImageView);
			} else
				Glide.clear(mImageView);
		}

		@Override
		public void onClick(View v) {
			startActivity(DetailHotelActivity.newIntent(getActivity(), mHotel.getId()));
		}
	}

	private class HotelAdapter extends RecyclerView.Adapter<HotelHolder> {
		private int previousPosition = 0;
		List<Hotel> hotels;

		HotelAdapter(List<Hotel> list) {
			hotels = list;
		}

		private List<Hotel> getList() {
			return hotels;
		}

		@Override
		public HotelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_hotel_item, parent, false);
			HotelHolder hotelHolder = new HotelHolder(view);
			return hotelHolder;
		}

		@Override
		public void onBindViewHolder(HotelHolder holder, int position) {
			Hotel hotel = hotels.get(position);
			holder.bindHotel(hotel);
			if (position > previousPosition)
				AnimationUtils.recyclerViewAnimate(holder, true);
			else
				AnimationUtils.recyclerViewAnimate(holder, false);
			previousPosition = position;
		}

		@Override
		public int getItemCount() {
			return hotels.size();
		}
	}

	private void fetchData(final String cityName) {
		ParseQuery<Hotel> query = ParseQuery.getQuery(Hotel.class);
		query.whereEqualTo("City", cityName);
		final List<Hotel> list = new ArrayList<>();
		query.findInBackground(new FindCallback<Hotel>() {
			@Override
			public void done(List<Hotel> objects, ParseException e) {
				for (Hotel hotel : objects) {
					Hotel newHotel = new Hotel();
					newHotel.setId(hotel.getObjectId());
					newHotel.setName(hotel.getName());
					newHotel.setPriceDay(hotel.getPriceDay());
					newHotel.setPhoto(hotel.getPhoto());
					list.add(newHotel);
				}
				updateAdapter(list);
			}
		});
	}

	private void updateAdapter(List<Hotel> list) {
		if (mHotelAdapter == null)
			mRecyclerView.setAdapter(new HotelAdapter(list));
		else {
			mHotelAdapter.getList().clear();
			mHotelAdapter.getList().addAll(list);
			mHotelAdapter.notifyDataSetChanged();
		}
	}
}
