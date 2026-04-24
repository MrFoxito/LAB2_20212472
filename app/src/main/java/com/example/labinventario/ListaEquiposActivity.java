package com.example.labinventario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.labinventario.datos.GestorEquipos;
import com.example.labinventario.databinding.ActivityListaEquiposBinding;
import com.example.labinventario.dto.Equipo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListaEquiposActivity extends AppCompatActivity {

    ActivityListaEquiposBinding binding;
    public static String TAG = "msg-test";

    EquipoListAdapter equipoAdapter;
    List<Equipo> listaVisible = new ArrayList<>();

    int posicionSeleccionada = -1;

    ActivityResultLauncher<Intent> launcherFormulario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaEquiposBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Inventario de Equipos");

        configurarLauncher();
        configurarAdapter();
        configurarFiltros();
        configurarFab();
        configurarLongClick();

        refrescarLista();
    }

    private void configurarLauncher() {
        launcherFormulario = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "Formulario completado, refrescando lista");
                        resetearFiltros();
                        refrescarLista();
                    }
                }
        );
    }

    private void configurarAdapter() {
        equipoAdapter = new EquipoListAdapter(this, listaVisible);
        binding.listViewEquipos.setAdapter(equipoAdapter);
    }

    private void configurarFiltros() {
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.filtro_tipos_equipo, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFiltroTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(this,
                R.array.filtro_estados, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFiltroEstado.setAdapter(adapterEstado);

        AdapterView.OnItemSelectedListener filtroListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        binding.spinnerFiltroTipo.setOnItemSelectedListener(filtroListener);
        binding.spinnerFiltroEstado.setOnItemSelectedListener(filtroListener);
    }

    private void configurarFab() {
        binding.fabAgregar.setOnClickListener(view -> {
            Intent intent = new Intent(ListaEquiposActivity.this, FormEquipoActivity.class);
            launcherFormulario.launch(intent);
        });
    }

    private void configurarLongClick() {
        binding.listViewEquipos.setOnItemLongClickListener((adapterView, view, position, id) -> {
            posicionSeleccionada = position;
            startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_context_action_bar, menu);
                    mode.setTitle("Opciones");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Equipo equipoSel = listaVisible.get(posicionSeleccionada);
                    int itemId = item.getItemId();

                    if (itemId == R.id.action_editar) {
                        Log.d(TAG, "Editar: " + equipoSel.getCodigo());
                        Intent intent = new Intent(ListaEquiposActivity.this, FormEquipoActivity.class);
                        intent.putExtra("codigoEquipo", equipoSel.getCodigo());
                        launcherFormulario.launch(intent);
                        mode.finish();
                        return true;
                    } else if (itemId == R.id.action_eliminar) {
                        Log.d(TAG, "Eliminar: " + equipoSel.getCodigo());
                        confirmarEliminacion(equipoSel);
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    posicionSeleccionada = -1;
                }
            });
            return true;
        });
    }

    private void aplicarFiltros() {
        String tipo = binding.spinnerFiltroTipo.getSelectedItem().toString();
        String estado = binding.spinnerFiltroEstado.getSelectedItem().toString();

        listaVisible.clear();
        listaVisible.addAll(GestorEquipos.filtrar(tipo, estado));
        equipoAdapter.notifyDataSetChanged();

        actualizarVistaVacia();
    }

    private void refrescarLista() {
        listaVisible.clear();
        listaVisible.addAll(GestorEquipos.obtenerTodos());
        equipoAdapter.notifyDataSetChanged();

        actualizarVistaVacia();
    }

    private void actualizarVistaVacia() {
        if (listaVisible.isEmpty()) {
            binding.textViewVacio.setVisibility(View.VISIBLE);
            binding.listViewEquipos.setVisibility(View.GONE);
        } else {
            binding.textViewVacio.setVisibility(View.GONE);
            binding.listViewEquipos.setVisibility(View.VISIBLE);
        }
    }

    private void resetearFiltros() {
        binding.spinnerFiltroTipo.setSelection(0);
        binding.spinnerFiltroEstado.setSelection(0);
    }

    private void confirmarEliminacion(Equipo equipo) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de eliminar el equipo " + equipo.getCodigo()
                        + " - " + equipo.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialogInterface, i) -> {
                    GestorEquipos.eliminar(equipo.getCodigo());
                    Log.d(TAG, "Equipo eliminado: " + equipo.getCodigo());
                    Toast.makeText(this, "Equipo eliminado", Toast.LENGTH_SHORT).show();
                    resetearFiltros();
                    refrescarLista();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            Log.d(TAG, "Refresh presionado");
            resetearFiltros();
            refrescarLista();
            Toast.makeText(this, "Lista actualizada", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
