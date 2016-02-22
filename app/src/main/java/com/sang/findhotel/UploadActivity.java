package com.sang.findhotel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 13/12/2015.
 */
public class UploadActivity extends AppCompatActivity {
	private Toolbar mToolbar;
	private EditText mNameEditText, mAddressEditText, mPhoneEditText, mPriceHourEditText, mPriceDayEditText;
	private Spinner mCityName;
	private ImageButton mChoosePhotoButton;
	private RecyclerView mPhotoRecyclerView;
	private PhotoAdapter photoAdapter;
	private String[] COUNTRIES = new String[]{
					"Hà Nội", "Đà Nẵng", "Huế", "Nha Trang", "Hồ Chí Minh"
	};

	private static final int REQUEST_CHOOSE_PHOTO = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		mToolbar = (Toolbar) findViewById(R.id.tool_bar);
		mNameEditText = (EditText) findViewById(R.id.name_hotel_edit_text);
		mAddressEditText = (EditText) findViewById(R.id.address_hotel_edit_text);
		mPhoneEditText = (EditText) findViewById(R.id.phone_hotel_edit_text);
		mPriceHourEditText = (EditText) findViewById(R.id.price_hotel_edit_text);
		mPriceDayEditText = (EditText) findViewById(R.id.price_day_hotel_edit_text);
		mCityName = (Spinner) findViewById(R.id.city_name_text_view);
		mChoosePhotoButton = (ImageButton) findViewById(R.id.choose_photo_button);
		mPhotoRecyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
		setSupportActionBar(mToolbar);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, COUNTRIES);
		mCityName.setAdapter(adapter);

		List<String> list = new ArrayList<>();
		mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(UploadActivity.this, 3));
		mPhotoRecyclerView.addItemDecoration(new SpacePhotoDecoration());
		photoAdapter = new PhotoAdapter(UploadActivity.this, list);
		mPhotoRecyclerView.setAdapter(photoAdapter);

		mChoosePhotoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent = intent.createChooser(intent, "Select Hotel's Photo");
				startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
			}
		});
	}

	private class SpacePhotoDecoration extends RecyclerView.ItemDecoration {
		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			outRect.left = 8;
			outRect.right = 8;
		}
	}

	private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView imageView;
		private String mPhotoPath;

		public PhotoHolder(View itemView) {
			super(itemView);
			imageView = (ImageView) itemView.findViewById(R.id.photo_image_view);
			imageView.setOnClickListener(this);
		}

		private void bindPhoto(Context context, String filePath) {
			mPhotoPath = filePath;
			Glide.with(context).load(Uri.parse("file://" + mPhotoPath)).into(imageView);
		}

		@Override
		public void onClick(View v) {
		}
	}

	private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
		private List<String> photos;
		private Context mContext;

		public PhotoAdapter(Context context, List<String> list) {
			this.photos = list;
			this.mContext = context;
		}

		private void addPhotoToList(String photoPath) {
			photos.add(photoPath);
		}

		private List<String> getPhotoList() {
			return photos;
		}

		@Override
		public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View view = inflater.inflate(R.layout.image_item, parent, false);
			PhotoHolder holder = new PhotoHolder(view);
			return holder;
		}

		@Override
		public void onBindViewHolder(PhotoHolder holder, int position) {
			String file = photos.get(position);
			holder.bindPhoto(mContext, file);
		}

		@Override
		public int getItemCount() {
			return photos.size();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		if (requestCode == REQUEST_CHOOSE_PHOTO && data != null) {
			Uri selectedPhoto = data.getData();
			String photoPatch;
			String[] filePath = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(selectedPhoto, filePath, null, null, null);
			try {
				cursor.moveToFirst();
				photoPatch = cursor.getString(cursor.getColumnIndex(filePath[0]));
			} finally {
				cursor.close();
			}
			if (!photoAdapter.getPhotoList().contains(photoPatch)) {
				photoAdapter.addPhotoToList(photoPatch);
				photoAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.upload_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_done:
				String phone = mPhoneEditText.getText().toString();
				String priceHour = mPriceHourEditText.getText().toString();
				String priceDay = mPriceDayEditText.getText().toString();
				String name = mNameEditText.getText().toString();
				String address = mAddressEditText.getText().toString();
				if (!phone.equals("")
								&& !priceHour.equals("")
								&& !priceDay.equals("")
								&& !name.equals("")
								&& !address.equals("")) {
					final ProgressDialog dialog = new ProgressDialog(UploadActivity.this);
					dialog.setTitle("Please Wait");
					dialog.setMessage("Uploading");
					dialog.show();
					Hotel hotel = new Hotel();
					hotel.setCity(mCityName.getSelectedItem().toString());
					hotel.setName(name);
					hotel.setAddress(address);
					hotel.setPriceDay(Integer.parseInt(priceDay));
					hotel.setPriceHour(Integer.parseInt(priceHour));
					hotel.setPhone(Integer.parseInt(phone));
					List<ParseFile> photos = new ArrayList<>();
					List<String> photoPaths = photoAdapter.getPhotoList();
					for (String photo : photoPaths) {
						ParseFile parseFile = new ParseFile("Photo", saveScaledPhoto(photo));
						photos.add(parseFile);
					}
					hotel.setPhoto(photos);
					hotel.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							dialog.dismiss();
							finish();
						}
					});
				} else {
					Toast.makeText(this, "please choice photo", Toast.LENGTH_SHORT).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private byte[] saveScaledPhoto(String filePath) {
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
						200,
						200 * bitmap.getHeight() / bitmap.getWidth(),
						false);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] scaledData = stream.toByteArray();
		return scaledData;
	}
}
