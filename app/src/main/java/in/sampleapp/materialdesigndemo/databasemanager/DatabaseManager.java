package in.sampleapp.materialdesigndemo.databasemanager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.winjit.cryptography.EncryptionDecryption;

import in.sampleapp.materialdesigndemo.BuildConfig;
import in.sampleapp.materialdesigndemo.entities.TaskResponse;
import in.sampleapp.materialdesigndemo.utilities.Utilities;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides the DAOs used by the other classes.
 */
public class DatabaseManager extends OrmLiteSqliteOpenHelper
{

	private static final String DATABASE_NAME = "SampleMaterial_v1.0.db";
	private static final int DATABASE_VERSION = 1;
	EncryptionDecryption encryptionDecryption;
	byte[] iv, key;
	private Context _context;
	private Dao<?, String> simpleDao = null;
	private RuntimeExceptionDao<?, String> simpleRuntimeDao = null;
	private Class<?> className = null;

	public DatabaseManager(Context context, Class className)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		_context = context;
		this.className = className;

		encryptionDecryption = new EncryptionDecryption();
		try
		{
			// Gets the device Unique ID and Generates Key/IV Bytes
			iv = encryptionDecryption.getIVBytes(BuildConfig.APPLICATION_ID);
			key = encryptionDecryption.getKeyBytes(BuildConfig.APPLICATION_ID);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached value.
	 */
	public Dao<?, String> getDao(Class className) throws SQLException
	{
		if (simpleDao == null)
		{
			simpleDao = getDao(className);
		}
		return simpleDao;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Custom class. It will create it or just give the cached value. RuntimeExceptionDao only through
	 * RuntimeExceptions.
	 */
	public RuntimeExceptionDao<?, String> getSimpleDataDao()
	{
		if (simpleRuntimeDao == null)
			simpleRuntimeDao = getRuntimeExceptionDao(className);
		return simpleRuntimeDao;
	}

	/**
	 * All the records from the current selected Class
	 * 
	 */
	public ArrayList<?> getRecords()
	{
		RuntimeExceptionDao<?, String> simpleDao = getSimpleDataDao();
		ArrayList<?> encryptedlist = (ArrayList<?>) simpleDao.queryForAll();

		ArrayList<Object> plainList = new ArrayList<>();
		for (int i = 0; i < encryptedlist.size(); i++)
		{
			plainList.add(getDecryptedObject(encryptedlist.get(i)));
		}

		return plainList;
	}

	/**
	 * Insert the records in current selected Class
	 * 
	 * @param object
	 *            - data to be inserted
	 */
	public int addData(Object object)
	{
		RuntimeExceptionDao<Object, String> dao = (RuntimeExceptionDao<Object, String>) getSimpleDataDao();
		return dao.create(getEncryptedObject(object));
	}

	/**
	 * Delete all the data from the selected table
	 */
	public void deleteAll()
	{
		ConnectionSource connectionSource = getConnectionSource();
		try
		{
			TableUtils.clearTable(connectionSource, className);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void close()
	{
		super.close();
		simpleRuntimeDao = null;
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, TaskResponse.class);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			TableUtils.dropTable(connectionSource, TaskResponse.class, true);
			onCreate(db, connectionSource);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Delete all the tables data
	 */
	public void clearAllTables()
	{
		ConnectionSource connectionSource = getConnectionSource();
		try
		{
			TableUtils.clearTable(connectionSource, TaskResponse.class);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * This function is used to encryt the Field values present in the object
	 * 
	 * @param obj
	 *            - The Object to be encrypted
	 */
	private Object getEncryptedObject(Object obj)
	{

		Class<?> objClass = obj.getClass();

		Field[] fields = objClass.getDeclaredFields();

		Object encryptedObject = null;
		try
		{
			encryptedObject = Class.forName(objClass.getName()).newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				field.set(encryptedObject, EncryptionDecryption.encryptText(field.get(obj).toString(), key, iv));
			}
			catch (Exception e)
			{
				try
				{
					field.set(encryptedObject, field.get(obj));
				}
				catch (IllegalAccessException e1)
				{
					e1.printStackTrace();
				}
			}
		}

		return encryptedObject;
	}

	/**
	 * This function is used to decrypt the Field values present in the object
	 * 
	 * @param obj
	 *            - The Object to be decrypted
	 */
	private Object getDecryptedObject(Object obj)
	{

		Class<?> objClass = obj.getClass();

		Field[] fields = objClass.getDeclaredFields();

		Object decryptedObject = null;
		try
		{
			decryptedObject = Class.forName(objClass.getName()).newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				field.set(decryptedObject, encryptionDecryption.decryptText(field.get(obj).toString(), key, iv));
			}
			catch (Exception e)
			{
				try
				{
					field.set(decryptedObject, field.get(obj));
				}
				catch (IllegalAccessException e1)
				{
					e1.printStackTrace();
				}
			}
		}

		return decryptedObject;
	}
}
