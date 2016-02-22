package com.sang.findhotel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sang on 08/12/2015.
 */
public class AboutActivity extends AppCompatActivity{
  public static Intent newIntent(Context context){
    Intent intent = new Intent(context, AboutActivity.class);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
  }
}
