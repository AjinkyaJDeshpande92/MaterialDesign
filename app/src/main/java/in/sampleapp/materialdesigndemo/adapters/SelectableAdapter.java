package in.sampleapp.materialdesigndemo.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

/**
 * This class is used to handle the multiple select/de select for the Recycler View Adapter
 */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{

	private SparseBooleanArray selectedItems;

	public SelectableAdapter()
	{
		selectedItems = new SparseBooleanArray();
	}

	/**
	 * Indicates if the item at position position is selected
	 * 
	 * @param position
	 *            Position of the item to check
	 * @return true if the item is selected, false otherwise
	 */
	public boolean isSelected(int position)
	{
		return getSelectedItems().contains(position);
	}

	/**
	 * Toggle the selection status of the item at a given position
	 * 
	 * @param position
	 *            Position of the item to toggle the selection status for
	 */
	public void toggleSelection(int position)
	{
		if (selectedItems.get(position, false))
		{
			selectedItems.delete(position);
		}
		else
		{
			selectedItems.put(position, true);
		}
		notifyItemChanged(position);
	}

	/**
	 * Indicates the list of selected items
	 * 
	 * @return List of selected items ids
	 */
	public List<Integer> getSelectedItems()
	{
		List<Integer> items = new ArrayList<>(selectedItems.size());
		for (int i = 0; i < selectedItems.size(); ++i)
		{
			items.add(selectedItems.keyAt(i));
		}
		return items;
	}
}
