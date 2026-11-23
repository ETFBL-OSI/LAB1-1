import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperacijeTest {

    private static final double DELTA = 1e-9;

    private final PrintStream originalniOut = System.out;
    private final ByteArrayOutputStream izlaz = new ByteArrayOutputStream();

    @BeforeEach
    void preusmjeriIzlaz() {
        System.setOut(new PrintStream(izlaz, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void vratiIzlaz() {
        System.setOut(originalniOut);
    }

    private String ispis() {
        return izlaz.toString(StandardCharsets.UTF_8);
    }

    // --- Kriterijumi prihvatanja iz issue-a #5 ---

    @Test
    void podijeliDesetICetiriVracaDvaIPo() {
        assertEquals(2.5, Operacije.podijeli(10, 4), DELTA);
    }

    @Test
    void podijeliMinusDevetITriVracaMinusTri() {
        assertEquals(-3.0, Operacije.podijeli(-9, 3), DELTA);
    }

    @Test
    void podijeliPetINulaIspisujePorukuIVracaNulu() {
        assertEquals(0.0, Operacije.podijeli(5, 0), DELTA);
        assertEquals("Greska: dijeljenje nulom!", ispis().trim());
    }

    // --- Dijeljenje ---

    @Test
    void podijeliBezGreskeNistaNeIspisuje() {
        Operacije.podijeli(10, 4);
        assertEquals("", ispis());
    }

    @Test
    void podijeliDvaNegativnaDajePozitivan() {
        assertEquals(4.0, Operacije.podijeli(-12, -3), DELTA);
    }

    @Test
    void podijeliPozitivanINegativanDajeNegativan() {
        assertEquals(-4.0, Operacije.podijeli(12, -3), DELTA);
    }

    @Test
    void podijeliSaJedanVracaIstiBroj() {
        assertEquals(7.25, Operacije.podijeli(7.25, 1), DELTA);
    }

    @Test
    void podijeliNuluSaBrojemVracaNulu() {
        assertEquals(0.0, Operacije.podijeli(0, 5), DELTA);
        assertEquals("", ispis());
    }

    @Test
    void podijeliRealneBrojeve() {
        assertEquals(0.5, Operacije.podijeli(0.75, 1.5), DELTA);
    }

    @Test
    void podijeliRezultatSaBeskonacnimDecimalama() {
        assertEquals(1.0 / 3.0, Operacije.podijeli(1, 3), DELTA);
    }

    // --- Dijeljenje nulom (granični slučajevi) ---

    @Test
    void podijeliNegativanBrojNulomVracaNulu() {
        double rezultat = Operacije.podijeli(-5, 0);
        assertEquals(0.0, rezultat, DELTA);
        assertFalse(Double.isInfinite(rezultat));
        assertTrue(ispis().contains("Greska: dijeljenje nulom!"));
    }

    @Test
    void podijeliNuluNulomVracaNuluANeNaN() {
        double rezultat = Operacije.podijeli(0, 0);
        assertEquals(0.0, rezultat, DELTA);
        assertFalse(Double.isNaN(rezultat));
        assertTrue(ispis().contains("Greska: dijeljenje nulom!"));
    }

    @Test
    void podijeliNegativnomNulomSeTretiraKaoDijeljenjeNulom() {
        double rezultat = Operacije.podijeli(5, -0.0);
        assertEquals(0.0, rezultat, DELTA);
        assertFalse(Double.isInfinite(rezultat));
        assertTrue(ispis().contains("Greska: dijeljenje nulom!"));
    }

    @Test
    void podijeliNulomNeVracaBeskonacnostNiNaN() {
        for (double a : new double[] {1, -1, 0, Double.MAX_VALUE, Double.MIN_VALUE}) {
            double rezultat = Operacije.podijeli(a, 0);
            assertFalse(Double.isInfinite(rezultat), "a = " + a);
            assertFalse(Double.isNaN(rezultat), "a = " + a);
        }
    }

    @Test
    void porukaJeIstaKaoUIssueu() {
        assertEquals("Greska: dijeljenje nulom!", Operacije.PORUKA_DIJELJENJE_NULOM);
    }

    @Test
    void podijeliVeomaMalimBrojemNijeDijeljenjeNulom() {
        assertEquals(Double.POSITIVE_INFINITY, Operacije.podijeli(Double.MAX_VALUE, Double.MIN_VALUE));
        assertEquals("", ispis());
    }

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
