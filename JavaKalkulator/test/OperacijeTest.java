import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OperacijeTest {

    private static final double DELTA = 1e-9;

    // --- Kriterijumi prihvatanja iz issue-a #4 ---

    @Test
    void saberiDvaITriVracaPet() {
        assertEquals(5.0, Operacije.saberi(2, 3), DELTA);
    }

    @Test
    void oduzmiDvaITriVracaMinusJedan() {
        assertEquals(-1.0, Operacije.oduzmi(2, 3), DELTA);
    }

    @Test
    void pomnoziDvaIPoISaCetiriVracaDeset() {
        assertEquals(10.0, Operacije.pomnozi(2.5, 4), DELTA);
    }

    // --- Sabiranje ---

    @Test
    void saberiNegativneBrojeve() {
        assertEquals(-7.5, Operacije.saberi(-3.5, -4), DELTA);
    }

    @Test
    void saberiSaNulomVracaIstiBroj() {
        assertEquals(42.0, Operacije.saberi(42, 0), DELTA);
        assertEquals(42.0, Operacije.saberi(0, 42), DELTA);
    }

    @Test
    void saberiJeKomutativno() {
        assertEquals(Operacije.saberi(1.25, -8), Operacije.saberi(-8, 1.25), DELTA);
    }

    @Test
    void saberiRealneBrojeve() {
        assertEquals(0.3, Operacije.saberi(0.1, 0.2), DELTA);
    }

    // --- Oduzimanje ---

    @Test
    void oduzmiIstiBrojVracaNulu() {
        assertEquals(0.0, Operacije.oduzmi(7.7, 7.7), DELTA);
    }

    @Test
    void oduzmiNegativanBroj() {
        assertEquals(8.0, Operacije.oduzmi(5, -3), DELTA);
    }

    @Test
    void oduzmiNijeKomutativno() {
        assertEquals(1.0, Operacije.oduzmi(3, 2), DELTA);
        assertEquals(-1.0, Operacije.oduzmi(2, 3), DELTA);
    }

    // --- Množenje ---

    @Test
    void pomnoziSaNulomVracaNulu() {
        assertEquals(0.0, Operacije.pomnozi(123.456, 0), DELTA);
    }

    @Test
    void pomnoziSaJedanVracaIstiBroj() {
        assertEquals(-9.5, Operacije.pomnozi(-9.5, 1), DELTA);
    }

    @Test
    void pomnoziDvaNegativnaDajePozitivan() {
        assertEquals(12.0, Operacije.pomnozi(-3, -4), DELTA);
    }

    @Test
    void pomnoziNegativanIPozitivanDajeNegativan() {
        assertEquals(-12.0, Operacije.pomnozi(-3, 4), DELTA);
    }

    // --- Granični slučajevi (double) ---

    @Test
    void saberiVelikeBrojevePrelaziUBeskonacnost() {
        assertEquals(Double.POSITIVE_INFINITY,
                Operacije.saberi(Double.MAX_VALUE, Double.MAX_VALUE));
    }

    @Test
    void pomnoziVelikeBrojevePrelaziUBeskonacnost() {
        assertEquals(Double.NEGATIVE_INFINITY,
                Operacije.pomnozi(Double.MAX_VALUE, -2));
    }

    @Test
    void operacijeSaNaNVracajuNaN() {
        assertTrue(Double.isNaN(Operacije.saberi(Double.NaN, 1)));
        assertTrue(Double.isNaN(Operacije.oduzmi(1, Double.NaN)));
        assertTrue(Double.isNaN(Operacije.pomnozi(Double.NaN, 0)));
    }
}
