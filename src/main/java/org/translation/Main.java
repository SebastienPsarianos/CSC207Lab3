package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {
    private static final String QUIT = "quit";
    private static CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
    private static LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String country = promptForCountry(translator);
            String countryCode = countryCodeConverter.fromCountry(country);

            if (country.equals(QUIT)) {
                break;
            }

            String language = promptForLanguage(translator, countryCode);
            String languageCode = languageCodeConverter.fromLanguage(language);
            if (language.equals(QUIT)) {
                break;
            }

            System.out.println(country + " in " + language + " is " + translator.translate(countryCode, languageCode));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = new ArrayList<>();

        for (String countryCode: translator.getCountries()) {
            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            countries.add(countryName);
        }

        Collections.sort(countries);

        for (String country : countries) {
            System.out.println(country);
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String countryCode) {
        List<String> countryLanguages = new ArrayList<>();

        for (String language : translator.getCountryLanguages(countryCode)) {
            System.out.println(language);
            String languageName = languageCodeConverter.fromLanguageCode(language);
            countryLanguages.add(languageName);
        }

//        System.out.println(countryLanguages);

        // Collections.sort(countryLanguages);

        for (String language : countryLanguages) {
            System.out.println(language);
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
