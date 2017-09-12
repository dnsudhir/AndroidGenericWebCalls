package dnsudhir.com.androidgenericwebcalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import java.util.HashMap;

public class HttpWebCall extends AsyncTask<Void, Void, String> {

  private boolean dialog;
  private String title;
  private String message;
  private Context context;
  private String[] keys;
  private String[] values;
  private String URL;
  private ProgressDialog progressDialog;
  private OnCallComplete onCallComplete;
  private HashMap<String, String> hashMap;

  public HttpWebCall(Context context, String[] keys, String[] values, String URL) {
    this.context = context;
    this.keys = keys;
    this.values = values;
    this.URL = URL;
    hashMap = new HashMap<>();
  }

  public void setProgressDialog(boolean dialog) {
    this.dialog = dialog;
    title = "Please Wait";
    message = "Loading...";
  }

  public void setProgressDialog(boolean dialog, String title, String message) {
    this.dialog = dialog;
    this.title = title;
    this.message = message;
  }

  @Override protected void onPreExecute() {
    super.onPreExecute();
    if (dialog) {
      progressDialog = ProgressDialog.show(context, title, message);
    }

    for (int i = 0; i < keys.length; i++) {
      hashMap.put(keys[i], values[i]);
    }
  }

  @Override protected String doInBackground(Void... voids) {
    RequestHandler requestHandler = new RequestHandler();
    return requestHandler.sendPostRequest(URL, hashMap);
  }

  @Override protected void onPostExecute(String s) {
    super.onPostExecute(s);
    if (dialog && progressDialog.isShowing()) progressDialog.dismiss();

    if (s != null && !s.contentEquals("")) {
      onCallComplete.CallCompleted(true, s);
    } else {
      onCallComplete.CallCompleted(false, s);
    }
  }

  public void setCheckExecute(HttpWebCall webCall, OnCallComplete onCallComplete) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
      this.onCallComplete = onCallComplete;
      webCall.execute();
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("No Internet Connection");
      builder.setMessage("Please Check Your Internet Connection");
      builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {
          ((Activity) context).finish();
          context.startActivity((((Activity) context).getIntent()));
        }
      });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {
          ((Activity) context).finish();
        }
      });
      builder.setCancelable(false);
      builder.show();
    }
  }
  interface OnCallComplete {
    void CallCompleted(boolean b, String result);
  }
}
