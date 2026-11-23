import java.util.Locale;
import java.util.Scanner;

/**
 * Tekstualni meni kalkulatora u konzoli.
 *
 * Korisnik bira operaciju, unosi dva realna broja i dobija rezultat na dvije
 * decimale. Meni se ponavlja dok korisnik ne izabere {@code 0} (ili dok se
 * ulaz ne završi). Računanje se delegira klasi {@link Operacije}.
 */
public final class Meni {

    public static final String TEKST_MENIJA =
            "1 - Sabiranje\n"
            + "2 - Oduzimanje\n"
            + "3 - Mnozenje\n"
            + "4 - Dijeljenje\n"
            + "0 - Kraj";

    public static final String PORUKA_NEPOZNATA_OPCIJA = "Nepoznata opcija";
    public static final String PORUKA_NEISPRAVAN_BROJ = "Neispravan broj, pokusajte ponovo.";

    private Meni() {
    }

    /**
     * Pokreće meni: čita izbore i brojeve iz {@code ulaz}, a ispisuje na
     * {@code System.out}. Vraća se kada korisnik izabere {@code 0} ili kada
     * ulaz nema više podataka.
     */
    public static void pokreni(Scanner ulaz) {
        while (true) {
            for (String linija : TEKST_MENIJA.split("\n")) {
                System.out.println(linija);
            }
            System.out.print("Izbor: ");

            if (!ulaz.hasNext()) {
                System.out.println();
                return;
            }
            String izbor = ocisti(ulaz.next());

            if (izbor.equals("0")) {
                return;
            }
            if (!izbor.matches("[1-4]")) {
                System.out.println(PORUKA_NEPOZNATA_OPCIJA);
                continue;
            }

            Double a = ucitajBroj(ulaz, "Prvi broj: ");
            if (a == null) {
                return;
            }
            Double b = ucitajBroj(ulaz, "Drugi broj: ");
            if (b == null) {
                return;
            }

            switch (izbor) {
                case "1" -> ispisiRezultat(Operacije.saberi(a, b));
                case "2" -> ispisiRezultat(Operacije.oduzmi(a, b));
                case "3" -> ispisiRezultat(Operacije.pomnozi(a, b));
                case "4" -> {
                    // podijeli() sam ispisuje poruku o dijeljenju nulom;
                    // tada se rezultat ne ispisuje.
                    double rezultat = Operacije.podijeli(a, b);
                    if (b != 0) {
                        ispisiRezultat(rezultat);
                    }
                }
                default -> throw new IllegalStateException("Nedostižno: " + izbor);
            }
        }
    }

    /** Formatira rezultat na dvije decimale, npr. {@code 7.5 -> "7.50"}. */
    static String formatirajRezultat(double rezultat) {
        String tekst = String.format(Locale.ROOT, "%.2f", rezultat);
        // Vrlo mali negativni brojevi bi se ispisali kao "-0.00".
        return tekst.equals("-0.00") ? "0.00" : tekst;
    }

    /**
     * Parsira realan broj; prihvata i decimalni zarez ({@code 5,5}).
     * Vraća {@code null} za neispravan unos, kao i za NaN i beskonačnost.
     */
    static Double parsirajBroj(String tekst) {
        String normalizovan = ocisti(tekst).replace(',', '.');
        // Double.parseDouble prihvata i "5d", "5f", "0x1p3", "NaN"...;
        // dozvoljavamo samo obične decimalne brojeve (opciono sa eksponentom).
        if (!normalizovan.matches("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?")) {
            return null;
        }
        double broj = Double.parseDouble(normalizovan);
        return Double.isFinite(broj) ? broj : null;
    }

    /**
     * Uklanja razmake i BOM ({@code ﻿}) koji PowerShell na Windows-u
     * dodaje na početak ulaza kada se tekst preusmjeri programu ({@code "..." | java Main}).
     */
    private static String ocisti(String tekst) {
        return tekst.replace("﻿", "").trim();
    }

    private static void ispisiRezultat(double rezultat) {
        System.out.println("Rezultat: " + formatirajRezultat(rezultat));
    }

    /** Traži broj dok unos ne bude ispravan; vraća {@code null} na kraju ulaza. */
    private static Double ucitajBroj(Scanner ulaz, String upit) {
        while (true) {
            System.out.print(upit);
            if (!ulaz.hasNext()) {
                System.out.println();
                return null;
            }
            Double broj = parsirajBroj(ulaz.next());
            if (broj != null) {
                return broj;
            }
            System.out.println(PORUKA_NEISPRAVAN_BROJ);
        }
    }
}
