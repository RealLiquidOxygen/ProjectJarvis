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
package jarvis.inpustream;

import Conversation.SessionManager;
import jarvis.ProjectJarvis;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import outputstream.BlackBoard;

/**
 *
 * @author owoye001
 */
 public class ClickEvent implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!BlackBoard.mainPanel.SystemReadyStatus) {
                return;
            }
            if (e.getClickCount() == 1) {
                Thread newActivity = new Thread() {
                    @Override
                    public void run() {
                        if (ProjectJarvis.txtEntry != null) ProjectJarvis.txtEntry.setVisible(false); //kill existing thread
                        ProjectJarvis.txtEntry = new TextEntry();
                        ProjectJarvis.txtEntry.setVisible(true);
                        SessionManager.mainListenerTextEars.resetTextEntryVariableTypesScreen();
                        BlackBoard.mainPanel.textEntry = true; //text entry
                        BlackBoard.mainPanel.sessionManager.collectInputValues();
                    }
                };
                newActivity.start();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

    }
