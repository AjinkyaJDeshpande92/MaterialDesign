package in.sampleapp.materialdesigndemo.application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

/**
 * This is the singleton class used throughout the Application.
 * It handles the Volley network requests and Otto Bus to navigate the data between the screens.
 */
public class MaterialApplication extends Application
{
	public static final String TAG = MaterialApplication.class.getSimpleName();
	//Network Response Timeout
	public static final int MAX_REQUEST_TIMEOUT_MS = 120000;
	private static MaterialApplication mInstance;
	private RequestQueue mRequestQueue;
	private Bus mBus;

	public static synchronized MaterialApplication getInstance()
	{
		return mInstance;
	}

	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
	};

	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(getApplicationContext(), null);
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		req.setRetryPolicy(
				new DefaultRetryPolicy(MAX_REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		getRequestQueue().add(req);

	}

	public Bus getBus()
	{
		if (mBus == null)
		{
			mBus = new Bus(ThreadEnforcer.MAIN);
		}
		return mBus;
	}

}
