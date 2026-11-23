/**
 * Računske operacije kalkulatora.
 *
 * Sve operacije su izdvojene u ovu klasu, odvojeno od dijela programa
 * koji komunicira sa korisnikom. Metode ništa ne ispisuju, samo vraćaju rezultat.
 */
public final class Operacije {

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
}
