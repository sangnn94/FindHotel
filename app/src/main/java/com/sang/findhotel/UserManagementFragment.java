package com.sang.findhotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sang on 11/12/2015.
 */
public class UserManagementFragment extends Fragment{
  private Button mUploadButton;
  public static Fragment newInstance(){
    return new UserManagementFragment();
  }
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_management, container, false);
    mUploadButton = (Button) view.findViewById(R.id.upload_button);
    mUploadButton.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), UploadActivity.class));
      }
    });
    return view;
  }
}
