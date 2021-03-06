/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.i18n;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;

/**
 * @author Séverin Moussel, Fabio Lombardi
 * 
 */
public class I18n extends AbstractI18n {

    private static final String BONITA_HOME = System.getProperty("bonita.home");

    private static final String I18N_PATH = "client" + File.separator + "platform" + File.separator + "work" + File.separator + "i18n"
            + File.separator;

    private static final File I18N_DIR = new File(BONITA_HOME, I18N_PATH);

    private I18n() {
        // Singleton
    }

    public static I18n getInstance() {
        if (I18N_instance == null) {
            I18N_instance = new I18n();
        }
        return (I18n) I18N_instance;
    }

    @Override
    public void loadLocale(final LOCALE locale) {
        Map<String, String> results = loadLocale(locale, FileUtils.listDir(I18N_DIR));
        setLocale(locale, results);
    }

    private TreeMap<String, String> loadLocale(final LOCALE locale, List<File> files) {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (File file : getLocaleFiles(locale, files)) {
            treeMap.putAll(parsePoFile(file));
        }
        return treeMap;
    }

    private Map<String, String> parsePoFile(final File file) {
        return POParser.parse(file);
    }

    private List<File> getLocaleFiles(final LOCALE locale, List<File> files) {
        return FileUtils.getMatchingFiles(makeLocaleRegex(locale), files);
    }

    private String makeLocaleRegex(final LOCALE locale) {
        return new String("(.*)" + locale.toString().trim() + ".po");
    }

    private static File getLocaleFile(final String locale) {
        return new File(BONITA_HOME, I18N_PATH + locale + ".po");
    }

    public static Map<String, String> getAvailableLocales() {
        final Map<String, String> results = new LinkedHashMap<String, String>();
        final Map<String, String> locales = getLocales();

        for (final String locale : locales.keySet()) {
            if (getLocaleFile(locale).exists()) {
                results.put(locale, locales.get(locale));
            }
        }

        return results;
    }
}
