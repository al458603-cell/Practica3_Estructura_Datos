
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class JuegoDomino {
    private final Stack<Ficha> pozo;
    private final Deque<Ficha> cadena;
    private final Jugador[] jugadores;
    private final int NJUGADORES;
    private final int NFICHAS;
    private int turno;

    public JuegoDomino (String nombresJugadores[], int nFichas) {
        if (nombresJugadores == null || nombresJugadores.length < 2 || nombresJugadores.length > 4) {
            throw new IllegalArgumentException("El numero de jugadores debe estar entre 2 y 4");
        }

        this.NJUGADORES = nombresJugadores.length;
        this.NFICHAS = nFichas;

        this.pozo = new Stack<>();
        this.cadena = new LinkedList<>();
        this.jugadores = new Jugador[NJUGADORES];

        for (int i = 0; i < NJUGADORES; i++) {
            this.jugadores[i] = new Jugador(nombresJugadores[i]);
        }
    }
        
    public JuegoDomino (String nombresJugadores[]) {
        this(nombresJugadores, 7);
    }

    public void prepararPartida () {
        crearYMezclarFichas();
        repartirFichasIniciales();
        turno=0;
    }
    public void crearYMezclarFichas() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                pozo.add(new Ficha(i, j));
            }
        }
        Collections.shuffle(pozo);
    }

    private void repartirFichasIniciales() {
        for (int i = 0; i < NJUGADORES; i++) {

            for (int k = 0; k < NFICHAS; k++) {
                Ficha f = pozo.pop();

                jugadores[i].recibirFicha(f);
            }
        }
    }

    public void jugar() {
        System.out.println("--- ESTADO INICIAL ---");
        for (int j=0; j<NJUGADORES; j++)
            System.out.println(jugadores[j]);

        System.out.println("Fichas en pozo (durmientes): " + pozo.size());
        System.out.println("----------------------\n");

        // Jugador 1 empieza colocando su primera ficha boca arriba
        Ficha primeraFicha = jugadores[turno].getFichas().poll();
        cadena.add(primeraFicha);
        System.out.println("\n--- TURNO DE " + jugadores[turno].getNombre().toUpperCase() + " ---");
        System.out.println(jugadores[turno].getNombre() + " inicia la cadena con la ficha " + primeraFicha);
        mostrarCadena();

        this.turno = (this.turno+1)%this.NJUGADORES; //pasa al siguiente jugador
        Jugador actual = this.jugadores[turno];
        int pasesConsecutivos = 0;

        boolean finJuego = false;
        while (!finJuego) {
            System.out.println("\n--- TURNO DE " + actual.getNombre().toUpperCase() + " ---");
            boolean colocoFicha = jugarTurno(actual);

            if (!actual.tieneFichas()) {
                System.out.println("\n¡" + actual.getNombre() + " se ha quedado sin fichas!");
                finJuego = true;
            }
            else {
               if (colocoFicha) {
                  pasesConsecutivos = 0;
               } else {
                   pasesConsecutivos++;
                   System.out.println(actual.getNombre() + " NO ha podido colocar ninguna ficha y pasa.");
                   if (pasesConsecutivos == 2) {
                       System.out.println("\n¡Ambos jugadores han pasado consecutivamente! El juego está cerrado.");
                       finJuego = true;
                   }
               }
            }

            mostrarCadena();

            if (!finJuego) {
                this.turno= (this.turno+1)%NJUGADORES;
                actual = this.jugadores[turno];
            }
        }
        determinarGanador();
    }

    public Jugador getJugadorActual(){
        return jugadores[turno];
    }

   
    public boolean jugarTurno(Jugador jugador) {
        int extremoIzq = cadena.getFirst().getLadoA();
        int extremoDer = cadena.getLast().getLadoB();

        Ficha ficha = jugador.buscarFicha(extremoIzq, extremoDer);
        if (ficha != null) {
            System.out.println(jugador.getNombre() + " coloca la ficha " + ficha);
            intentarColocar(ficha);
            return true;
        }

        while (!pozo.isEmpty()) {
            Ficha robada = pozo.pop();
            System.out.println(jugador.getNombre() + " roba la ficha " + robada);
            if (intentarColocar(robada)) {
                return true;
            }
            jugador.recibirFicha(robada);
        }

        return false;
    }

    public boolean intentarColocar(Ficha f) {
        int extremoIzq = cadena.getFirst().getLadoA();
        int extremoDer = cadena.getLast().getLadoB();

        boolean cabeIzq = f.getLadoA() == extremoIzq || f.getLadoB() == extremoIzq;
        boolean cabeDer = f.getLadoA() == extremoDer || f.getLadoB() == extremoDer;

        if (cabeIzq && cabeDer) {

            if (extremoIzq <= extremoDer) {
                colocarEnIzquierda(f, extremoIzq);
            }
            else {
                colocarEnDerecha(f, extremoDer);
            }
            return true;
        }
        if (cabeIzq) {
            colocarEnIzquierda(f, extremoIzq);
            return true;
        }
        if (cabeDer) {
            colocarEnDerecha(f, extremoDer);
            return true;
        }
        return false;
    }

    public void colocarEnIzquierda(Ficha f, int valorExtremo) {
        if (f.getLadoA() == valorExtremo) {
            f = f.invertir();
            cadena.addFirst(f);
        }
        else if (f.getLadoB() == valorExtremo) {
            cadena.addFirst(f);
        }
    }

    public void colocarEnDerecha(Ficha f, int valorExtremo) {
        if (f.getLadoA() == valorExtremo) {
            cadena.addLast(f);
        }
        else if (f.getLadoB() == valorExtremo) {
            f = f.invertir();
            cadena.addLast(f);
        }
    }

    public void mostrarCadena() {
        StringBuilder sb = new StringBuilder("CADENA ACTUAL: ");
        for (Ficha f : cadena) {
            sb.append(f.toString()).append(" ");
        }
        System.out.println(sb.toString());
    }

    public Jugador determinarGanador() {
        Jugador ganador = null;
        boolean empate = false;

        for (Jugador j : jugadores) {
            int puntos = j.calcularPuntosRestantes();
            System.out.println("Puntos restantes de " + j.getNombre() + ": " + puntos);

            if (ganador == null) {
                ganador = j;
                continue;
            }

            int puntosGanador = ganador.calcularPuntosRestantes();
            int fichas = j.getFichas().size();
            int fichasGanador = ganador.getFichas().size();

            if (puntos < puntosGanador || (puntos == puntosGanador && fichas < fichasGanador)) {
                ganador = j;
                empate = false;
            } else if (puntos == puntosGanador && fichas == fichasGanador) {
                empate = true;
            }
        }

        if (empate) {
            System.out.println("Empate: no hay ganador");
            return null;
        }
        System.out.println("Gana " + ganador.getNombre());
        return ganador;
    }

    public Jugador[] getJugadores() {
        return this.jugadores;
    }
    public Stack<Ficha> getPozo() {
        return this.pozo;
    }
    public int fichasEnPozo() {
        return this.pozo.size();
    }
    public Object[] getCadena() {
        return this.cadena.toArray();
    }
}
