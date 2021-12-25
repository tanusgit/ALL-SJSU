package edu.sjsu.homeworkimage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ThreadedDownloadActivity extends AppCompatActivity {

	EditText urlEdit;
	ImageView img;
	Button reset;
	Button async, message, runnable;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.urlEdit = (EditText) findViewById(R.id.edit_image_url);

		urlEdit = findViewById(R.id.edit_image_url);
		img = findViewById(R.id.myimage);
		reset = findViewById(R.id.reset_image);
		runnable = findViewById(R.id.run_runnable);
		message = findViewById(R.id.run_messages);
		async = findViewById(R.id.run_async_task);
	}

	public void resetImage(View v) {
		urlEdit.setText("");
		img.setImageResource(R.drawable.redapple);
	}

	public void runAsyncTask(View v) {
		String u = urlEdit.getText().toString();
		if (u.isEmpty()) {
			Toast.makeText(getApplicationContext(), "please enter an url",
					Toast.LENGTH_SHORT).show();
		} else {
			LoadImage load = new LoadImage(img);
			load.execute(u);
		}

	}


	public void runMessages() {
		String u = urlEdit.getText().toString();
		if (u.isEmpty()) {
			Toast.makeText(getApplicationContext(), "please enter an url",
					Toast.LENGTH_SHORT).show();
		} else {
			LoadImage load = new LoadImage(img);
			load.execute(u);
		}
	}

	public void runRunnable() {
		String u = urlEdit.getText().toString();
		if (u.isEmpty()) {
			Toast.makeText(getApplicationContext(), "please enter an url",
					Toast.LENGTH_SHORT).show();
		} else {
			LoadImage load = new LoadImage(img);
			load.execute(u);
		}
	}

	private class LoadImage extends AsyncTask<String, Void, Bitmap> {
		ImageView imgv;

		public LoadImage(ImageView v) {
			this.imgv = v;
		}

		@Override
		protected Bitmap doInBackground(String... strings) {
			String url = strings[0];
			Bitmap bitmap = null;
			try {
				InputStream input = new java.net.URL(url).openStream();
				bitmap = BitmapFactory.decodeStream(input);
			} catch (IOException e) {

			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap b) {
			img.setImageBitmap(b);
		}

	}
	Bitmap downloadBitmap (String url) {
		return null;
	}


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}