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
package org.bonitasoft.web.rest.model.builder.profile;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.impl.ProfileImpl;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;

/**
 * @author Colin PUY
 * 
 */
public class ProfileItemBuilder {

    protected long id = 1L;
    protected String name = "aName";
    protected String description = "aDescription";
    protected String icon = "anIcon";
    protected boolean isDefault = false;

    public static ProfileItemBuilder aProfileItem() {
        return new ProfileItemBuilder();
    }

    public ProfileItem build() {
        ProfileItem item = new ProfileItem();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setIcon(icon);
        item.setIsDefault(isDefault);
        return item;
    }
    
    public Profile toProfile() {
    	ProfileImpl profile = new ProfileImpl(name);
    	profile.setDescription(description);
    	profile.setIconPath(icon);
    	profile.setDefault(isDefault);
    	return profile;
    }
    
    public ProfileItemBuilder fromEngineItem(Profile profile) {
        id = profile.getId();
        name = profile.getName();
        description = profile.getDescription();
        icon = profile.getIconPath();
        isDefault = profile.isDefault();
        return this;
    }

    public ProfileItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProfileItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public ProfileItemBuilder isDefault(boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }
}
