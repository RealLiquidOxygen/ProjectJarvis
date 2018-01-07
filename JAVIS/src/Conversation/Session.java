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
package Conversation;

import datastructures.Human;
import datastructures.SessionVideo;
import datastructures.WhatMattered;
import datastructures.WhoWasThere;
import java.util.Date;

/**
 *
 * @author owoye001
 */
public class Session {
    
    private Human HumanJarvis; //javis mood
    private Human startSessionHuman; //start session human. 
    private Date sessionDate; //the date the session took place
    private SessionVideo sessionVideo; //session recorded video
    private WhoWasThere whoWasThere; //session entities
    private WhatMattered whatMattered; //what mattered
    
    
    
    public Session(){
        
        sessionDate = new Date();
        sessionVideo = new SessionVideo();
        whoWasThere = new WhoWasThere();
        whatMattered = new WhatMattered();
        startSessionHuman = new Human(null, null, null);
        HumanJarvis = new Human(null, null, "This is an artifician intelligence powered human");
        
    }

  

    /**
     * @return the startSessionHuman
     */
    public Human getStartSessionHuman() {
        return startSessionHuman;
    }

    /**
     * @param startSessionHuman the startSessionHuman to set
     */
    public void setStartSessionHuman(Human startSessionHuman) {
        this.startSessionHuman = startSessionHuman;
    }

    /**
     * @return the sessionDate
     */
    public Date getSessionDate() {
        return sessionDate;
    }

    /**
     * @param sessionDate the sessionDate to set
     */
    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    /**
     * @return the sessionVideo
     */
    public SessionVideo getSessionVideo() {
        return sessionVideo;
    }

    /**
     * @param sessionVideo the sessionVideo to set
     */
    public void setSessionVideo(SessionVideo sessionVideo) {
        this.sessionVideo = sessionVideo;
    }

    /**
     * @return the whoWasThere
     */
    public WhoWasThere getWhoWasThere() {
        return whoWasThere;
    }

    /**
     * @param whoWasThere the whoWasThere to set
     */
    public void setWhoWasThere(WhoWasThere whoWasThere) {
        this.whoWasThere = whoWasThere;
    }

    /**
     * @return the whatMattered
     */
    public WhatMattered getWhatMattered() {
        return whatMattered;
    }

    /**
     * @param whatMattered the whatMattered to set
     */
    public void setWhatMattered(WhatMattered whatMattered) {
        this.whatMattered = whatMattered;
    }

    /**
     * @return the HumanJarvis
     */
    public Human getHumanJarvis() {
        return HumanJarvis;
    }

    /**
     * @param HumanJarvis the HumanJarvis to set
     */
    public void setHumanJarvis(Human HumanJarvis) {
        this.HumanJarvis = HumanJarvis;
    }
    
   
    
    
}
