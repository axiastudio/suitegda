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
package com.axiastudio.suite.finanziaria.entities;

import com.axiastudio.suite.deliberedetermine.entities.ImpegnoDetermina;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * Per le realtà che hanno una gestione delle finanziaria è possibile scrivere
 * e registrare un utilità per la gestione dell'interoperabilità.
 * 
 * IFinanziaria è un'interfaccia segnaposto. L'utilità registrata non deve
 * necessariamente dichiarare la sua implementazione.
 * 
 */
public interface IFinanziaria {
    
    public List<ImpegnoDetermina> getImpegniDetermina(String attoOBozza, String organoSettore, String anno, String numero);
    
}