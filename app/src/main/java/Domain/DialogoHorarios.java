package Domain;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.ucr.jcm.com.appucr.HorariosActivity;
import app.ucr.jcm.com.appucr.R;

/**
 * Created by JuanCarlos on 15/08/2016.
 */

public class DialogoHorarios extends DialogFragment {


    private ListView horarioAcosta;
    private ListView horarioUCR;
    private String lugar;
    ArrayAdapter<String> adaptadorLocal;
    ArrayAdapter<String> adaptadorUCR;
    private List<ArrayList<String>> listas;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_horario, null);
        builder.setView(vista)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        horarioAcosta = (ListView) vista.findViewById(R.id.listSalidaLugar);
        horarioUCR = (ListView) vista.findViewById(R.id.listSalidaU);

        TextView tv_Lugar = (TextView) vista.findViewById(R.id.info_text);
        TextView tv_LugarTexto = (TextView) vista.findViewById(R.id.textView3);

        tv_Lugar.setText("Informacion de Horario de " + lugar);
        tv_LugarTexto.setText("Salida " + lugar);

        horarioAcosta.setBackgroundColor(Color.parseColor("#FFC2C2C2"));
        horarioUCR.setBackgroundColor(Color.parseColor("#FFC2C2C2"));

        this.listas = obtenerListaExternos(this.lugar);

        adaptadorLocal.clear();
        adaptadorUCR.clear();

        adaptadorLocal.addAll(listas.get(0));
        horarioAcosta.setAdapter(adaptadorLocal);

        adaptadorUCR.addAll(listas.get(1));
        horarioUCR.setAdapter(adaptadorUCR);


        return builder.create();
    }

    public void setAdapter(ArrayAdapter<String> adapterLocal, ArrayAdapter<String> adapterUCR, String lugar) {
        adaptadorLocal = adapterLocal;
        adaptadorUCR = adapterUCR;
        this.lugar = lugar;
    }


    private List<ArrayList<String>> obtenerListaExternos(String archivo) {

        List<ArrayList<String>> listas = new ArrayList<>();

        listas.add(new ArrayList<String>());
        listas.add(new ArrayList<String>());

        try {
            InputStream fraw = null;
            switch (archivo) {
                case "Acosta":
                    fraw = getResources().openRawResource(R.raw.acosta);
                    break;
                case "Alajuela":
                    fraw = getResources().openRawResource(R.raw.alajuela);
                    break;
                case "Alajuelita":
                    fraw = getResources().openRawResource(R.raw.alajuelita);
                    break;
                case "Cartago":
                    fraw = getResources().openRawResource(R.raw.cartago);
                    break;
                case "Coronado":
                    fraw = getResources().openRawResource(R.raw.coronado);
                    break;
                case "Desamparados-Aserri":
                    fraw = getResources().openRawResource(R.raw.desamparadosaserri);
                    break;
                case "Grecia":
                    fraw = getResources().openRawResource(R.raw.grecia);
                    break;
                case "Guadalupe-Ipis":
                    fraw = getResources().openRawResource(R.raw.gudalupe);
                    break;
                case "Heredia":
                    fraw = getResources().openRawResource(R.raw.heredia);
                    break;
            }

            if (fraw != null) {
                BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
                boolean ucr = false;

                String linea = brin.readLine();
                while (linea != null) {
                    if (linea.equals("UCR")) {
                        ucr = true;
                    } else if (ucr) {
                        listas.get(1).add(linea);
                    } else {
                        listas.get(0).add(linea);
                    }

                    linea = brin.readLine();
                }
                fraw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listas;
    }

}
