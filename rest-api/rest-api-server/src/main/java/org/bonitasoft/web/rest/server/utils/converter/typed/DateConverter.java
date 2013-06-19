/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.utils.converter.typed;

import java.util.Date;

import org.bonitasoft.web.rest.server.utils.converter.Converter;

/**
 * @author Colin PUY
 * 
 */
//FIXME : implement when ENGINE-1099 is resolved
public class DateConverter implements Converter<Date> {

    @Override
    public Date convert(String convert) {
        throw new RuntimeException("Date converter is not implemented yet");
    }

}