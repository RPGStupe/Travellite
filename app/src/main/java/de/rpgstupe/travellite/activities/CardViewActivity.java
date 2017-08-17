package de.rpgstupe.travellite.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.StorageReference;
import com.opencsv.CSVParser;
import com.snatik.storage.Storage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import de.rpgstupe.travellite.DataObject;
import de.rpgstupe.travellite.Donations;
import de.rpgstupe.travellite.MyRecyclerViewAdapter;
import de.rpgstupe.travellite.R;

/**
 * Created by Fabian on 25.06.2017.
 */

public class CardViewActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;

    private static Storage storage;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private Context context;
    private String primary;
    private String secondary;
    private String date;
    private static String journalFilePath;
    private long id;
    private static String imageFilePath;
    private Donations donations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet(), this);
        mRecyclerView.setAdapter(mAdapter);
        this.context = this;
        initializeAds();
        donations = new Donations(this);
    }

    private void initializeAds() {
        AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdMobAdView.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter
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
                        DataObject dataObject = null;
                        switch (which) {
                            case 0: // edit
                                dataObject = mAdapter.getmDataset().get(position);
                                if (dataObject != null) {
                                    long id = dataObject.getId();
                                    editJournalEntry(id, dataObject.getCardImage(), dataObject.getmText1(), dataObject.getmText2(), dataObject.getDate(), dataObject);
                                }
                                break;
                            case 1: // delete
                                dataObject = mAdapter.getmDataset().get(position);
                                if (dataObject != null) {
                                    long id = dataObject.getId();
                                    CardViewActivity.deleteCard(id);
                                    mAdapter.deleteItem(position);
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

    private ArrayList<DataObject> getDataSet() {
        storage = new Storage(getApplicationContext());
        String path = storage.getInternalFilesDirectory();
        journalFilePath = path + "/Journal.csv";
        imageFilePath = path + "/journal_images";

        if (!storage.isFileExist(journalFilePath)) {
            storage.createFile(journalFilePath, "");
        }
        if (!storage.isFileExist(imageFilePath)) {
            storage.createDirectory(imageFilePath);
        }

        ArrayList results = new ArrayList<>();
        CSVParser parser = new CSVParser(',');
        String content = storage.readTextFile(journalFilePath);
        if (!"".equals(content)) {
            String[] contentArray = content.split("\n");
            for (final String item : contentArray) {
                final String[] items;
                try {
                    items = parser.parseLine(item);
                    results.add(new DataObject(items[1], items[2], items[3], loadImageFromStorage(items[0]), Long.parseLong(items[0])));
                    System.out.println("TEST");
                    StorageReference storageRef = MainActivity.storage.getReference("images/journal/" + MainActivity.mAuth.getCurrentUser().getUid());
                    storageRef.child(items[0]).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MainActivity.uploadFile(loadImageFromStorage(items[0]), items[0]);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(results);
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

    private DataObject dataObject;

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
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                    dataObject = new DataObject("", "", "", null, 0);
                    dataObject.setmText1(primary);
                    dataObject.setmText2(secondary);
                    dataObject.setCardImage(bitmap);
                    dataObject.setDate(date);
                    dataObject.setId(id);
                    if (fileUri != null) {
                        saveBitmap(bitmap, Long.toString(id));
                    }
                    //TODO
                    notes.getText().toString();
                    mAdapter.addItem(dataObject, 0);
                    mRecyclerView.scrollToPosition(0);
                    addToCsv();
                    MainActivity.prefs.edit().putInt("de.rpgstupe.travellite.countcards", MainActivity.prefs.getInt("de.rpgstupe.travellite.countcards", 0) + 1).apply();
                    MainActivity.database.getReference("users").child(MainActivity.mAuth.getCurrentUser().getUid()).child("journal").setValue(getDataSet());
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



    private void editJournalEntry(final long id, Bitmap cardImage, String title, String notes, String date, final DataObject dataObject) {
        bitmap = cardImage;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_journal_entry));
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_journal_card, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText mTitle = (EditText) viewInflated.findViewById(R.id.title);
        mTitle.setText(title);
        final EditText mNotes = (EditText) viewInflated.findViewById(R.id.notes);
        mNotes.setText(notes);
        imgPreview = (ImageView) viewInflated.findViewById(R.id.img_preview);
        imgPreview.setImageBitmap(cardImage);
        final Button btnDate = (Button) viewInflated.findViewById(R.id.btn_date);
        btnDate.setText(date);
        final Calendar c = Calendar.getInstance();

        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                primary = mTitle.getText().toString();
                secondary = mNotes.getText().toString();
                CardViewActivity.this.date = btnDate.getText().toString();
                dataObject.setmText1(primary);
                dataObject.setmText2(secondary);
                dataObject.setCardImage(bitmap);
                dataObject.setDate(CardViewActivity.this.date);
                dataObject.setId(id);
                if (fileUri != null) {
                    saveBitmap(bitmap, Long.toString(id));
                }
                editCsv(dataObject);
                mAdapter.updateItems();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        builder.show();
    }

    private void editCsv(DataObject dataObject)  {
        long id = dataObject.getId();
        String content = storage.readTextFile(journalFilePath);
        String[] lines = content.split(System.getProperty("line.separator"));
        System.out.println("ID1: " + id);
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
            if (lines[i].startsWith(Long.toString(id))) {
                System.out.println("READY TO WRITE");
                lines[i] = id + "," + dataObject.getmText1() + "," + dataObject.getmText2() + "," + dataObject.getDate();
            }
        }
        StringBuilder finalStringBuilder = new StringBuilder();
        for (String s : lines) {
            if (!s.equals("")) {
                finalStringBuilder.append(s).append(System.getProperty("line.separator"));
            }
        }
        String newContent = finalStringBuilder.toString();
        System.out.println(newContent);
        storage.createFile(journalFilePath, newContent);
    }


    private void saveBitmap(Bitmap bitmap, String name) {
        storage.createFile(imageFilePath + name, bitmap);
        MainActivity.uploadFile(bitmap, name);
    }

    private Bitmap loadImageFromStorage(String name) {
        Bitmap b = null;
        if (storage.isFileExist(imageFilePath + name)) {
            byte[] imageBytes = storage.readFile(imageFilePath + name);
            b = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return b;
    }

    private void addToCsv() {
        storage.appendFile(journalFilePath, (id + "," + primary + "," + secondary + "," + date));
    }


    private ImageView imgPreview;
    private Uri fileUri;
    private Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            fileUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                imgPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST) {

        }
    }

    public static void deleteCard(long id) {
        String content = storage.readTextFile(journalFilePath);
        String[] lines = content.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(Long.toString(id))) {
                lines[i] = "";
            }
        }
        StringBuilder finalStringBuilder = new StringBuilder();
        for (String s : lines) {
            if (!s.equals("")) {
                finalStringBuilder.append(s).append(System.getProperty("line.separator"));
            }
        }
        String newContent = finalStringBuilder.toString();
        storage.createFile(journalFilePath, newContent);
    }
}