package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, Map<String, String>> translationData = new HashMap<>();
    private final String alpha3 = "alpha3";
    private final CountryCodeConverter countryConverter = new CountryCodeConverter();
    private final LanguageCodeConverter languageConverter = new LanguageCodeConverter();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // Loop through the countries
            for (Object obj : jsonArray) {
                JSONObject country = (JSONObject) obj;

                // Grab all the keys other than alpha2, id and alpha3
                Set<String> keys = getLanguageKeys(country);

                Map<String, String> countryData = new HashMap<>();
                for (String key: keys) {
                    // Each country name translation is stored as <LanguageCode, Translation>
                    countryData.put(key.toUpperCase(), country.getString(key));
                }

                // Store each country accessible by its alpha3 code
                translationData.put(country.getString(alpha3).toUpperCase(), countryData);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String countryCode) {
        ArrayList<String> countryLanguages = new ArrayList<>();

        for (String value : translationData.get(countryCode.toUpperCase()).values()) {
            if (!"id".equals(value) && !alpha3.equals(value) && !"alpha2".equals(value)) {
                countryLanguages.add(value);
            }
        }

        return countryLanguages;
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(translationData.keySet());
    }

    @Override
    public String translate(String country, String language) {

        return translationData.get(country.toUpperCase()).get(language.toUpperCase());
    }

    private Set<String> getLanguageKeys(JSONObject country) {
        Set<String> keys = country.keySet();
        keys.remove("id");
        keys.remove("alpha2");
        keys.remove(alpha3);
        return keys;
    }
}
