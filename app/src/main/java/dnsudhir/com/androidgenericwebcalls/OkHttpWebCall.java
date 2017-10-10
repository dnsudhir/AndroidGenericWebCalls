package dnsudhir.com.androidgenericwebcalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpWebCall extends AsyncTask<Void, Void, String> {

  private boolean dialog;
  private String title;
  private String message;
  private ProgressDialog progressDialog;
  private Context context;
  private RequestBody requestBody;
  private String URL;
  private Request request;
  private OnCallComplete onCallComplete;

  public OkHttpWebCall(Context context, RequestBody requestBody, String URL) {
    this.context = context;
    this.requestBody = requestBody;
    this.URL = URL;
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
    if (requestBody != null) {
      request = new Request.Builder().url(URL).post(requestBody).build();
    } else {
      request = new Request.Builder().url(URL).build();
    }
  }

  @Override protected String doInBackground(Void... voids) {
    String strResponse = "";
    OkHttpClient okHttpClient = new OkHttpClient();
    try {
      Response response = okHttpClient.newCall(request).execute();
      if (response.body() != null) {
        strResponse = response.body().string();
      }else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return strResponse;
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

  public void setCheckExecute(OkHttpWebCall webCall, OnCallComplete onCallComplete) {
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
