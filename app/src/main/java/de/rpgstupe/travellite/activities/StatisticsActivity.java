package de.rpgstupe.travellite.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.rpgstupe.travellite.Configuration;
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
        initializeContinent((PieChartView) findViewById(R.id.chartAfrica), Configuration.instance.countryCodesListAfrica, getResources().getString(R.string.africa));
        initializeContinent((PieChartView) findViewById(R.id.chartAsia), Configuration.instance.countryCodesListAsia, getResources().getString(R.string.asia));
        initializeContinent((PieChartView) findViewById(R.id.chartAustralia), Configuration.instance.countryCodesListAustralia, getResources().getString(R.string.australia));
        initializeContinent((PieChartView) findViewById(R.id.chartEurope), Configuration.instance.countryCodesListEurope, getResources().getString(R.string.europe));
        initializeContinent((PieChartView) findViewById(R.id.chartLatinamerica), Configuration.instance.countryCodesListLatinAmerica, getResources().getString(R.string.latinamerica));
        initializeContinent((PieChartView) findViewById(R.id.chartNA), Configuration.instance.countryCodesListNA, getResources().getString(R.string.northamerica));
    }
    private void initializeContinent(PieChartView pieChartView, String[] countryCodes, String title) {
        List<String> countryCodesList = new ArrayList<>(Arrays.asList(countryCodes));
        int counter = 0;
        for (String countryCodeSelected : Configuration.instance.selectedCountryCodesList) {
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
        if ((double) counter / countryCodesList.size() * 100 == 0) {
            data.setCenterText2("0%");
        } else {
            data.setCenterText2(df2.format((double) counter / countryCodesList.size() * 100) + "%");
        }
        data.setCenterText2Color(Color.GRAY);
        pieChartView.setPieChartData(data);
    }

    private void initializeWorld() {
        PieChartView pieChartView = (PieChartView) findViewById(R.id.chartWorld);
        pieChartView.setMinimumHeight(getResources().getDisplayMetrics().widthPixels);
        DecimalFormat df2 = new DecimalFormat(".##");
        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue(Configuration.instance.selectedCountryCodesList.size(), Color.argb(255, 230, 130, 9)));
        values.add(new SliceValue(Configuration.instance.countryCodesList.length - Configuration.instance.selectedCountryCodesList.size(), Color.parseColor("#DBDCDE")));
        PieChartData data = new PieChartData(values);
        data.setHasCenterCircle(true);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(true);
        pieChartView.setChartRotationEnabled(false);
        data.setCenterText1(getResources().getString(R.string.world));
        data.setCenterText1Color(Color.GRAY);
        if ((double) Configuration.instance.selectedCountryCodesList.size() / Configuration.instance.countryCodesList.length * 100 == 0) {
            data.setCenterText2("0%");
        } else {
            data.setCenterText2(df2.format((double) Configuration.instance.selectedCountryCodesList.size() / Configuration.instance.countryCodesList.length * 100) + "%");
        }
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
