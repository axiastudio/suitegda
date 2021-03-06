/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.plugins.ooops;

import com.axiastudio.pypapi.IStreamProvider;

import java.util.Map;

/**
 *
 * @author tiziano
 */
public class Template {
    private final String name;
    private final String description;
    private final IStreamProvider streamProvider;
    private RuleSet ruleSet;
    private final Map<String, Object> objectsMap;
    private String parentTemplateName;
    private Object userData=null;

    public Template(IStreamProvider streamProvider, String name, String description) {
        this(streamProvider, name, description, null, null, null);
    }

    public Template(IStreamProvider streamProvider, String name, String description, String parentTemplateName) {
        this(streamProvider, name, description, parentTemplateName, null, null);
    }

    public Template(IStreamProvider streamProvider, String name, String description, String parentTemplateName, RuleSet ruleSet) {
        this(streamProvider, name, description, parentTemplateName, ruleSet, null);
    }
    
    public Template(IStreamProvider streamProvider, String name, String description, String parentTemplateName, RuleSet ruleSet, Map<String, Object> objectsMap) {
        this.name = name;
        this.description = description;
        this.parentTemplateName = parentTemplateName;
        this.streamProvider = streamProvider;
        this.ruleSet = ruleSet;
        this.objectsMap = objectsMap;
    }

    public IStreamProvider getStreamProvider() {
        return streamProvider;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Map<String, Object> getObjectsMap() {
        return objectsMap;
    }

    public String getParentTemplateName() {
        return parentTemplateName;
    }

    public void setParentTemplateName(String parentTemplateName) {
        this.parentTemplateName = parentTemplateName;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
