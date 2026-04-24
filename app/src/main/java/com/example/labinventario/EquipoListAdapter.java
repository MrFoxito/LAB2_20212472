package com.example.labinventario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.example.labinventario.databinding.ItemFilaEquipoBinding;
import com.example.labinventario.dto.Equipo;

import java.util.List;

public class EquipoListAdapter extends BaseAdapter {

    private final Context context;
    private List<Equipo> listaEquipos;

    public EquipoListAdapter(Context context, List<Equipo> listaEquipos) {
        this.context = context;
        this.listaEquipos = listaEquipos;
    }

    public void setListaEquipos(List<Equipo> listaEquipos) {
        this.listaEquipos = listaEquipos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listaEquipos.size();
    }

    @Override
    public Equipo getItem(int position) {
        return listaEquipos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemFilaEquipoBinding binding;

        if (convertView == null) {
            binding = ItemFilaEquipoBinding.inflate(
                    LayoutInflater.from(context), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemFilaEquipoBinding) convertView.getTag();
        }

        Equipo equipo = listaEquipos.get(position);

        binding.tvItemCodigo.setText(equipo.getCodigo());
        binding.tvItemNombre.setText(equipo.getNombre());
        binding.tvItemTipo.setText(equipo.getTipoEquipo());
        binding.tvItemEstado.setText(equipo.getEstado());

        int colorEstado = obtenerColorEstado(equipo.getEstado());
        binding.tvItemEstado.setTextColor(colorEstado);

        return convertView;
    }

    private int obtenerColorEstado(String estado) {
        switch (estado) {
            case "Operativo":
                return ContextCompat.getColor(context, R.color.estado_operativo);
            case "En mantenimiento":
                return ContextCompat.getColor(context, R.color.estado_mantenimiento);
            case "Fuera de servicio":
                return ContextCompat.getColor(context, R.color.estado_fuera_servicio);
            default:
                return ContextCompat.getColor(context, R.color.gris_texto);
        }
    }
}
