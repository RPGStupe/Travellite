package de.rpgstupe.travellite.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.snatik.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.rpgstupe.travellite.CardDataObject;
import de.rpgstupe.travellite.Configuration;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.adapters.JournalAdapter;
import de.rpgstupe.travellite.database.CardDatabaseObject;
import de.rpgstupe.travellite.payment.Donations;
import de.rpgstupe.travellite.utils.AdUtil;

/**
 * Created by Fabian on 25.06.2017.
 */

public class CardViewActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;
    private RecyclerView mRecyclerView;
    public static JournalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String primary;
    private String secondary;
    private String date;
    //    private static String journalFilePath;
    private long id;
    private Donations donations;
    public static Storage storage;
    public static String path;
    public static String imageFilePath;
    public static ImageView mPlus;
    public static TextView mNothingHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        storage = new Storage(getApplicationContext());
        path = storage.getInternalFilesDirectory();
        imageFilePath = path + "/journal_images";
        mPlus = (ImageView) findViewById(R.id.btn_plus);
        mNothingHere = (TextView) findViewById(R.id.nothing_here);
        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJournalEntry();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new JournalAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);
        AdUtil.initializeAds((AdView) findViewById(R.id.admob_adview));
        donations = new Donations(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new JournalAdapter
                .MyClickListener() {
            @Override
            public void onItemLongClick(final int position, View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                builder.setTitle(getString(R.string.dialog_title_journal));

                // add a list
                String[] options = {getString(R.string.edit), getString(R.string.delete)};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CardDataObject dataObject = null;
                        switch (which) {
                            case 0: // edit
                                dataObject = mAdapter.getmDataset().get(position);
                                if (dataObject != null) {
                                    long id = dataObject.getId();
                                    editJournalEntry(id, dataObject.getCardImage(), dataObject.getTitle(), dataObject.getNotes(), dataObject.getDate(), dataObject);
                                }
                                break;
                            case 1: // delete
                                dataObject = mAdapter.getmDataset().get(position);
                                if (dataObject != null) {
                                    long id = dataObject.getId();
                                    mAdapter.deleteItem(position);
                                    CardViewActivity.deleteCard(id);
                                }
                                break;
                        }
                    }
                });

                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private List<CardDataObject> getDataSet() {
        List<CardDataObject> results = new ArrayList<>();
        if (Configuration.instance.dataSnapshot != null && Configuration.instance.dataSnapshot.journal != null) {
            for (CardDatabaseObject databaseObject : Configuration.instance.dataSnapshot.journal) {
                results.add(new CardDataObject(databaseObject.id, databaseObject.title, databaseObject.notes, databaseObject.date));
            }
        }
        return results;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_journal_add:
                addJournalEntry();
                break;
            case R.id.action_journal_donate:
                donations.onDonationButtonClicked();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_journal_menu, menu);
        return true;
    }

    private CardDataObject dataObject;

    private void addJournalEntry() {
        bitmap = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New journal entry");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_journal_card, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText title = (EditText) viewInflated.findViewById(R.id.title);
        final EditText notes = (EditText) viewInflated.findViewById(R.id.notes);
        final TextView message = (TextView) viewInflated.findViewById(R.id.warning_message);
        imgPreview = (ImageView) viewInflated.findViewById(R.id.img_preview);
        final Button btnDate = (Button) viewInflated.findViewById(R.id.btn_date);
        final Button mOk = (Button) viewInflated.findViewById(R.id.btn_ok);
        final Button mCancel = (Button) viewInflated.findViewById(R.id.btn_cancel);
        final Calendar c = Calendar.getInstance();

        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        final String formattedDate = df.format(c.getTime());
        btnDate.setText(formattedDate);
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CardViewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year, month - 1, dayOfMonth, 0, 0);
                        btnDate.setText(df.format(c.getTime()));
                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
        final AlertDialog dialog = builder.create();
        // Set up the buttons
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().replaceAll(" ", "").length() == 0) {
                    message.setText(getString(R.string.no_empty_title));
                } else {
                    dialog.dismiss();
                    primary = title.getText().toString();
                    secondary = notes.getText().toString();
                    date = btnDate.getText().toString();
                    id = System.currentTimeMillis();
                    dataObject = new CardDataObject(0, "", "", "");
                    dataObject.setTitle(primary);
                    dataObject.setNotes(secondary);
                    dataObject.setCardImage(bitmap);
                    dataObject.setDate(date);
                    dataObject.setId(id);
                    if (fileUri != null) {
                        saveBitmap(bitmap, Long.toString(id));
                    }
                    notes.getText().toString();
                    mAdapter.addItem(dataObject, 0);
                    mRecyclerView.scrollToPosition(0);
//                    addToCsv();
                    Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).child("journal").setValue(mAdapter.getmDatasetDatabase());
                    mAdapter.uploadAll(mAdapter.getmDataset());
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        viewInflated.findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        dialog.show();
        //TODO camera implementation
    }


    private void editJournalEntry(final long id, byte[] cardImage, String title, String notes, String date, final CardDataObject dataObject) {
        bitmap = cardImage;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_journal_entry));
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_journal_card, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText mTitle = (EditText) viewInflated.findViewById(R.id.title);
        mTitle.setText(title);
        final EditText mNotes = (EditText) viewInflated.findViewById(R.id.notes);
        mNotes.setText(notes);
        imgPreview = (ImageView) viewInflated.findViewById(R.id.img_preview);
        Glide.with(this).asBitmap().load(cardImage).into(imgPreview);
        final Button btnDate = (Button) viewInflated.findViewById(R.id.btn_date);
        btnDate.setText(date);
        final Calendar c = Calendar.getInstance();

        final Button mOk = (Button) viewInflated.findViewById(R.id.btn_ok);
        final Button mCancel = (Button) viewInflated.findViewById(R.id.btn_cancel);
        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CardViewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year, month - 1, dayOfMonth, 0, 0);
                        btnDate.setText(df.format(c.getTime()));
                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
        final AlertDialog dialog = builder.create();
        // Set up the buttons
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                primary = mTitle.getText().toString();
                secondary = mNotes.getText().toString();
                CardViewActivity.this.date = btnDate.getText().toString();
                dataObject.setTitle(primary);
                dataObject.setNotes(secondary);
                dataObject.setCardImage(bitmap);
                dataObject.setDate(CardViewActivity.this.date);
                dataObject.setId(id);
                if (fileUri != null) {
                    saveBitmap(bitmap, Long.toString(id));
                }
                mAdapter.updateItems();
                Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).child("journal").setValue(mAdapter.getmDatasetDatabase());
                mAdapter.uploadAll(mAdapter.getmDataset());
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        viewInflated.findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        //TODO camera implementation
        dialog.show();
    }

    private void saveBitmap(byte[] bitmap, String name) {
        storage.createFile(imageFilePath + name, bitmap);
    }

    private ImageView imgPreview;
    private Uri fileUri;
    private byte[] bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            fileUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Glide.with(this).asBitmap().load(byteArray).into(imgPreview);
                this.bitmap = byteArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST) {

        }
    }

    public static void deleteCard(long id) {
        List<CardDataObject> dataObjectList = mAdapter.getmDataset();
        for (int i = 0; i < dataObjectList.size(); i++) {
            if (dataObjectList.get(i).getId() == id) {
                mAdapter.getmDataset().remove(i);
                break;
            }
        }
        Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).child("journal").setValue(mAdapter.getmDatasetDatabase());
    }

    public static void reload() {
        mAdapter.notifyDataSetChanged();
    }
}