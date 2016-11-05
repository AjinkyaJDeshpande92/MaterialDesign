package in.sampleapp.materialdesigndemo.webservicemanager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.content.Context;

import in.sampleapp.materialdesigndemo.application.MaterialApplication;


public class WebserviceManager
{
	Context context;
	WebserviceResponse webAPIResponseListener;

	public WebserviceManager(Context context)
	{
		this.context = context;
	}

	/**
	 * Method to set WebResponse Listener form the calling class, so that the webService Manager an pass the Response data to the calling class.
	 */
	public void setWebResponseListener(WebserviceResponse webAPIResponseListener)
	{
		this.webAPIResponseListener = webAPIResponseListener;
	}

	private void callWebservice(final String url)
	{
		WebserviceResponseImplementor webResponseListener = new WebserviceResponseImplementor(context, webAPIResponseListener);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, webResponseListener, webResponseListener)
		{
			@Override
			public String getBodyContentType()
			{
				// This function is called when the POST body is not empty.
				return "application/json; charset=utf-8";
			}

		};
		MaterialApplication.getInstance().addToRequestQueue(jsonObjectRequest, "jobj_req");

	}

	public void fetchListFromServer()
	{
		String url = "https://dl.dropboxusercontent.com/u/6890301/tasks.json";
		callWebservice(url);
	}

	/**
	 * This class is used to process the response fetched from the WS.
	 */
	public class WebserviceResponseImplementor implements Response.ErrorListener, Response.Listener
	{
		WebserviceResponse webAPIResponseListener;
		Context context;

		public WebserviceResponseImplementor(Context context, WebserviceResponse webAPIResponseListener)
		{
			this.webAPIResponseListener = webAPIResponseListener;
			this.context = context;
		}

		@Override
		public void onErrorResponse(VolleyError error)
		{
			try
			{
				String strMessage = "";
				if (error instanceof TimeoutError || error instanceof NoConnectionError)
				{
					strMessage = "Internet connection is not available.";
				}
				else if (error instanceof AuthFailureError)
				{
					strMessage = "Unable to connect to server.Please try again later.";
				}
				else if (error instanceof ServerError)
				{
					strMessage = "Unable to connect to server.Please try again later.";
				}
				else if (error instanceof NetworkError)
				{
					strMessage = "Unable to find working internet connection.Please try again later.";
				}
				else if (error instanceof ParseError)
				{
					strMessage = "Unable to parse the response.Please try again later.";
				}
				if (webAPIResponseListener != null)
				{
					webAPIResponseListener.onFailure(strMessage);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onResponse(Object response)
		{
			MaterialApplication.getInstance().getBus().post(response);
		}

	}
}
