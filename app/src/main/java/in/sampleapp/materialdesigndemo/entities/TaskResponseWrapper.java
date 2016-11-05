package in.sampleapp.materialdesigndemo.entities;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * This is an entity class to parse and hold the data from the Webservice.
 */
public class TaskResponseWrapper implements Serializable
{
	@SerializedName("data")
	ArrayList<TaskResponse> tasklist;

	public ArrayList<TaskResponse> getTasklist()
	{
		return tasklist;
	}

	public void setTasklist(ArrayList<TaskResponse> tasklist)
	{
		this.tasklist = tasklist;
	}
}
