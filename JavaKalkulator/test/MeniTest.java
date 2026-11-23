import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeniTest {

    private static final String MENI =
            "1 - Sabiranje\n"
            + "2 - Oduzimanje\n"
            + "3 - Mnozenje\n"
            + "4 - Dijeljenje\n"
            + "0 - Kraj\n"
            + "Izbor: ";

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

    /** Pokreće meni sa zadatim unosom i vraća ispis (sa \n kao krajem reda). */
    private String pokreni(String unos) {
        assertTimeoutPreemptively(Duration.ofSeconds(5),
                () -> Meni.pokreni(new Scanner(unos)));
        return izlaz.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static int brojPojavljivanja(String tekst, String podniz) {
        int broj = 0;
        for (int i = tekst.indexOf(podniz); i >= 0; i = tekst.indexOf(podniz, i + 1)) {
            broj++;
        }
        return broj;
    }

    // --- Kriterijumi prihvatanja iz issue-a #6 ---

    @Test
    void sabiranjeDvaIPetIPoIspisujeSedamIPedeset() {
        String ispis = pokreni("1\n2\n5.5\n0\n");
        assertTrue(ispis.contains("Rezultat: 7.50\n"), ispis);
    }

    @Test
    void dijeljenjePetSaNulomIspisujePorukuONuli() {
        String ispis = pokreni("4\n5\n0\n0\n");
        assertTrue(ispis.contains(Operacije.PORUKA_DIJELJENJE_NULOM), ispis);
        assertFalse(ispis.contains("Rezultat:"), ispis);
    }

    @Test
    void izborSedamIspisujeNepoznataOpcija() {
        String ispis = pokreni("7\n0\n");
        assertTrue(ispis.contains("Nepoznata opcija\n"), ispis);
    }

    @Test
    void izborNulaZavrsavaProgram() {
        assertEquals(MENI, pokreni("0\n"));
    }

    // --- Izgled menija i tok ---

    @Test
    void cijeliTokSabiranjaIzgledaKaoUIssueu() {
        String ocekivano = MENI
                + "Prvi broj: Drugi broj: Rezultat: 7.50\n"
                + MENI;
        assertEquals(ocekivano, pokreni("1\n2\n5.5\n0\n"));
    }

    @Test
    void nepoznataOpcijaPonovoPrikazujeMeni() {
        assertEquals(MENI + "Nepoznata opcija\n" + MENI, pokreni("7\n0\n"));
    }

    @Test
    void meniSePonavljaDokSeNeIzabereNula() {
        String ispis = pokreni("1\n1\n1\n2\n3\n6\n3\n4\n2\n4\n8\n2\n0\n");
        assertEquals(5, brojPojavljivanja(ispis, MENI));
        assertTrue(ispis.contains("Rezultat: 2.00\n"), ispis);
        assertTrue(ispis.contains("Rezultat: -3.00\n"), ispis);
        assertTrue(ispis.contains("Rezultat: 8.00\n"), ispis);
        assertTrue(ispis.contains("Rezultat: 4.00\n"), ispis);
    }

    @Test
    void poslijeNuleSeNistaViseNeCita() {
        // Unos poslije 0 se ignoriše — program je završio.
        String ispis = pokreni("0\n1\n2\n3\n");
        assertEquals(MENI, ispis);
    }

    @Test
    void nakonDijeljenjaNulomMeniNastavljaRad() {
        String ispis = pokreni("4\n5\n0\n4\n10\n4\n0\n");
        assertTrue(ispis.contains(Operacije.PORUKA_DIJELJENJE_NULOM), ispis);
        assertTrue(ispis.contains("Rezultat: 2.50\n"), ispis);
        assertEquals(3, brojPojavljivanja(ispis, MENI));
    }

    // --- Svaka operacija ---

    @Test
    void oduzimanje() {
        assertTrue(pokreni("2\n2\n5.5\n0\n").contains("Rezultat: -3.50\n"));
    }

    @Test
    void mnozenje() {
        assertTrue(pokreni("3\n2.5\n4\n0\n").contains("Rezultat: 10.00\n"));
    }

    @Test
    void dijeljenje() {
        assertTrue(pokreni("4\n10\n4\n0\n").contains("Rezultat: 2.50\n"));
    }

    @Test
    void dijeljenjeZaokruzujeNaDvijeDecimale() {
        assertTrue(pokreni("4\n2\n3\n0\n").contains("Rezultat: 0.67\n"));
    }

    // --- Nepoznate opcije ---

    @Test
    void neispravniIzboriIspisujuNepoznataOpcija() {
        for (String izbor : new String[] {"5", "-1", "10", "01", "1.0", "abc", "+1"}) {
            izlaz.reset();
            String ispis = pokreni(izbor + "\n0\n");
            assertEquals(MENI + "Nepoznata opcija\n" + MENI, ispis, "izbor = " + izbor);
        }
    }

    // --- Unos brojeva ---

    @Test
    void prihvataDecimalniZarez() {
        assertTrue(pokreni("1\n2\n5,5\n0\n").contains("Rezultat: 7.50\n"));
    }

    @Test
    void brojeviMoguBitiUIstomRedu() {
        assertTrue(pokreni("1 2 5.5\n0\n").contains("Rezultat: 7.50\n"));
    }

    @Test
    void neispravanBrojSeTraziPonovo() {
        String ispis = pokreni("1\nabc\n2\n5.5\n0\n");
        assertEquals(MENI
                + "Prvi broj: " + Meni.PORUKA_NEISPRAVAN_BROJ + "\n"
                + "Prvi broj: Drugi broj: Rezultat: 7.50\n"
                + MENI, ispis);
    }

    @Test
    void neispravanDrugiBrojSeTraziPonovo() {
        String ispis = pokreni("1\n2\nx\n5.5\n0\n");
        assertTrue(ispis.contains("Drugi broj: " + Meni.PORUKA_NEISPRAVAN_BROJ + "\nDrugi broj: "), ispis);
        assertTrue(ispis.contains("Rezultat: 7.50\n"), ispis);
    }

    @Test
    void negativniBrojevi() {
        assertTrue(pokreni("3\n-2\n-3.5\n0\n").contains("Rezultat: 7.00\n"));
    }

    @Test
    void bomNaPocetkuUlazaSeIgnorise() {
        assertTrue(pokreni("﻿1\n2\n5.5\n0\n").contains("Rezultat: 7.50\n"));
        izlaz.reset();
        assertTrue(pokreni("1\n﻿2\n5.5\n0\n").contains("Rezultat: 7.50\n"));
    }

    // --- Kraj ulaza (EOF) ---

    @Test
    void prazanUlazZavrsavaProgram() {
        assertEquals(MENI + "\n", pokreni(""));
    }

    @Test
    void krajUlazaPrilikomUnosaBrojaZavrsavaProgram() {
        String ispis = pokreni("1\n2\n");
        assertTrue(ispis.endsWith("Drugi broj: \n"), ispis);
        assertFalse(ispis.contains("Rezultat:"), ispis);
    }

    // --- formatirajRezultat ---

    @Test
    void formatiranjeNaDvijeDecimale() {
        assertEquals("7.50", Meni.formatirajRezultat(7.5));
        assertEquals("0.00", Meni.formatirajRezultat(0));
        assertEquals("-3.50", Meni.formatirajRezultat(-3.5));
        assertEquals("0.67", Meni.formatirajRezultat(2.0 / 3.0));
        assertEquals("1000000.00", Meni.formatirajRezultat(1_000_000));
    }

    @Test
    void formatiranjeKoristiTackuBezObziraNaLocale() {
        Locale stari = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);
            assertEquals("7.50", Meni.formatirajRezultat(7.5));
        } finally {
            Locale.setDefault(stari);
        }
    }

    @Test
    void formatiranjeNeIspisujeMinusNulu() {
        assertEquals("0.00", Meni.formatirajRezultat(-0.001));
        assertEquals("0.00", Meni.formatirajRezultat(-0.0));
    }

    // --- parsirajBroj ---

    @Test
    void parsiranjeIspravnihBrojeva() {
        assertEquals(5.5, Meni.parsirajBroj("5.5"));
        assertEquals(5.5, Meni.parsirajBroj("5,5"));
        assertEquals(-2.0, Meni.parsirajBroj("-2"));
        assertEquals(3.0, Meni.parsirajBroj("+3"));
        assertEquals(0.5, Meni.parsirajBroj(".5"));
        assertEquals(5.0, Meni.parsirajBroj("5."));
        assertEquals(1500.0, Meni.parsirajBroj("1.5e3"));
    }

    @Test
    void parsiranjeNeispravnihBrojevaVracaNull() {
        for (String tekst : new String[] {"", "abc", "5d", "5f", "0x10", "NaN", "Infinity",
                "-Infinity", "1.2.3", "1e", "--1", ".", "1e999"}) {
            assertNull(Meni.parsirajBroj(tekst), "tekst = " + tekst);
        }
    }
}
