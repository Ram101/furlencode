package com.example.ramanandank.nightowl;

/**
 * Created by ramanandank on 3/12/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MakePhotoActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Button takepicture, send;
    ImageView iv;
    String mCategory;
    private TextInputLayout inputLayoutPlaceName, inputLayoutReview;
    TextView timeFrom, timeTo;
    timeData mTo, mFrom;
    EditText inputPlaceName, inputReview;
    Bitmap mImageBm;
    ArrayList<timeData> timeholder = new ArrayList<timeData>();
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_take);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send);
        setSupportActionBar(toolbar);
        Resources res = getResources();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        mTo = new timeData();
        mFrom = new timeData();
        final String[] category = res.getStringArray(R.array.category);
        inputLayoutPlaceName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutReview = (TextInputLayout) findViewById(R.id.input_layout_review);
        inputPlaceName = (EditText) findViewById(R.id.input_name);
        inputReview = (EditText) findViewById(R.id.input_review);
        timeFrom = (TextView) findViewById(R.id.time_from);
        timeTo = (TextView) findViewById(R.id.time_to);
        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeholder.clear();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(MakePhotoActivity.this, 0, 0, true);
                timePickerDialog.show(getFragmentManager(), "FromTimepickerdialog");
            }
        });
        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(MakePhotoActivity.this, 0, 0, true);
                timePickerDialog.show(getFragmentManager(), "ToTimepickerdialog");

            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = category[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        takepicture = (Button) findViewById(R.id.button);
        send = (Button) findViewById(R.id.button_send);
        iv = (ImageView) findViewById(R.id.imageView);

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePlaceName()) {
                    return;
                }

                if (!validateReview()) {
                    return;
                }

                if (mImageBm == null) {
                    Toast.makeText(getApplicationContext(), "Please upload image to proceed", Toast.LENGTH_SHORT);
                    return;
                }
                addData add = new addData();
                Place addPlace = new Place();
                addPlace.setDescription(inputReview.getText().toString());
                addPlace.setName(inputPlaceName.getText().toString());
                addPlace.setCategory(mCategory);
                addPlace.setFrom(timeholder.get(0).getHour() + ":" + timeholder.get(0).getMinute());
                addPlace.setTo(timeholder.get(1).getHour() + ":" + timeholder.get(1).getMinute());
                addPlace.setLatitude(location.getLatitude());
                addPlace.setLongitude(location.getLongitude());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mImageBm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                addPlace.setPic(encodedBase64);
                add.setUser("guest");
                add.setPlace(addPlace);
                Gson sendingJson = new Gson();
                String Thejson = sendingJson.toJson(add);
                new uploadDataToServer().execute(Thejson);

            }
        });
        inputPlaceName.addTextChangedListener(new MyTextWatcher(inputPlaceName));
        inputReview.addTextChangedListener(new MyTextWatcher(inputReview));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        mImageBm = (Bitmap) data.getExtras().get("data");

        iv.setImageBitmap(mImageBm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        timeData temp = new timeData();
        temp.setHour(hourOfDay);
        temp.setMinute(minute);
        timeholder.add(temp);
        if (timeholder.size() == 1) {
            timeFrom.setText("Opening Time is "+timeholder.get(0).getHour() + ":" + timeholder.get(0).getMinute());
        }
        if (timeholder.size() == 2) {
            timeTo.setText("Closing TIme is "+timeholder.get(1).getHour() + ":" + timeholder.get(1).getMinute());
        }
    }


    private class timeData {
        int hour;
        int minute;

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

    }

    private boolean validatePlaceName() {
        if (inputPlaceName.getText().toString().trim().isEmpty()) {
            inputLayoutPlaceName.setError(getString(R.string.err_msg_place_name));
            requestFocus(inputPlaceName);
            return false;
        } else {
            inputLayoutPlaceName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateReview() {
        if (inputReview.getText().toString().trim().isEmpty()) {
            inputLayoutReview.setError(getString(R.string.err_msg_review));
            requestFocus(inputPlaceName);
            return false;
        } else {
            inputLayoutPlaceName.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validatePlaceName();
                    break;
                case R.id.input_review:
                    validateReview();
                    break;

            }
        }
    }

    private class uploadDataToServer extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            return Uploading(params[0]);
        }

        private String Uploading(String json) {
            final String FORECAST_BASE_URL = "http:/192.168.2.168:5000/furlencode/add/place";
            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(json);
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Response"+s,Toast.LENGTH_SHORT).show();
        }
    }
}
