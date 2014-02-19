package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;

/**
 * User: tiziano
 * Date: 19/12/13
 * Time: 16:47
 */
public class PraticaToolbar extends PyPaPiToolBar {

    public PraticaToolbar(String title, Window parent){
        super(title, parent);
        this.insertButton("apriDettaglio", "Apri il dettaglio collegato",
                "classpath:com/axiastudio/suite/resources/arrow_turn_right.png",
                "Apri il dettaglio collegato", parent);
    }

}