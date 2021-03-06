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
package org.bonitasoft.console.client.javascript.libs;

import org.bonitasoft.console.client.javascript.JsResource;
import org.bonitasoft.console.client.javascript.Lib;

/**
 * @author Vincent Elcrin
 * 
 */
public class Portal extends Lib {

    public Portal() {
        super(
                new JsResource(PortalRes.INSTANCE.common()),
                new JsResource(PortalRes.INSTANCE.popup()),
                new JsResource(PortalRes.INSTANCE.dashboard()),
                new JsResource(PortalRes.INSTANCE.form()),
                new JsResource(PortalRes.INSTANCE.skin()),
                new JsResource(PortalRes.INSTANCE.table()),
                new JsResource(PortalRes.INSTANCE.menu()),
                new JsResource(PortalRes.INSTANCE.switchPanel()),
                new JsResource(PortalRes.INSTANCE.wizard()),
                new JsResource(PortalRes.INSTANCE.socialbar()),
                new JsResource(PortalRes.INSTANCE.gwt()),
                new JsResource(PortalRes.INSTANCE.datepicker()),
                new JsResource(PortalRes.INSTANCE.autoUploader()),
                new JsResource(PortalRes.INSTANCE.arrayPrototype()));
    }
}
