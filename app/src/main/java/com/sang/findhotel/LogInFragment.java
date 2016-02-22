package com.sang.findhotel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by sang on 08/12/2015.
 */
public class LogInFragment extends Fragment {
  private EditText mUserNameEditText;
  private EditText mPasswordEditText;
  private Button mLogInButton;
  private TextView mSignUpTextView;
  private CallBacks mCallbacks;
  public static final int REQUEST_CODE = 1;
  LogInCallBacks CallBacks;
  interface LogInCallBacks{
    void refreshAdapter();
  }
  public static Fragment newInstance(){
    LogInFragment logInFragment = new LogInFragment();
    return logInFragment;
  }
  public interface CallBacks{
    void onLogInListener();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mCallbacks = (CallBacks) activity;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_log_in, container, false);
    mUserNameEditText = (EditText) view.findViewById(R.id.username_edit_text);
    mPasswordEditText = (EditText) view.findViewById(R.id.password_edit_text);
    mLogInButton = (Button) view.findViewById(R.id.log_in_button);
    mSignUpTextView = (TextView) view.findViewById(R.id.sign_up_text_view);

    mLogInButton.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v) {
        StringBuilder errorMessage = new StringBuilder("Please enter a ");
        Boolean hasError = false;
        if (isEmpty(mUserNameEditText)) {
          if (hasError)
            errorMessage.append(", ");
          errorMessage.append("username");
          hasError = true;
        }
        if (isEmpty(mPasswordEditText)) {
          if (hasError)
            errorMessage.append(", ");
          errorMessage.append("password");
          hasError = true;
        }
        if (hasError) {
          Toast.makeText(getActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show();
          return;
        }
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Please Wait");
        dialog.setMessage(getString(R.string.sign_in));
        dialog.show();
        ParseUser.logInInBackground(
                mUserNameEditText.getText().toString(),
                mPasswordEditText.getText().toString(),
                new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                    dialog.dismiss();
                    if(e == null){
                      mCallbacks.onLogInListener();
                    }
                    else{
                      Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
    String mSignUpString = "Create one";
    final SpannableStringBuilder sb = new SpannableStringBuilder(mSignUpString);
    final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

    sb.setSpan(fcs, 0, mSignUpString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    sb.setSpan(bss, 0, mSignUpString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    mSignUpTextView.append(sb);
    mSignUpTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(getActivity(), SignUpActivity.class), REQUEST_CODE);
      }
    });
    return view;
  }

  private Boolean isEmpty(EditText editText) {
    return editText.getText().toString().trim().length() == 0 ? true : false;
  }

}
