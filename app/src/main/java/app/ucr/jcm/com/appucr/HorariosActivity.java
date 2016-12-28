package app.ucr.jcm.com.appucr;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Domain.DialogoHorarios;
import Util.Constantes;

public class HorariosActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;

    // adaptadores de las listas

    private static ArrayAdapter<String> adaptadorEducacion;
    private static ArrayAdapter<String> adaptadorDeportivas;
    private static ArrayAdapter<String> adaptadorIngenieria;
    private static ArrayAdapter<String> adaptadorInvestigacion;

    // trasporte externo
    private static ArrayAdapter<String> adaptadorExterno;
    private static ArrayAdapter<String> horarioEscogidoLocal;
    private static ArrayAdapter<String> horarioEscogidoUCR;

    private static android.app.FragmentManager managerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // adaptadores de las listas, cargan los horarios en los listView correspondientes
        adaptadorEducacion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, obtenerListaInternos("educacion"));
        adaptadorDeportivas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, obtenerListaInternos("deportivas"));
        adaptadorIngenieria = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, obtenerListaInternos("ingenieria"));
        adaptadorInvestigacion = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, obtenerListaInternos("investigacion"));

        // Transporte externo
        adaptadorExterno = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Constantes.getBusesExternos());
        horarioEscogidoLocal = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        horarioEscogidoUCR  = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());

        managerFragment = getFragmentManager();

    }

    private List<String> obtenerListaInternos(String archivo) {
        List<String> lista = new ArrayList<String>();
        try {
            InputStream fraw = null;
            switch (archivo) {
                case "educacion":
                    fraw = getResources().openRawResource(R.raw.educacion);
                    break;
                case "deportivas":
                    fraw = getResources().openRawResource(R.raw.deportivas);
                    break;
                case "ingenieria":
                    fraw = getResources().openRawResource(R.raw.ingenieria);
                    break;
                case "investigacion":
                    fraw = getResources().openRawResource(R.raw.investigacion);
                    break;
            }
            if (fraw != null) {
                BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
                String linea = brin.readLine();
                lista.add(archivo.toUpperCase());
                while (linea != null) {
                    lista.add(linea);
                    linea = brin.readLine();
                }
                fraw.close();
            }
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }

        return lista;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static void cargaDeHorariosBusInterno(View root) {

        ListView horarioEducacion = (ListView) root.findViewById(R.id.horarioEducacion);
        ListView horarioInvestigacion = (ListView) root.findViewById(R.id.horarioInvestigacion);
        ListView horarioIngenieria = (ListView) root.findViewById(R.id.horarioIngenieria);
        ListView horarioDeportivas = (ListView) root.findViewById(R.id.horarioDeportivas);

        horarioEducacion.setAdapter(adaptadorEducacion);
        horarioInvestigacion.setAdapter(adaptadorInvestigacion);
        horarioIngenieria.setAdapter(adaptadorIngenieria);
        horarioDeportivas.setAdapter(adaptadorDeportivas);

    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_horarios, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_horarios_internos, container, false);
                    cargaDeHorariosBusInterno(rootView);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_horarios_externos, container, false);
                    final ListView horariosExternos = (ListView) rootView.findViewById(R.id.listViewHorariosExternos);
                    horariosExternos.setAdapter(adaptadorExterno);
                    horariosExternos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DialogoHorarios dialog = new DialogoHorarios();
                            // se pasa el adaptador antes de mostrar el dialog
                            dialog.setAdapter(horarioEscogidoLocal,horarioEscogidoUCR, adapterView.getItemAtPosition(i).toString());
                            dialog.show(managerFragment.beginTransaction(), "Dialogo");
                        }
                    });
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Administartivo";
                case 1:
                    return "Bus Interno";
                case 2:
                    return "Transporte publico";
            }
            return null;
        }
    }
}
