package dnsudhir.com.androidgenericwebcalls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Method Showing Usage of HttpWebCall
    mHttpwebCall();
    //Method Showing Usage of OkHttpWebCall
    mOkHttpWebCall();
  }

  /**
   *
   */
  private void mOkHttpWebCall() {

    OkHttpWebCall webCall = new OkHttpWebCall(this, null, "url");
    webCall.setProgressDialog(true);
    webCall.setCheckExecute(webCall, new OkHttpWebCall.OnCallComplete() {
      /**
       *
       * @param b if this boolean variable is false, response is either null or empty string
       * @param result this param is the response
       */
      @Override public void CallCompleted(boolean b, String result) {
        if (b) {
          //Code To Execute After Completion of WebCall
        }
      }
    });


  }

  private void mHttpwebCall() {

    HttpWebCall webCall = new HttpWebCall(this,new String[]{"key1","key2"},new String[]{"val1","val2"},"url");
    webCall.setProgressDialog(true);
    webCall.setCheckExecute(webCall, new HttpWebCall.OnCallComplete() {
      /**
       *
       * @param b if this boolean variable is false, response is either null or empty string
       * @param result this param is the response
       */
      @Override public void CallCompleted(boolean b, String result) {
        if (b) {
          //Code to execute After Completion of Statments
        }
      }
    });
  }
}
