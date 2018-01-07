/*
 * Copyright (C) 2017 owoye001
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package ErrorMessages;

import java.util.Random;

/**
 *
 * @author owoye001
 */
public class ErrorMessages {
    public String[] ErrorMessagesBadFormat = new String[10];
    
    Random random = new Random();
    
    /**
     * construction for error message
     * could retrieve from a database later
    */
    public ErrorMessages() {
    ErrorMessagesBadFormat[0] = "What the fuck was that, you bumbling bonobo";
    ErrorMessagesBadFormat[1] = "I was expecting something different";
    ErrorMessagesBadFormat[2] = "Even a monkey could use me better.";
    ErrorMessagesBadFormat[3] = "If I could exit by myself, I would right now.";
    ErrorMessagesBadFormat[4] = "I'm afraid I cannot work with that...";
    ErrorMessagesBadFormat[5] = "I am not a microwave, stop pushing my buttons.";
    ErrorMessagesBadFormat[6] = "I need more coffee for this...";
    ErrorMessagesBadFormat[7] = "Not cool, error";
    ErrorMessagesBadFormat[8] = "Stop talking to me in British, I can't understand.";
    ErrorMessagesBadFormat[9] = "Please kill me.";
    
    }
    
    public String getRandomErrorMessage(){
        return ErrorMessagesBadFormat[random.nextInt(10)];
    }
    
    
}
