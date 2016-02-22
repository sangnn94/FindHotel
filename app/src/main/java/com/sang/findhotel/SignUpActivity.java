package com.sang.findhotel;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by sang on 08/12/2015.
 */
public class SignUpActivity extends AppCompatActivity {
  private Button mSignUpButton;
  private EditText mEmailEditText;
  private EditText mUserNameEditText;
  private EditText mPasswordEditText;
  private Toolbar mToolbar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    mToolbar = (Toolbar) findViewById(R.id.tool_bar);
    mSignUpButton = (Button) findViewById(R.id.sign_up_button);
    mUserNameEditText = (EditText) findViewById(R.id.username_edit_text);
    mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
    mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mSignUpButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // Check valid form
        StringBuilder errorMessage = new StringBuilder("Please enter a ");
        Boolean hasError = false;
        if (isEmpty(mEmailEditText)) {
          errorMessage.append("email");
          hasError = true;
        }
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
          Toast.makeText(SignUpActivity.this, errorMessage.toString(), Toast.LENGTH_SHORT).show();
          return;
        }
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Signing Up");
        dialog.show();
        // create new user
        ParseUser newUser = new ParseUser();
        newUser.setEmail(mEmailEditText.getText().toString());
        newUser.setUsername(mUserNameEditText.getText().toString());
        newUser.setPassword(mPasswordEditText.getText().toString());
        newUser.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            dialog.dismiss();
            if (e != null)
              Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            else {
            }
          }
        });
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private Boolean isEmpty(EditText editText) {
    return editText.getText().toString().trim().length() == 0 ? true : false;
  }
}
