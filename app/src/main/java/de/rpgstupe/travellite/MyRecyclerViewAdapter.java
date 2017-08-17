package de.rpgstupe.travellite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fabian on 25.06.2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private final Context context;

    public ArrayList<DataObject> getmDataset() {
        return mDataset;
    }

    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnLongClickListener {
        TextView primaryText;
        TextView secondaryText;
        ImageView cardImage;
        RelativeLayout layout_review;
        RelativeLayout layout_normal;

        public DataObjectHolder(View itemView) {
            super(itemView);
            primaryText = (TextView) itemView.findViewById(R.id.tv_journal_card_primary);
            secondaryText = (TextView) itemView.findViewById(R.id.tv_journal_card_secondary);
            cardImage = (ImageView) itemView.findViewById(R.id.img_journal_card);
            layout_normal = (RelativeLayout) itemView.findViewById(R.id.layout_normal);
            layout_review = (RelativeLayout) itemView.findViewById(R.id.layout_review);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            myClickListener.onItemLongClick(getAdapterPosition(), v);
            return true;
        }
    }


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_journal, parent, false);
        return new DataObjectHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DataObjectHolder dataObjectHolderNormal = (DataObjectHolder) holder;
        dataObjectHolderNormal.primaryText.setText(mDataset.get(position).getmText1() + " (" + mDataset.get(position).getDate() + ")");
        dataObjectHolderNormal.secondaryText.setText(mDataset.get(position).getmText2());
        if (mDataset.get(position).getCardImage() != null) {
            dataObjectHolderNormal.cardImage.setImageBitmap(mDataset.get(position).getCardImage());
            dataObjectHolderNormal.cardImage.setMaxHeight((int) (300 * context.getResources().getDisplayMetrics().density));
        } else {
            dataObjectHolderNormal.cardImage.setImageBitmap(null);
            dataObjectHolderNormal.cardImage.setMaxHeight(0);
        }
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
        notifyItemRangeChanged(0, getmDataset().size());
    }

    public void updateItems() {
        notifyItemRangeChanged(0, getmDataset().size());
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, getmDataset().size());
    }

    public DataObject getDataObject(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemLongClick(int position, View v);
    }
}