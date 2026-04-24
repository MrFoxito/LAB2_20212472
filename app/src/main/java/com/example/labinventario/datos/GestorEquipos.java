package com.example.labinventario.datos;

import com.example.labinventario.dto.Equipo;

import java.util.ArrayList;
import java.util.List;

public class GestorEquipos {

    private static final ArrayList<Equipo> inventario = new ArrayList<>();

    private GestorEquipos() {
    }

    public static List<Equipo> obtenerTodos() {
        return new ArrayList<>(inventario);
    }

    public static boolean agregar(Equipo equipo) {
        for (Equipo e : inventario) {
            if (e.getCodigo().equalsIgnoreCase(equipo.getCodigo())) {
                return false;
            }
        }
        inventario.add(equipo);
        return true;
    }

    public static void actualizar(String codigoOriginal, Equipo equipoNuevo) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getCodigo().equalsIgnoreCase(codigoOriginal)) {
                inventario.set(i, equipoNuevo);
                return;
            }
        }
    }

    public static void eliminar(String codigo) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getCodigo().equalsIgnoreCase(codigo)) {
                inventario.remove(i);
                return;
            }
        }
    }

    public static Equipo buscarPorCodigo(String codigo) {
        for (Equipo e : inventario) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    public static List<Equipo> filtrar(String tipo, String estado) {
        ArrayList<Equipo> resultado = new ArrayList<>();
        for (Equipo e : inventario) {
            boolean pasaTipo = tipo.equals("Todos") || e.getTipoEquipo().equalsIgnoreCase(tipo);
            boolean pasaEstado = estado.equals("Todos") || e.getEstado().equalsIgnoreCase(estado);
            if (pasaTipo && pasaEstado) {
                resultado.add(e);
            }
        }
        return resultado;
    }
}
