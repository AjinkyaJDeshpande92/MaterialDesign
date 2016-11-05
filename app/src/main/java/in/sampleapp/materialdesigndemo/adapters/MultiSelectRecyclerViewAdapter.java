package in.sampleapp.materialdesigndemo.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.sampleapp.materialdesigndemo.R;
import in.sampleapp.materialdesigndemo.entities.TaskResponse;
import in.sampleapp.materialdesigndemo.utilities.Utilities;

/**
 * This class is used to handle the Listing to be displayed on the Screen to User.
 */
public class MultiSelectRecyclerViewAdapter extends SelectableAdapter<MultiSelectRecyclerViewAdapter.ViewHolder>
{

	Utilities utilities;
	int iCurrentPage;
	private ArrayList<TaskResponse> mArrayList;
	private Context mContext;
	private ViewHolder.ClickListener clickListener;

	public MultiSelectRecyclerViewAdapter(Context context, ArrayList<TaskResponse> arrayList, ViewHolder.ClickListener clickListener,
			int iCurrentPage)
	{
		this.mArrayList = arrayList;
		this.mContext = context;
		this.clickListener = clickListener;
		this.iCurrentPage = iCurrentPage;
		utilities = new Utilities(context);

	}

	// Create new views
	@Override
	public MultiSelectRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{

		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_multiselect, null);

		ViewHolder viewHolder = new ViewHolder(itemLayoutView, clickListener);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position)
	{

		viewHolder.tvName.setText(mArrayList.get(position).getName());
		viewHolder.tvID.setText("" + utilities.formatDecimal(mArrayList.get(position).getID()));
		viewHolder.chkSelection.setChecked(isSelected(position) ? true : false);
		mArrayList.get(position).setbSoftDelete(viewHolder.chkSelection.isChecked());
		if (iCurrentPage == 0)
		{
			// Done Page
			if (mArrayList.get(position).getState() == 1)
			{
				viewHolder.linlayParent.setVisibility(View.VISIBLE);
				viewHolder.vwLine.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.linlayParent.setVisibility(View.GONE);
				viewHolder.vwLine.setVisibility(View.GONE);
			}
		}
		else
		{
			// Pending Page
			if (mArrayList.get(position).getState() == 0)
			{
				viewHolder.linlayParent.setVisibility(View.VISIBLE);
				viewHolder.vwLine.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.linlayParent.setVisibility(View.GONE);
				viewHolder.vwLine.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public int getItemCount()
	{
		return mArrayList.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{

		public TextView tvName, tvID;
		CheckBox chkSelection;
		LinearLayout linlayParent;
		View vwLine;
		private ClickListener listener;

		public ViewHolder(View itemLayoutView, ClickListener listener)
		{
			super(itemLayoutView);

			this.listener = listener;

			tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
			tvID = (TextView) itemLayoutView.findViewById(R.id.tvID);
			chkSelection = (CheckBox) itemView.findViewById(R.id.xchkBoxSelection);
			linlayParent = (LinearLayout) itemView.findViewById(R.id.xlinlayParent);
			vwLine = itemView.findViewById(R.id.xvwLine);
			itemLayoutView.setOnClickListener(this);
			chkSelection.setOnClickListener(this);
		}

		@Override
		public void onClick(View v)
		{
			if (listener != null)
			{
				if (v == chkSelection)
					listener.onItemClicked(getAdapterPosition(), true);
				else
					listener.onItemClicked(getAdapterPosition(), false);
			}
		}

		public interface ClickListener
		{
			void onItemClicked(int position, boolean bChkBox);
		}
	}
}
