

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Jugador {
    //parte privada
    private final String nombre;
    private Queue<Ficha> fichas;
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.fichas = new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Queue<Ficha> getFichas() {
        return fichas;
    }

    public void recibirFicha(Ficha f) {
        fichas.add(f);
    }

    public Ficha buscarFicha(int valor1, int valor2) {
        if (valor1 < 0 || valor1 > 6 || valor2 < 0 || valor2 > 6){
            return null;
        }
        if (tieneFichas()) {
            return null;
        }

        Iterator<Ficha> it = fichas.iterator();

        while (it.hasNext()){
            Ficha ficha = it.next();
            if( ficha.getLadoA() == valor1 || ficha.getLadoA() == valor2 || ficha.getLadoB() == valor1 || ficha.getLadoB() == valor2 ){
                it.remove();
                return ficha;
            }
        }
       
        return null;
    }


    public boolean tieneFichas() {
        return fichas.isEmpty();
    }

    public int calcularPuntosRestantes() {
        int suma = 0;
        Iterator<Ficha> it = fichas.iterator();
        while (it.hasNext()){
            int v_ficha = it.next().getPuntosTotales();
            suma += v_ficha;
        }
        return suma;
    }

    @Override
    public String toString() {
        String n_jugador = getNombre();
        String f_jugador = fichas.toString();

        return n_jugador + f_jugador;
    }
}
