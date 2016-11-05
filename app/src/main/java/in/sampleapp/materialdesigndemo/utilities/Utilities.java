package in.sampleapp.materialdesigndemo.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.text.DecimalFormat;

import in.sampleapp.materialdesigndemo.R;


public class Utilities
{
    Context mContext;
    Activity mActivity;
    public static SimpleArcDialog pgLoader;
    public static Snackbar snackbar = null;
    public static AlertDialog.Builder ConfirmationDialog;
    /**
     * This is a paramaterized constructor used to initialize Utilities.
     */
    public Utilities(Activity callingActivity)
    {
        mActivity = callingActivity;
        mContext = callingActivity;
    }

    /**
     * This is a paramaterized constructor used to initialize Utilities.
     *
     */
    public Utilities(Context context)
    {
        mContext = context;
    }


    /**
     * This function shows a Progress (Loading) dialog to User.
     *
     * @param strMessage
     *            -The Message needed to be displayed to the User on the Loading Dialog.
     */
    public static void showLoader(String strMessage, final Context currentContext)
    {
        try
        {

            try
            {
                if (pgLoader != null && pgLoader.isShowing())
                    pgLoader.dismiss();
                pgLoader = null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // Create New Loader
            ArcConfiguration configuration = new ArcConfiguration(currentContext);
            configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
            configuration.setText(strMessage);
            configuration.setTextColor(currentContext.getResources().getColor(android.R.color.black));
            pgLoader = new SimpleArcDialog(currentContext);
            int[] colorArray =
                    { currentContext.getResources().getColor(android.R.color.holo_blue_bright), currentContext.getResources().getColor(android.R.color.holo_purple),
                            currentContext.getResources().getColor(android.R.color.holo_blue_dark) };
            configuration.setColors(colorArray);
            configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
            pgLoader.setConfiguration(configuration);
            pgLoader.setCancelable(false);
            pgLoader.show();
        }
        catch (Exception e)
        {
            if (pgLoader != null)
            {
                if (pgLoader.isShowing())
                    pgLoader.dismiss();
            }
            e.printStackTrace();
        }
    }

    /**
     * This function dismiss the current loading dialog present on the screen
     */
    public static void dismissLoader()
    {
        try
        {
            if (pgLoader != null && pgLoader.isShowing())
                pgLoader.dismiss();
            pgLoader = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This function is used to format the Decimal into #.## format
     *
     * @param number
     *            -The Input number
     */
    public String formatDecimal(double number)
    {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(number);
    }

    /**
     * Show an alert dialog box to user with a Custom buttons.
     *
     * @param strMessage
     *            - The Message to be displayed
     */
    @SuppressLint("NewApi")
    public void showMultipleOptionsDialog(final String strMessage, String positiveButtonText, String negativeButtonText,
                                          final DialogListener dialogListener)
    {
        try
        {
            ConfirmationDialog = new AlertDialog.Builder(mContext);
            ConfirmationDialog.setMessage(strMessage);
            ConfirmationDialog.setCancelable(false);
            ConfirmationDialog.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    dialogListener.onPositiveButtonClicked(strMessage);
                }
            });
            ConfirmationDialog.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    dialogListener.onNegativeButtonClicked(strMessage);
                }
            });
            ConfirmationDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
