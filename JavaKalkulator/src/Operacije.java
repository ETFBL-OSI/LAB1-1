/**
 * Računske operacije kalkulatora.
 *
 * Sve operacije su izdvojene u ovu klasu, odvojeno od dijela programa
 * koji komunicira sa korisnikom. Metode samo vraćaju rezultat; jedini izuzetak
 * je {@link #podijeli(double, double)}, koja pri dijeljenju nulom ispisuje
 * poruku o grešci.
 */
public final class Operacije {

    /** Poruka koja se ispisuje kada je djelilac nula. */
    public static final String PORUKA_DIJELJENJE_NULOM = "Greska: dijeljenje nulom!";

    private Operacije() {
    }

    /** Vraća zbir {@code a + b}. */
    public static double saberi(double a, double b) {
        return a + b;
    }

    /** Vraća razliku {@code a - b}. */
    public static double oduzmi(double a, double b) {
        return a - b;
    }

    /** Vraća proizvod {@code a * b}. */
    public static double pomnozi(double a, double b) {
        return a * b;
    }

    /**
     * Vraća količnik {@code a / b}.
     *
     * Ako je djelilac nula (uključujući {@code -0.0}), ispisuje poruku
     * {@value #PORUKA_DIJELJENJE_NULOM} i vraća {@code 0}, umjesto da vrati
     * beskonačnost ili NaN.
     */
    public static double podijeli(double a, double b) {
        if (b == 0) {
            System.out.println(PORUKA_DIJELJENJE_NULOM);
            return 0;
        }
        return a / b;
    }
}
