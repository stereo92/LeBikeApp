package org.fablabsantiago.smartcities.app.appmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MisAlertasActivity extends AppCompatActivity implements
        MisAlertasInterfaces.MisAlertasTabListener,
        MisAlertasInterfaces.AlertaDialogListener
{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    DatabaseHandler baseDatos;
    List<Alerta> listaAlertas = new ArrayList<Alerta>();

    FragmentManager fragmentManager;
    AlertaEditDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misalertas);

        // Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initializing BaseDatos
        baseDatos = new DatabaseHandler(this);
        baseDatos.eraseAlertasTable();

        // Catch new Alerta action
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("REQUESTING_NEW_ALERTA")) {
                openAlertasEditDialog(
                        null,
                        (float) intent.getDoubleExtra("NEW_ALERTA_LATITUDE", 0),
                        (float) intent.getDoubleExtra("NEW_ALERTA_LONGITUDE", 0));
            }
        }

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Completas"));
        tabLayout.addTab(tabLayout.newTab().setText("Pendientes"));
        tabLayout.addTab(tabLayout.newTab().setText("Todas"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (listaAlertas.isEmpty()) {
            listaAlertas = baseDatos.getAlertas();

            if (listaAlertas.isEmpty()) {
                baseDatos.newAlerta(new Alerta(
                        2010, false, (float) -33.450276, (float) -70.627628,
                        "auto",
                        "12:56", "20/09/2016",
                        "Cruce de autos imprudentes",
                        "Casi salgo volando por un auto que se precipito con mi dedo chico",
                        3010, 0, "completa"));
                baseDatos.newAlerta(new Alerta(
                        2011, true, (float) -33.444446, (float) -70.628695,
                        "vias",
                        "16:44", "26/09/2016",
                        "Nueva ciclovia",
                        "esta super choriflai me encanta para venir cno mis amigos de la prepa",
                        3010, 0, "pendiente"));
                baseDatos.newAlerta(new Alerta(
                        2012, false, (float) -33.451013, (float) -70.629386,
                        "peat",
                        "13:32", "13/08/2016",
                        "Peaton qlo",
                        "Que wea se cree el zarpao se te cruza y na así nomá",
                        3010, 0, "pendiente"));
                baseDatos.newAlerta(new Alerta(
                        2013, false, (float) -33.452174, (float) -70.626191,
                        "auto",
                        "01:45", "19/08/2016",
                        "Muchas micros",
                        "Ando todo nervioso por culpa de todas las micros",
                        3010, 0, "pendiente"));
                baseDatos.newAlerta(new Alerta(
                        2014, true, (float) -33.446737, (float) -70.630335,
                        "mant",
                        "14:07", "07/09/2016",
                        "Buen taller de bicis",
                        "Justo se me pincho la rueda por acá cerca y pase a parcharla",
                        3010, 0, "completa"));
                baseDatos.newAlerta(new Alerta(
                        2015, true, (float) -33.446978, (float) -70.628538,
                        "otro",
                        "20:00", "20/09/2016",
                        "Harta vida nocturna",
                        "Esta bueno, hay artos bares y pubs con estacionamiento para bicis",
                        3010, 0, "completa"));
                baseDatos.newAlerta(new Alerta(
                        2016, true, (float) -33.446057, (float) -70.630664,
                        "vege",
                        "12:32", "14/09/2016",
                        "Rica calzada con arboles",
                        "Agradable circular por esta área",
                        3010, 0, "pendiente"));
                baseDatos.newAlerta(new Alerta(
                        2017, false, (float) -33.452373, (float) -70.628868,
                        "peat",
                        "11:26", "18/09/2016",
                        "Cruce de muchos peatones",
                        "Es algo molesto poder cruzar a veces",
                        3010, 0, "completa"));
                baseDatos.newAlerta(new Alerta(
                        2018, false, (float) -33.448125, (float) -70.630219,
                        "vege",
                        "10:40", "17/09/2016",
                        "Arbol molesto",
                        "Arbol se asoma hacía la calle y molesta el paso",
                        3010, 0, "pendiente"));
            }
        }

        // TODO: Ahora cada fragment hace acceso a la base de datos y carga las alertas
        // correspondientes de manera independiente. El siguiente paso es hacer que la actividad
        // cargue la data de la BD y la entregue a los fragments que la desplegarán en listas.
        MisAlertasPagerAdapter adapter = new MisAlertasPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    /*                                /
     *        MENUU_ITEMSS            /
     *                               */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_misalertas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_alerta_menuitem) {
            Intent mapIntent = new Intent(this, EnRutaActivity.class);
            mapIntent.setAction("NEW_ALERTA_ACTION");
            startActivity(mapIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlertasListClick(Alerta alerta) {
        openAlertasEditDialog(alerta, 0, 0);
    }

    public void openAlertasEditDialog(Alerta alerta, float lat, float lon) {
        fragmentManager = getSupportFragmentManager();
        dialog = new AlertaEditDialog();
        Bundle newAlertaExtras = new Bundle();
        if (alerta != null) {
            newAlertaExtras = alerta.toBundle();
            newAlertaExtras.putString("NEW_ALERTA_ACTION", "EDIT_ALERTA");
        } else {
            int lastAlertaId = baseDatos.getLastAlertaId();
            newAlertaExtras.putString("NEW_ALERTA_ACTION","NEW_ALERTA_FROM_MAP");
            newAlertaExtras.putInt("NEW_ALERTA_ID", lastAlertaId + 1);
            newAlertaExtras.putFloat("NEW_ALERTA_LAT", lat);
            newAlertaExtras.putFloat("NEW_ALERTA_LON", lon);
        }

        dialog.setArguments(newAlertaExtras);
        dialog.setAlertasDialogListener(this);
        dialog.show(fragmentManager, "edit_alerta_fragment");
    }


    @Override
    public void onCloseClick() {
        dialog.dismiss();
    }

    @Override
    public void onMostrarMapa() {
        Intent mapIntent = new Intent(this, EnRutaActivity.class);
        mapIntent.setAction("SEE_ALERTA_ACTION");
        startActivity(mapIntent);
    }

    @Override
    public void onAgregarAlerta(Alerta alerta, String action) {
        if (action.equals("UPDATE_ALERTA")) {
            Toast.makeText(this, "updating alerta " + alerta.getTitulo(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "creating alerta" + alerta.getTitulo(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEliminarAlerta(int alertaId) {
        if (alertaId > 0) {
            baseDatos.deleteAlerta(alertaId);
        }
        dialog.dismiss();
    }
}
