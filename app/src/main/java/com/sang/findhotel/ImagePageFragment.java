package com.sang.findhotel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by sang on 22/12/2015.
 */
public class ImagePageFragment extends Fragment {
	private ImageView image;
	private String url;
	private static final String ARGS = "ImagePageFragment";
	public static Fragment newInstance(String url){
		ImagePageFragment fragment = new ImagePageFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARGS, url);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		url = getArguments().getString(ARGS);
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hotel_image_item, container,false);
		image = (ImageView) view.findViewById(R.id.photo_image_view);
		Glide.with(container.getContext()).load(url).centerCrop().into(image);
		return view;
	}
}
