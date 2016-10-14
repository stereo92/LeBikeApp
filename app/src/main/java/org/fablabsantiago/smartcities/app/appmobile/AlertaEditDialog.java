package org.fablabsantiago.smartcities.app.appmobile;


import android.media.Image;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.fablabsantiago.smartcities.app.appmobile.MisAlertasInterfaces.AlertaDialogListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AlertaEditDialog extends DialogFragment
{
    Alerta alerta;
    Bundle alertaInfo;

    TextView fecha;
    TextView hora;
    ImageView positiva;
    ImageView negativa;
    EditText titulo;
    EditText descripcion;
    ImageView ciclovia;
    ImageView vias;
    ImageView espacios;
    ImageView mantencion;
    ImageView automoviles;
    ImageView otros;
    ImageView peaton;
    List<ImageView> tiposAlerta;


    private AlertaDialogListener alertaDialogListener;

    public AlertaEditDialog () {
    }

    public AlertaEditDialog newInstance(Alerta alerta) {
        AlertaEditDialog alertaEditDialog = new AlertaEditDialog();
        Bundle bundle = alerta.toBundle();
        alertaEditDialog.setArguments(bundle);
        return alertaEditDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alertaInfo = getArguments();
        if (alertaInfo != null) {
            String action = alertaInfo.getString("NEW_ALERTA_ACTION");
            if (action.equals("EDIT_ALERTA")) {
                alertaInfo.remove("NEW_ALERTA_ACTION");
                alerta = new Alerta(alertaInfo);
            } else if (action.equals("NEW_ALERTA_FROM_MAP")){
                alerta = null;
            }
        }

        return inflater.inflate(R.layout.dialog_edit_alerta, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cerrar = (Button) view.findViewById(R.id.edit_alerta_dialog_close_button);
        fecha = (TextView) view.findViewById(R.id.edit_alerta_dialog_fecha);
        hora = (TextView) view.findViewById(R.id.edit_alerta_dialog_hora);
        positiva = (ImageView) view.findViewById(R.id.edit_alerta_dialog_alerta_positiva);
        negativa = (ImageView) view.findViewById(R.id.edit_alerta_dialog_alerta_negativa);
        titulo = (EditText) view.findViewById(R.id.edit_alerta_dialog_titulo);
        descripcion = (EditText) view.findViewById(R.id.edit_alerta_dialog_descripcion);
        ciclovia = (ImageView) view.findViewById(R.id.edit_alerta_dialog_ciclovias);
        vias = (ImageView) view.findViewById(R.id.edit_alerta_dialog_vias);
        espacios = (ImageView) view.findViewById(R.id.edit_alerta_dialog_espacios);
        mantencion = (ImageView) view.findViewById(R.id.edit_alerta_dialog_mantencion);
        automoviles = (ImageView) view.findViewById(R.id.edit_alerta_dialog_automoviles);
        otros = (ImageView) view.findViewById(R.id.edit_alerta_dialog_otros);
        peaton = (ImageView) view.findViewById(R.id.edit_alerta_dialog_peaton);
        LinearLayout mostrarMapa = (LinearLayout) view.findViewById(R.id.edit_alerta_dialog_mostrar_napa);
        ImageView camara = (ImageView) view.findViewById(R.id.edit_alerta_dialog_camera);
        TextView agregar = (TextView) view.findViewById(R.id.edit_alerta_dialog_agregar);
        TextView eliminar = (TextView) view.findViewById(R.id.edit_alerta_dialog_eliminar);

        tiposAlerta = new ArrayList<ImageView>(
                Arrays.asList(
                        ciclovia,
                        vias,
                        espacios,
                        mantencion,
                        automoviles,
                        otros,
                        peaton));

        if (alerta != null) {
            fecha.setText(alerta.getFecha());
            hora.setText(alerta.getHora());
            if (alerta.getPosNeg()) {
                positiva.setAlpha((float) 1.0);
            } else {
                negativa.setAlpha((float) 1.0);
            }
            titulo.setText(alerta.getTitulo());
            descripcion.setText(alerta.getDescrption());
            switch (alerta.getTipoAlerta()) {
                case "cicl": ciclovia.setAlpha((float) 1.0);
                             break;
                case "vias": vias.setAlpha((float) 1.0);
                             break;
                case "vege": espacios.setAlpha((float) 1.0);
                             break;
                case "mant": mantencion.setAlpha((float) 1.0);
                             break;
                case "auto": automoviles.setAlpha((float) 1.0);
                             break;
                case "peat": peaton.setAlpha((float) 1.0);
                             break;
                case "otro": otros.setAlpha((float) 1.0);
                             break;
                default:
                    break;
            }
        } else  {
            DateFormat date = new SimpleDateFormat("dd/MM/yy");
            DateFormat hour = new SimpleDateFormat("HH:mm");
            String dateText =  date.format(new Date());
            String hourText = hour.format(new Date());
            fecha.setText(dateText);
            hora.setText(hourText);

            mostrarMapa.setEnabled(false);
            mostrarMapa.setAlpha((float) 0.3);
        }


        positiva.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                positiva.setAlpha((float) 1.0);
                negativa.setAlpha((float) 0.4);
            }
        });
        negativa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                positiva.setAlpha((float) 0.4);
                negativa.setAlpha((float) 1.0);
            }
        });

        View.OnClickListener tipoAlertaOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                for (ImageView vista : tiposAlerta) {
                    vista.setAlpha((float) 0.4);
                }
                v.setAlpha((float) 1.0);
            }
        };
        for (ImageView tipo : tiposAlerta) {
            tipo.setOnClickListener(tipoAlertaOnClickListener);
        }


        cerrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alertaDialogListener.onCloseClick();
            }
        });

        mostrarMapa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                alertaDialogListener.onMostrarMapa();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int id;
                if (alerta != null) {
                    id = alerta.getId();
                } else {
                    id = -1;
                }
                alertaDialogListener.onEliminarAlerta(id);
            }
        });

        agregar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String fecha2 = fecha.getText().toString();
                String hora2 = hora.getText().toString();
                boolean posneg = (positiva.getAlpha() == (float) 1.0);
                String titulo2 = titulo.getText().toString();
                String descripcion2 = descripcion.getText().toString();

                String tipo2 = "";
                ImageView tipoSelec;
                for (ImageView tipo : tiposAlerta) {
                    if (tipo.getAlpha() == (float) 1.0) {
                        switch (tipo.getId()) {
                            case R.id.edit_alerta_dialog_ciclovias:
                                tipo2 = "cicl";
                                break;
                            case R.id.edit_alerta_dialog_vias:
                                tipo2 = "vias";
                                break;
                            case R.id.edit_alerta_dialog_mantencion:
                                tipo2 = "mant";
                                break;
                            case R.id.edit_alerta_dialog_automoviles:
                                tipo2 = "auto";
                                break;
                            case R.id.edit_alerta_dialog_peaton:
                                tipo2 = "peat";
                                break;
                            case R.id.edit_alerta_dialog_otros:
                                tipo2 = "otro";
                                break;
                            default:
                                tipo2 = "";
                                break;
                        }
                    }
                }


                String action;
                Alerta newAlerta;
                if (alerta != null) {
                    newAlerta = new Alerta(
                            alerta.getId(),
                            posneg,
                            (float) alerta.getLatLng().latitude,
                            (float) alerta.getLatLng().longitude,
                            tipo2,
                            hora2,
                            fecha2,
                            titulo2,
                            descripcion2,
                            -1,
                            alerta.getVersion() + 1,
                            "pendiente"
                            );
                    newAlerta.setEstado((newAlerta.isComplete()) ? "completa":"pendiente");
                    action = "UPDATE_ALERTA";
                } else {
                    newAlerta = new Alerta(
                            alertaInfo.getInt("NEW_ALERTA_ID"),
                            posneg,
                            alertaInfo.getFloat("NEW_ALERTA_LAT"),
                            alertaInfo.getFloat("NEW_ALERTA_LON"),
                            tipo2,
                            hora2,
                            fecha2,
                            titulo2,
                            descripcion2,
                            -1,
                            1,
                            "pendiente"
                    );
                    newAlerta.setEstado((newAlerta.isComplete()) ? "completa" : "pendiente");
                    action = "NEW_ALERTA";
                }

                alertaDialogListener.onAgregarAlerta(newAlerta, action);
            }
        });
    }


    public void setAlertasDialogListener(AlertaDialogListener alertaDialogListener) {
        this.alertaDialogListener = alertaDialogListener;
    }
}
