package de.rpgstupe.travellite.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.rpgstupe.travellite.CardDataObject;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.database.CardDatabaseObject;
import de.rpgstupe.travellite.utils.DatabaseUtil;

/**
 * Created by Fabian on 25.06.2017.
 */

public class JournalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String LOG_TAG = "JournalAdapter";
    private final Context context;
    private CardDatabaseObject mDatasetDatabase;

    public List<CardDataObject> getmDataset() {
        return mDataset;
    }

    private List<CardDataObject> mDataset;
    private static MyClickListener myClickListener;

    public List<CardDatabaseObject> getmDatasetDatabase() {
        List<CardDatabaseObject> cardDatabaseObjectList = new ArrayList<>();
        for (CardDataObject cardData : mDataset) {
            cardDatabaseObjectList.add(new CardDatabaseObject(cardData));
        }
        return cardDatabaseObjectList;
    }

    public static List<CardDatabaseObject> getmDatasetDatabase(List<CardDataObject> mDataset) {
        List<CardDatabaseObject> cardDatabaseObjectList = new ArrayList<>();
        for (CardDataObject cardData : mDataset) {
            cardDatabaseObjectList.add(new CardDatabaseObject(cardData));
        }
        return cardDatabaseObjectList;
    }

    public static void uploadAll(List<CardDataObject> cardDataObjectList) {
        for (CardDataObject cardDataObject : cardDataObjectList) {
            DatabaseUtil.uploadFile(cardDataObject.getCardImage(), Long.toString(cardDataObject.getId()));
        }
    }

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

    public JournalAdapter(List<CardDataObject> myDataset, Context context) {
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
        dataObjectHolderNormal.primaryText.setText(mDataset.get(position).getTitle() + " (" + mDataset.get(position).getDate() + ")");
        dataObjectHolderNormal.secondaryText.setText(mDataset.get(position).getNotes());
        if (mDataset.get(position).getCardImage() != null) {
            double imageWidth = context.getResources().getDisplayMetrics().widthPixels / 16d;
            double imageHeight = imageWidth*9d;
            dataObjectHolderNormal.cardImage.setMinimumHeight((int) (imageHeight));
            dataObjectHolderNormal.cardImage.setMaxHeight((int) (imageHeight));
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.skipMemoryCache(true);
            Glide.with(context).applyDefaultRequestOptions(requestOptions).asBitmap().load(mDataset.get(position).getCardImage()).transition(BitmapTransitionOptions.withCrossFade()).into(dataObjectHolderNormal.cardImage);
        } else {
            dataObjectHolderNormal.cardImage.setImageBitmap(null);
            dataObjectHolderNormal.cardImage.setMaxHeight(0);
        }
    }

    public void addItem(CardDataObject dataObj, int index) {
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

    public CardDataObject getDataObject(int position) {
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