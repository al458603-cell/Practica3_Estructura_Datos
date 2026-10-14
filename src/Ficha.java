public class Ficha {

    private int ladoA;
    private int ladoB;
   
    public Ficha(int ladoA, int ladoB) {

        if (ladoA < 0 || ladoA > 6 || ladoB < 0 || ladoB > 6) {

            throw new IllegalArgumentException("Los lados deben estar entre 0 y 6");
        }
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        }
       


    public int getLadoA() {
        return this.ladoA;
    }


    public int getLadoB() {
    return this.ladoB;
    }

    public int getPuntosTotales() {
    return (getLadoA() + getLadoB());

    }

    // Invierte la ficha para acomodarla a la cadena si es necesario
    public Ficha invertir() {
    int inv_a = this.getLadoB();
    int inv_b = this.getLadoA();
    Ficha invertida = new Ficha(inv_a,inv_b);
    return invertida;
        
    }

    @Override
    public String toString() {
    return "[" + getLadoA() +"|" + getLadoB() +"]";
    }
}
