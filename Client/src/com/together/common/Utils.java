package com.together.common;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.together.constants.Constants;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * @class Utils
 *
 * @brief Helper methods shared by various Activities.
 */
public class Utils {

	/**
	 * Return an uppercase version of the input or null if user gave no input.
	 * If user gave no input and @a showToast is true a toast is displayed to
	 * this effect.
	 */
	public static String uppercaseInput(Context context, String input,
			boolean showToast) {
		if (input.isEmpty()) {
			if (showToast)
				Utils.showToast(context, "no input provided");
			return null;
		} else
			// Convert the input entered by the user so it's in
			// uppercase.
			return input.toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Show a toast message.
	 */
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method is used to hide a keyboard after a user has finished typing
	 * the url.
	 */
	public static void hideKeyboard(Activity activity, IBinder windowToken) {
		InputMethodManager mgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(windowToken, 0);
	}

	/**
	 * Set the result of the Activity to indicate whether the operation on the
	 * content succeeded or not.
	 * 
	 * @param activity
	 *            The Activity whose result is being set.
	 * @param pathToContent
	 *            The pathname to the content file.
	 * @param failureReason
	 *            String to add to add as an extra to the Intent passed back to
	 *            the originating Activity if the @a pathToContent is null.
	 */
	public static void setActivityResult(Activity activity, Uri pathToContent,
			String failureReason) {
		if (pathToContent == null)
			// Indicate why the operation on the content was
			// unsuccessful or was cancelled.
			activity.setResult(Activity.RESULT_CANCELED,
					new Intent("").putExtra("reason", failureReason));
		else
			// Set the result of the Activity to designate the path to
			// the content file resulting from a successful operation.
			activity.setResult(Activity.RESULT_OK,
					new Intent("", pathToContent));
	}

	/**
	 * Set the result of the Activity to indicate whether the operation on the
	 * content succeeded or not.
	 * 
	 * @param activity
	 *            The Activity whose result is being set.
	 * @param resultCode
	 *            The result of the Activity, i.e., RESULT_CANCELED or
	 *            RESULT_OK.
	 * @param failureReason
	 *            String to add to add as an extra to the Intent passed back to
	 *            the originating Activity if the result of the Activity is
	 *            RESULT_CANCELED.
	 */
	public static void setActivityResult(Activity activity, int resultCode,
			String failureReason) {
		if (resultCode == Activity.RESULT_CANCELED)
			// Indicate why the operation on the content was
			// unsuccessful or was cancelled.
			activity.setResult(Activity.RESULT_CANCELED,
					new Intent("").putExtra("reason", failureReason));
		else
			// Everything is ok.
			activity.setResult(Activity.RESULT_OK);
	}

	/**
	 * Ensure this class is only used as a utility.
	 */
	private Utils() {
		throw new AssertionError();
	}

	public static void switchToFragment(Activity activity, Fragment to,
			int resourceID, String tagIfNotExist, Fragment... from) {
		FragmentTransaction ft = activity.getFragmentManager()
				.beginTransaction();

		for (Fragment fragment : from) {
			if (fragment != null) {
				if (fragment.isAdded()) {
					ft.hide(fragment);
				}
			}
		}

		if (to.isAdded()) {
			ft.show(to);
		} else {
			ft.add(resourceID, to, tagIfNotExist);
		}

		ft.commit();

	}

	public static String uploadFile(File sourceFile) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		if (!sourceFile.isFile()) {
			// dialog.dismiss();
		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(Constants.AUDIO_ENDPOINT);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("file", "temp.amr");

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
						+ "temp.amr" + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				int serverResponseCode = conn.getResponseCode();
				InputStream is = conn.getInputStream();
				String serverResponseMessage = IOUtils.toString(is, "UTF-8");

				Log.i("TAG", serverResponseMessage);

				if (serverResponseCode == 200) {
					JSONObject j = new JSONObject(serverResponseMessage);
					int status = j.getInt("status");
					if (status == 0) {
						return j.getString("file_path");
					} else {
						return null;
					}
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

			} catch (Exception e) {

				// dialog.dismiss();
				e.printStackTrace();
			}
			// dialog.dismiss();

		} // End else block
		return null;
	}
}
