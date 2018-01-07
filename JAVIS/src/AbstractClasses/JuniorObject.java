/*
 * Copyright (C) 2016 owoye001
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
package AbstractClasses;

import java.util.Date;

/**
 * Every outside world object is a junior object.
 * @author owoye001
 */
public abstract class JuniorObject {
    
    private String ObjectName;
    private String[] ObjectNickNames = new String[5];
    private int ActualNumberOfNickNames = 0;
    private String ObjectDescription = "";
    private String DateLastModified = "";
    private final String DateCreated = new Date().toString();
    
    /**
     * 
     * @param ObjectName the name of the object that is being created 
     * @param ObjectNickNames the nickname of the object being created
     * @param ObjectDescription the description of the object being created 
     */
    public JuniorObject(String ObjectName, String ObjectNickNames, String ObjectDescription) {
        this.ObjectName = ObjectName; //new objectname
        
        if (ActualNumberOfNickNames<=4){
           
            this.ObjectNickNames[ActualNumberOfNickNames++] = ObjectNickNames; //new nickname 
        } else {
            ActualNumberOfNickNames = 0; //reset nicknames and override old ones
        }
        
        this.ObjectDescription = ObjectDescription; //new object description 
        this.DateLastModified = new Date().toString();
    }

    /**
     * @return the ObjectName
     */
    public String getObjectName() {
        return ObjectName;
    }

    /**
     * @param ObjectName the ObjectName to set
     */
    public void setObjectName(String ObjectName) {
        this.ObjectName = ObjectName;
        this.DateLastModified = new Date().toString();
    }

    /**
     * @return the ObjectNickNames
     */
    public String[] getObjectNickNames() {
        return ObjectNickNames;
    }

    /**
     * @param ObjectNickNames the ObjectNickNames to set
     */
    public void setObjectNickNames(String[] ObjectNickNames) {
        this.ObjectNickNames = ObjectNickNames;
         this.DateLastModified = new Date().toString();
    }

    /**
     * @return the ActualNumberOfNickNames
     */
    public int getActualNumberOfNickNames() {
        return ActualNumberOfNickNames;
    }

    /**
     * @param ActualNumberOfNickNames the ActualNumberOfNickNames to set
     */
    public void setActualNumberOfNickNames(int ActualNumberOfNickNames) {
        this.ActualNumberOfNickNames = ActualNumberOfNickNames;
         this.DateLastModified = new Date().toString();
    }

    /**
     * @return the ObjectDescription
     */
    public String getObjectDescription() {
        return ObjectDescription;
    }

    /**
     * @param ObjectDescription the ObjectDescription to set
     */
    public void setObjectDescription(String ObjectDescription) {
        this.ObjectDescription = ObjectDescription;
         this.DateLastModified = new Date().toString();
    }

    /**
     * @return the DateLastModified
     */
    public String getDateLastModified() {
        return DateLastModified;
    }

    /**
     * @return the DateCreated
     */
    public String getDateCreated() {
        return DateCreated;
    }
    protected enum ObjectState { ALIVE, NONLIVING, INBETWEEN, UNDEFINED};
    protected enum ObjectCategory { HUMAN, INTELLIGENTMACHINE, MACHINE, OBJECTS };
    
}
