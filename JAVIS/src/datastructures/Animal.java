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
package datastructures;

import AbstractClasses.JuniorObject;

/**
 *
 * @author owoye001
 */
public class Animal extends JuniorObject{
    
    private String AnimalName;
    private String AnimalLastSetNickName;
    private String AnimalClassification;
    private String AnimalsOwner;
    
    public Animal(String AnimalName, String AnimalLastSetNickName, String AnimalDescription){
        super(AnimalName,AnimalLastSetNickName,AnimalDescription);
    }

    /**
     * @return the AnimalName
     */
    public String getAnimalName() {
        return AnimalName;
    }

    /**
     * @param AnimalName the AnimalName to set
     */
    public void setAnimalName(String AnimalName) {
        this.AnimalName = AnimalName;
    }

    /**
     * @return the AnimalClassification
     */
    public String getAnimalClassification() {
        return AnimalClassification;
    }

    /**
     * @param AnimalClassification the AnimalClassification to set
     */
    public void setAnimalClassification(String AnimalClassification) {
        this.AnimalClassification = AnimalClassification;
    }

    /**
     * @return the AnimalLastSetNickName
     */
    public String getAnimalLastSetNickName() {
        return AnimalLastSetNickName;
    }

    /**
     * @param AnimalLastSetNickName the AnimalLastSetNickName to set
     */
    public void setAnimalLastSetNickName(String AnimalLastSetNickName) {
        this.AnimalLastSetNickName = AnimalLastSetNickName;
    }

    /**
     * @return the AnimalsOwner
     */
    public String getAnimalsOwner() {
        return AnimalsOwner;
    }

    /**
     * @param AnimalsOwner the AnimalsOwner to set
     */
    public void setAnimalsOwner(String AnimalsOwner) {
        this.AnimalsOwner = AnimalsOwner;
    }
}
