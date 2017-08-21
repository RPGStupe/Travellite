package de.rpgstupe.travellite.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.rpgstupe.travellite.CountryListDataObject;
import de.rpgstupe.travellite.R;

/**
 * Created by Fabian on 20.08.2017.
 */

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private List<CountryListDataObject> mDataset;
    private Resources resources;
    private static CountryListAdapter.MyClickListener myClickListener;



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mCountryName;
        public ViewHolder(View mListItemCountry) {
            super(mListItemCountry);
            mCountryName = (TextView) mListItemCountry.findViewById(R.id.tv_list_item_country);
            mListItemCountry.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(CountryListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CountryListAdapter(Resources resources, List<CountryListDataObject> countryListDataSet) {
        mDataset = countryListDataSet;
        this.resources = resources;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View mListItemCountry = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_country, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(mListItemCountry);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
            holder.mCountryName.setText(mDataset.get(position).getCountryName());
            if (mDataset.get(position).isActivated()) {
                holder.mCountryName.setBackgroundColor(resources.getColor(R.color.colorPrimary));
            } else {
                holder.mCountryName.setBackgroundColor(resources.getColor(R.color.colorDeactivated));
            }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public List<CountryListDataObject> getmDataset() {
        return mDataset;
    }

}
