package in.sampleapp.materialdesigndemo.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * This is an entity class to parse and hold the data from the Webservice.
 */
public class TaskResponse implements Serializable
{
	@DatabaseField
	@SerializedName("name")
	String Name;
	@DatabaseField
	@SerializedName("id")
	double ID;
	@DatabaseField
	@SerializedName("state")
	int State;
	@DatabaseField
	boolean bSoftDelete;

	public String getName()
	{
		return Name;
	}

	public void setName(String name)
	{
		Name = name;
	}

	public double getID()
	{
		return ID;
	}

	public void setID(double ID)
	{
		this.ID = ID;
	}

	public int getState()
	{
		return State;
	}

	public void setState(int state)
	{
		State = state;
	}

	public boolean isbSoftDelete()
	{
		return bSoftDelete;
	}

	public void setbSoftDelete(boolean bSoftDelete)
	{
		this.bSoftDelete = bSoftDelete;
	}
}
