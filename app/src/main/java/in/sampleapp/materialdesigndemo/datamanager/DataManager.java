package in.sampleapp.materialdesigndemo.datamanager;

import java.util.ArrayList;

import android.content.Context;

import in.sampleapp.materialdesigndemo.entities.TaskResponse;

/**
 * This class is used as singleton class to save the Task list to be used throughout the application.
 */
public class DataManager
{
	static DataManager dataManager;
	Context context;
	ArrayList<TaskResponse> arrlstTasks;

	private DataManager(Context context)
	{
		this.context = context;
		initializeDataHolders();
	}

	public static DataManager getInstance(Context context)
	{
		if (dataManager == null)
			dataManager = new DataManager(context);
		return dataManager;
	}

	private void initializeDataHolders()
	{
		if (arrlstTasks == null)
		{
			arrlstTasks = new ArrayList<>();
		}
	}

	public ArrayList<TaskResponse> getTasks()
	{
		return arrlstTasks;
	}

	public void setTasks(ArrayList<TaskResponse> arrlstTasks)
	{
		this.arrlstTasks = arrlstTasks;
	}
}
