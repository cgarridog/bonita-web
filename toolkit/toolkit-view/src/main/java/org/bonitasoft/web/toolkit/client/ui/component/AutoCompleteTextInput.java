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
package org.bonitasoft.web.toolkit.client.ui.component;

import static com.google.gwt.query.client.GQuery.$;

import java.util.LinkedList;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CompoundAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.core.Components;
import org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownItem;
import org.bonitasoft.web.toolkit.client.ui.component.dropdown.DropDownPanel;
import org.bonitasoft.web.toolkit.client.ui.component.event.InputCompleteEvent;
import org.bonitasoft.web.toolkit.client.ui.html.HTML;
import org.bonitasoft.web.toolkit.client.ui.html.HTMLClass;
import org.bonitasoft.web.toolkit.client.ui.html.XML;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

/**
 * @author Séverin Moussel
 * 
 */
public class AutoCompleteTextInput extends Components implements Refreshable {

    private ItemDefinition itemDefinition = null;

    private JsId jsid = null;

    private final DropDownPanel dropdown = new DropDownPanel();

    private Element inputElement = null;

    private int nbResults = 20;

    /**
     * Delay after a change before refreshing items.
     */
    private int onChangeDelay = 250;

    private AbstractAttributeReader labelTemplate = null;

    private String valueAttributeName = null;

    private Filler<AutoCompleteTextInput> refreshFiller;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AutoCompleteTextInput(final JsId jsid, final ItemDefinition itemDefinition, final String labelAttributeName, final String valueAttributeName) {
        this(jsid, itemDefinition, new AttributeReader(labelAttributeName), valueAttributeName);
    }

    public AutoCompleteTextInput(final JsId jsid, final ItemDefinition itemDefinition, final AbstractAttributeReader labelTemplate,
            final String valueAttributeName) {
        super();
        this.jsid = jsid;
        this.itemDefinition = itemDefinition;
        this.labelTemplate = labelTemplate;
        this.valueAttributeName = valueAttributeName;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AutoCompleteTextInput setNbResults(final int nbResults) {
        this.nbResults = nbResults;

        if (isGenerated()) {
            refresh();
        }

        return this;
    }

    /**
     * @param onChangeDelay
     *            the onChangeDelay to set
     */
    public AutoCompleteTextInput setOnChangeDelay(final int onChangeDelay) {
        this.onChangeDelay = onChangeDelay;
        if (isGenerated()) {
            setEvents();
        }
        return this;
    }

    public AutoCompleteTextInput setValue(final String label, final String value) {
        this.setValue(this.inputElement, label, value);
        return this;
    }

    public AutoCompleteTextInput setValue(final String value) {

        // If the label is not passed, we call the API to get the corresponding label;
        APIID apiid = APIID.makeAPIID(value);
        if (apiid != null) {
            this.itemDefinition.getAPICaller().get(apiid,
                    new APICallback() {

                        @Override
                        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                            final IItem item = JSonItemReader.parseItem(response, AutoCompleteTextInput.this.itemDefinition);
                            if (item == null) {
                                return;
                            }
                            AutoCompleteTextInput.this.setValue(AutoCompleteTextInput.this.labelTemplate.read(item), value);
                        }
                    });
        }
        return this;
    }

    public String getValue() {
        return this.getValue(this.inputElement);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DROPDOWN
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AutoCompleteTextInput clearResults() {
        this.dropdown.empty();
        return this;
    }

    public AutoCompleteTextInput setResults(final java.util.List<IItem> items) {
        this.dropdown.empty();
        addResults(items);

        return this;
    }

    public AutoCompleteTextInput addResults(final java.util.List<IItem> items) {
        for (final IItem item : items) {
            addResult(item);
        }

        this.dropdown.open();

        return this;
    }

    public AutoCompleteTextInput addResult(final IItem item) {

        final String label = this.labelTemplate.read(item);
        final String value = item.getAttributeValue(this.valueAttributeName);

        this.dropdown.append(
                new DropDownItem(label, label,
                        new Action() {

                            @Override
                            public void execute() {
                                onItemSelected(label, value);
                            }

                        }
                ));

        return this;
    }

    private void onItemSelected(final String label, final String value) {
        setValue(label, value);
        this.dropdown.close();
        fireEvent(new InputCompleteEvent(value));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The filler called to fill the suggested items
     */
    private final class RefreshFiller extends Filler<AutoCompleteTextInput> {

        @Override
        protected void getData(final APICallback callback) {
            final String search = $(AutoCompleteTextInput.this.inputElement).val();
            if (search.length() > 0) {
                AutoCompleteTextInput.this.itemDefinition.getAPICaller().search(
                        0,
                        AutoCompleteTextInput.this.nbResults,
                        AutoCompleteTextInput.this.labelTemplate instanceof CompoundAttributeReader
                                ? APICaller.orderArrayToString(((CompoundAttributeReader) AutoCompleteTextInput.this.labelTemplate).getAttributes())
                                : AutoCompleteTextInput.this.labelTemplate.getLeadAttribute(),
                        search,
                        callback
                        );
            }
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final java.util.List<IItem> items = JSonItemReader.parseItems(json, AutoCompleteTextInput.this.itemDefinition);

            setResults(items);
        }
    }

    private void initFiller() {
        this.refreshFiller = new RefreshFiller();
    }

    @Override
    public void refresh() {
        this.refreshFiller.run();
        this.dropdown.open();
    }

    private native void setValue(Element e, String label, String value)
    /*-{
        $wnd.$(e).val(value).label(label).addClass('completed');
    }-*/;

    private native void resetValue(Element e)
    /*-{
        $wnd.$(e).val('').removeClass('completed');
    }-*/;

    private void resetValue() {
        this.resetValue(this.inputElement);
    }

    private native void reset(Element e)
    /*-{
        $wnd.$(e).label('').val('').removeClass('completed');
    }-*/;

    private void reset() {
        this.dropdown.close();
        this.reset(this.inputElement);
    }

    private native String getValue(Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    private static String AUTOCOMPLETE_ONCHANGE_TIMER = "AUTOCOMPLETE_ONCHANGE_TIMER";

    private void setEvents() {

        final GQuery input = $(this.inputElement);

        // ON TEXT CHANGE
        input.keypress(new Function() {

            @Override
            public boolean f(final Event e, final Object data) {

                // Window.alert(e.getKeyCode() + " - " + e.getCharCode());

                switch (e.getKeyCode()) {
                    case KeyCodes.KEY_ENTER:
                        e.stopPropagation();
                        return false;
                    case KeyCodes.KEY_ESCAPE:
                        AutoCompleteTextInput.this.reset();
                        break;
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_BACKSPACE:
                    case 0: // Character
                    default:

                        AutoCompleteTextInput.this.resetValue();

                        // ClearTimeout if another key is pressed before the end of previous timeout
                        if (input.data(AUTOCOMPLETE_ONCHANGE_TIMER) != null) {
                            final Timer t = (Timer) input.data(AUTOCOMPLETE_ONCHANGE_TIMER);
                            t.cancel();
                            input.removeData(AUTOCOMPLETE_ONCHANGE_TIMER);
                        }

                        final Timer t = new Timer() {

                            @Override
                            public void run() {
                                input.removeData(AUTOCOMPLETE_ONCHANGE_TIMER);
                                AutoCompleteTextInput.this.refresh();
                            }
                        };
                        input.data("AUTOCOMPLETE_ONCHANGE_TIMER", t);
                        t.schedule(500);
                        break;
                }

                return true;
            }
        });

        // ON TEXT BLUR
        input.blur(new Function() {

            @Override
            public void f() {
                if (input.data(AUTOCOMPLETE_ONCHANGE_TIMER) != null) {
                    final Timer t = (Timer) input.data(AUTOCOMPLETE_ONCHANGE_TIMER);
                    t.cancel();
                    input.removeData(AUTOCOMPLETE_ONCHANGE_TIMER);
                }

                if (!$(AutoCompleteTextInput.this.inputElement).is(".completed")
                        && $(AutoCompleteTextInput.this.dropdown.getElement()).find(":focus").size() == 0) {

                    AutoCompleteTextInput.this.reset();
                    AutoCompleteTextInput.this.dropdown.close();
                }
            }

        });
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HTML GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected java.util.List<Element> makeElements() {
        final LinkedList<Element> elements = new LinkedList<Element>();

        this.inputElement = XML.makeElement(HTML.inputText(
                this.jsid.toString(),
                new HTMLClass("delegateinput")
                        .addClass("autocomplete")
                        .add("autocomplete", "off")));

        setEvents();

        this.dropdown.addClass("autocomplete");

        elements.add(this.inputElement);
        elements.add(this.dropdown.getElement());
        this.dropdown.close();

        initFiller();

        return elements;
    }

}
