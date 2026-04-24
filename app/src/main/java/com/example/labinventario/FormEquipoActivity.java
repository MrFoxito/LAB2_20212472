package com.example.labinventario;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labinventario.datos.GestorEquipos;
import com.example.labinventario.databinding.ActivityFormEquipoBinding;
import com.example.labinventario.dto.Equipo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FormEquipoActivity extends AppCompatActivity {

    ActivityFormEquipoBinding binding;
    public static String TAG = "msg-test";

    boolean modoEdicion = false;
    Equipo equipoOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapterTipos = ArrayAdapter.createFromResource(this,
                R.array.tipos_equipo, android.R.layout.simple_spinner_item);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoEquipo.setAdapter(adapterTipos);

        String codigoRecibido = getIntent().getStringExtra("codigoEquipo");

        if (codigoRecibido != null) {
            modoEdicion = true;
            equipoOriginal = GestorEquipos.buscarPorCodigo(codigoRecibido);

            if (equipoOriginal == null) {
                Toast.makeText(this, "Equipo no encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            getSupportActionBar().setTitle("Editar Equipo");
            cargarDatosEnFormulario(equipoOriginal);
            binding.editTextCodigo.setEnabled(false);
            binding.spinnerTipoEquipo.setEnabled(false);
        } else {
            getSupportActionBar().setTitle("Nuevo Equipo");
        }

        binding.buttonGuardar.setOnClickListener(view -> {
            if (validarFormulario()) {
                confirmarGuardado();
            }
        });
    }

    private void cargarDatosEnFormulario(Equipo equipo) {
        binding.editTextCodigo.setText(equipo.getCodigo());
        binding.editTextNombreEquipo.setText(equipo.getNombre());
        binding.editTextObservaciones.setText(equipo.getObservaciones());

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) binding.spinnerTipoEquipo.getAdapter();
        int pos = adapter.getPosition(equipo.getTipoEquipo());
        binding.spinnerTipoEquipo.setSelection(pos);

        switch (equipo.getEstado()) {
            case "Operativo":
                binding.radioOperativo.setChecked(true);
                break;
            case "En mantenimiento":
                binding.radioMantenimiento.setChecked(true);
                break;
            case "Fuera de servicio":
                binding.radioFueraServicio.setChecked(true);
                break;
        }
    }

    private boolean validarFormulario() {
        boolean valido = true;

        String codigo = binding.editTextCodigo.getText().toString().trim();
        String nombre = binding.editTextNombreEquipo.getText().toString().trim();

        if (codigo.isEmpty()) {
            binding.editTextCodigo.setError("El código es obligatorio");
            valido = false;
        }

        if (nombre.isEmpty()) {
            binding.editTextNombreEquipo.setError("El nombre es obligatorio");
            valido = false;
        }

        if (binding.radioGroupEstado.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Debe seleccionar un estado", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;
    }

    private void confirmarGuardado() {
        String titulo = modoEdicion ? "¿Desea actualizar este equipo?" : "¿Desea registrar este equipo?";

        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar")
                .setMessage(titulo)
                .setPositiveButton("Sí", (dialogInterface, i) -> guardarEquipo())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarEquipo() {
        String codigo = binding.editTextCodigo.getText().toString().trim();
        String nombre = binding.editTextNombreEquipo.getText().toString().trim();
        String tipoEquipo = binding.spinnerTipoEquipo.getSelectedItem().toString();
        String observaciones = binding.editTextObservaciones.getText().toString().trim();
        String estado = obtenerEstadoSeleccionado();

        Equipo equipo = new Equipo(codigo, nombre, tipoEquipo, estado, observaciones);

        if (modoEdicion) {
            GestorEquipos.actualizar(equipoOriginal.getCodigo(), equipo);
            Toast.makeText(this, "Equipo actualizado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Equipo actualizado: " + codigo);
        } else {
            boolean agregado = GestorEquipos.agregar(equipo);
            if (!agregado) {
                Toast.makeText(this, "Ya existe un equipo con ese código", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Equipo registrado", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Equipo registrado: " + codigo);
        }

        setResult(RESULT_OK);
        finish();
    }

    private String obtenerEstadoSeleccionado() {
        int radioId = binding.radioGroupEstado.getCheckedRadioButtonId();
        if (radioId == R.id.radioOperativo) {
            return "Operativo";
        } else if (radioId == R.id.radioMantenimiento) {
            return "En mantenimiento";
        }
        return "Fuera de servicio";
    }
}
