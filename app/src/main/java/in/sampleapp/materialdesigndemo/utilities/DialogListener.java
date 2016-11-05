package in.sampleapp.materialdesigndemo.utilities;

/**
 * This class is used to implement the Actions performed by the Dialog Pop up displayed to the User
 */
public interface DialogListener
{
	void onPositiveButtonClicked(String message);

	void onNegativeButtonClicked(String message);
}
