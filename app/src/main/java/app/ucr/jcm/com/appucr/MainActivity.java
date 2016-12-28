package app.ucr.jcm.com.appucr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import Util.Constantes;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Busca aulas, edificios, etc");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav:
                break;
            case R.id.nav_gallery:
                startActivity(new Intent(this, HorariosActivity.class));
                break;
            case R.id.nav_slideshow:
                startActivity(new Intent(this, SodasActivity.class));
                break;
            case R.id.nav_manage:
                startActivity(new Intent(this, EventosActivity.class));
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    GoogleMap.OnCameraChangeListener camaraMapa = new GoogleMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            if (mMap.getCameraPosition().zoom < (Constantes.ZOOM_PREDEFINIDO - 0.5)) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constantes.UCR_LOCATION, Constantes.ZOOM_PREDEFINIDO));
            } else// si el zoom se aumenta demasiado devuleve al usuario a la posicion de la U
                if (mMap.getCameraPosition().target.latitude > 9.949071 || mMap.getCameraPosition().target.latitude < 9.930416) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constantes.UCR_LOCATION, mMap.getCameraPosition().zoom));
                } else// si se aleja mucho al norte o sur devulve a la posicion
                    if (mMap.getCameraPosition().target.longitude > -84.036156 || mMap.getCameraPosition().target.longitude < -84.055587) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constantes.UCR_LOCATION, mMap.getCameraPosition().zoom));
                    }// si se aleja mucho al este u oeste devuelve a la posicion
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // animacion zoom ubicacion y altura de la camara
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constantes.UCR_LOCATION, Constantes.ZOOM_PREDEFINIDO));
        // configuraciones del mapa
        mMap.getUiSettings().setCompassEnabled(true);// brujula

        //verificacion de permisos de usuario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);//boton de localizacion

            }//permisos Consedidos
            else
                Toast.makeText(MainActivity.this, "Active sus permisos de GPS para acceder a la ubicacion", Toast.LENGTH_LONG).show();
        }

        mMap.setOnCameraChangeListener(camaraMapa); // listener de la camara del mapa

        // Parte interna de los edificios
      //  LatLng NEWARK = new LatLng(9.936919, -84.050878);// localizacion
       // GroundOverlayOptions newarkMap = new GroundOverlayOptions();
       // newarkMap.image(BitmapDescriptorFactory.fromResource(R.drawable.facio));// imagen
       // newarkMap.position(NEWARK, 670f, 489f);// tamanio del edificio
        //mMap.addGroundOverlay(newarkMap);

        // Get back the mutable Polyline
        PolygonOptions facio = new PolygonOptions();

        facio.add(new LatLng(10.270759, -83.637945));
        facio.add(new LatLng(9.795573, -83.631602));
        facio.add(new LatLng(9.647566, -84.447960));
        facio.add(new LatLng(10.206692, -84.507805));
        facio.add(new LatLng(10.270759, -83.637945));

        facio.fillColor(Color.parseColor("#1e000000"));
        facio.strokeColor(Color.parseColor("#55000000"));
        facio.strokeWidth(5);

        facio.addHole(Constantes.DELIMITACIONES_RODRIGO_FACIO);

        mMap.addPolygon(facio);



    }

    private void agregarParadas() {
        mMap.addMarker(new MarkerOptions().position(Constantes.ALAJUELA).title("Buses de Alajuela"));
        mMap.addMarker(new MarkerOptions().position(Constantes.ALAJUELITA).title("Buses de Alajuelita"));
        mMap.addMarker(new MarkerOptions().position(Constantes.CALLE_BLANCOS).title("Buses de Calle Blancos"));
        mMap.addMarker(new MarkerOptions().position(Constantes.CARTAGO).title("Buses de Cartago"));
        mMap.addMarker(new MarkerOptions().position(Constantes.CORONADO).title("Buses de Coronado"));
        mMap.addMarker(new MarkerOptions().position(Constantes.DESAMPARADOS_ASERRI).title("Buses de Desamparados y Aserri"));
        mMap.addMarker(new MarkerOptions().position(Constantes.EL_CARMEN).title("Buses de El Carmen"));
        mMap.addMarker(new MarkerOptions().position(Constantes.GRECIA_ACOSTA).title("Buses de Gracia y Acosta"));
        mMap.addMarker(new MarkerOptions().position(Constantes.HEREDIA).title("Buses de Heredia"));
        mMap.addMarker(new MarkerOptions().position(Constantes.MORAVIA).title("Buses de Moravia"));
        mMap.addMarker(new MarkerOptions().position(Constantes.PARACITO_HEREDIA).title("Buses de Paracito de Heredia"));
        mMap.addMarker(new MarkerOptions().position(Constantes.PAVAS).title("Buses de Pavas"));
        mMap.addMarker(new MarkerOptions().position(Constantes.PERIFERICA_CARMIOL).title("Buses de la Perefiferica y Carmiol"));
        mMap.addMarker(new MarkerOptions().position(Constantes.SAN_CARLOS).title("Buses de San Carlos"));
        mMap.addMarker(new MarkerOptions().position(Constantes.SAN_CAYETANO).title("Buses de San Cayetano"));
        mMap.addMarker(new MarkerOptions().position(Constantes.SAN_RAFEAL_ABAJO_SAN_JUAN_DE_DIOS).title("Buses de San Rafel Abajo y San Juan De Dios"));
        mMap.addMarker(new MarkerOptions().position(Constantes.SAN_RAMON).title("Buses de San Ramon"));
        mMap.addMarker(new MarkerOptions().position(Constantes.SANTA_ANA_ESCAZU).title("Buses de Santa Ana y Escazu"));
        mMap.addMarker(new MarkerOptions().position(Constantes.TIBAS).title("Buses de Tibas"));

    }


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.ucr.jcm.com.appucr/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.ucr.jcm.com.appucr/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


