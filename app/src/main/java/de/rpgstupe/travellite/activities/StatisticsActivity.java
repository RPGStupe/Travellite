package de.rpgstupe.travellite.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.rpgstupe.travellite.R;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Fabian on 31.07.2017.
 */

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initializeWorld();
        initializeContinent((PieChartView) findViewById(R.id.chartAfrica), MainActivity.countryCodesListAfrica, getResources().getString(R.string.africa));
        initializeContinent((PieChartView) findViewById(R.id.chartAsia), MainActivity.countryCodesListAsia, getResources().getString(R.string.asia));
        initializeContinent((PieChartView) findViewById(R.id.chartAustralia), MainActivity.countryCodesListAustralia, getResources().getString(R.string.australia));
        initializeContinent((PieChartView) findViewById(R.id.chartEurope), MainActivity.countryCodesListEurope, getResources().getString(R.string.europe));
        initializeContinent((PieChartView) findViewById(R.id.chartLatinamerica), MainActivity.countryCodesListLatinAmerica, getResources().getString(R.string.latinamerica));
        initializeContinent((PieChartView) findViewById(R.id.chartNA), MainActivity.countryCodesListNA, getResources().getString(R.string.northamerica));
    }
    private void initializeContinent(PieChartView pieChartView, String[] countryCodes, String title) {
        List<String> countryCodesList = new ArrayList<>(Arrays.asList(countryCodes));
        int counter = 0;
        for (String countryCodeSelected : MainActivity.selectedCountryCodesList) {
            if (countryCodesList.contains(countryCodeSelected)) {
                counter++;
            }
        }
        pieChartView.setMinimumHeight(getResources().getDisplayMetrics().widthPixels);
        DecimalFormat df2 = new DecimalFormat(".##");
        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue(counter, Color.argb(255, 230, 130, 9)));
        values.add(new SliceValue(countryCodesList.size() - counter, Color.parseColor("#DBDCDE")));
        PieChartData data = new PieChartData(values);
        data.setHasCenterCircle(true);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(true);
        pieChartView.setChartRotationEnabled(false);
        data.setCenterText1(title);
        data.setCenterText1Color(Color.GRAY);
        data.setCenterText2(df2.format((double) counter / countryCodesList.size() * 100) + "%");
        data.setCenterText2Color(Color.GRAY);
        pieChartView.setPieChartData(data);
    }

    private void initializeWorld() {
        PieChartView pieChartView = (PieChartView) findViewById(R.id.chartWorld);
        pieChartView.setMinimumHeight(getResources().getDisplayMetrics().widthPixels);
        DecimalFormat df2 = new DecimalFormat(".##");
        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue(MainActivity.selectedCountries, Color.argb(255, 230, 130, 9)));
        values.add(new SliceValue(MainActivity.allCountries - MainActivity.selectedCountries, Color.parseColor("#DBDCDE")));
        PieChartData data = new PieChartData(values);
        data.setHasCenterCircle(true);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(true);
        pieChartView.setChartRotationEnabled(false);
        data.setCenterText1(getResources().getString(R.string.world));
        data.setCenterText1Color(Color.GRAY);
        data.setCenterText2(df2.format((double) MainActivity.selectedCountries / MainActivity.allCountries * 100) + "%");
        data.setCenterText2Color(Color.GRAY);
        pieChartView.setPieChartData(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
