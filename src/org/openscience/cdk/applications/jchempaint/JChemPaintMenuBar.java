/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 1997-2005  The JChemPaint project
 *
 *  Contact: jchempaint-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All we ask is that proper credit is given for our work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openscience.cdk.applications.jchempaint;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import org.openscience.cdk.tools.LoggingTool;
import org.openscience.cdk.applications.jchempaint.action.JCPAction;

/**
 *  JChemPaint menu bar
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 * @created    22. April 2005
 */
public class JChemPaintMenuBar extends JMenuBar
{

	private LoggingTool logger;


	/**
	 *  The default constructor method. Adds the plugin menu.
	 *
	 *@param  jcpPanel  Description of the Parameter
	 */
	public JChemPaintMenuBar(JChemPaintPanel jcpPanel)
	{
		this(jcpPanel, true);
	}


	/**
	 *  The more flexible constructor method.
	 *
	 *@param  jcpPanel       Description of the Parameter
	 *@param  addPluginMenu  Description of the Parameter
	 */
	public JChemPaintMenuBar(JChemPaintPanel jcpPanel, boolean addPluginMenu)
	{
		this(jcpPanel, addPluginMenu, null);
	}


	/**
	 *  Constructor for the JChemPaintMenuBar object
	 *
	 *@param  jcpPanel        Description of the Parameter
	 *@param  addPluginMenu   Description of the Parameter
	 *@param  menuDefinition  Description of the Parameter
	 */
	public JChemPaintMenuBar(JChemPaintPanel jcpPanel, boolean addPluginMenu,
			String menuDefinition)
	{
		logger = new LoggingTool(this);
		createMenubar(jcpPanel, addPluginMenu, menuDefinition);
	}


	/**
	 *  Creates a JMenuBar with all the menues that are specified in the properties
	 *  file. <p>
	 *
	 *  The menu items in the bar are defined by the property 'menubar' in
	 *  org.openscience.cdk.applications.jchempaint.resources.JChemPaint.properties.
	 *
	 *@param  jcpPanel        Description of the Parameter
	 *@param  addPluginMenu   Description of the Parameter
	 *@param  menuDefinition  Description of the Parameter
	 */
	protected void createMenubar(JChemPaintPanel jcpPanel, boolean addPluginMenu, String menuDefinition)
	{
		addNormalMenuBar(jcpPanel, menuDefinition);
		/*
		 *  if (addPluginMenu && jcp.getPluginManager() != null) {
		 *  logger.info("Creating Plugin menu");
		 *  this.add(jcp.getPluginManager().getMenu());
		 *  }
		 */
		this.add(Box.createHorizontalGlue());
		this.add(createMenu(jcpPanel, "help"));
	}


	/**
	 *  Adds a feature to the NormalMenuBar attribute of the JChemPaintMenuBar
	 *  object
	 *
	 *@param  jcpPanel        The feature to be added to the NormalMenuBar
	 *      attribute
	 *@param  menuDefinition  The feature to be added to the NormalMenuBar
	 *      attribute
	 */
	private void addNormalMenuBar(JChemPaintPanel jcpPanel, String menuDefinition)
	{
		String definition = menuDefinition;
		if (definition == null)
		{
			definition = getMenuResourceString("menubar");
		}
		String[] menuKeys = StringHelper.tokenize(definition);
		for (int i = 0; i < menuKeys.length; i++)
		{
			JMenu m = createMenu(jcpPanel, menuKeys[i]);
			if (m != null)
			{
				this.add(m);
			}
		}
	}


	/**
	 *  Creates a JMenu given by a String with all the MenuItems specified in the
	 *  properties file.
	 *
	 *@param  key       The String used to identify the Menu
	 *@param  jcpPanel  Description of the Parameter
	 *@return           The created JMenu
	 */
	protected JMenu createMenu(JChemPaintPanel jcpPanel, String key)
	{
		logger.debug("Creating menu: ", key);
		String[] itemKeys = StringHelper.tokenize(getMenuResourceString(key));
		JMenu menu = new JMenu(JCPLocalizationHandler.getInstance().getString(key));
		for (int i = 0; i < itemKeys.length; i++)
		{
			if (itemKeys[i].equals("-"))
			{
				menu.addSeparator();
			} else if (itemKeys[i].startsWith("@"))
			{
				JMenu me = createMenu(jcpPanel, itemKeys[i].substring(1));
				menu.add(me);
			} else if (itemKeys[i].endsWith("+"))
			{
				JMenuItem mi = createMenuItem(jcpPanel,
						itemKeys[i].substring(0, itemKeys[i].length() - 1),
						true, false
						);
				// default off, because we cannot turn it on anywhere (yet)
				menu.add(mi);
			} else
			{
				JMenuItem mi = createMenuItem(jcpPanel, itemKeys[i], false, false);
				menu.add(mi);
			}
		}
		return menu;
	}


	/**
	 *  Gets the menuResourceString attribute of the JChemPaint object
	 *
	 *@param  key  Description of the Parameter
	 *@return      The menuResourceString value
	 */
	public String getMenuResourceString(String key)
	{
		String str;
		try
		{
			str = JCPPropertyHandler.getInstance().getGUIDefinition().getString(key);
		} catch (MissingResourceException mre)
		{
			str = null;
		}
		return str;
	}


	/**
	 *  Craetes a JMenuItem given by a String and adds the right ActionListener to
	 *  it.
	 *
	 *@param  cmd         String The Strin to identify the MenuItem
	 *@param  jcpPanel    Description of the Parameter
	 *@param  isCheckBox  Description of the Parameter
	 *@param  isChecked   Description of the Parameter
	 *@return             JMenuItem The created JMenuItem
	 */
	protected JMenuItem createMenuItem(JChemPaintPanel jcpPanel, String cmd, boolean isCheckBox, boolean isChecked)
	{
		logger.debug("Creating menu item: ", cmd);
		String translation = "***" + cmd + "***";
		try
		{
			translation = JCPLocalizationHandler.getInstance().getString(cmd);
			logger.debug("Found translation: ", translation);
		} catch (MissingResourceException mre)
		{
			logger.error("Could not find translation for: " + cmd);
		}
		JMenuItem mi = null;
		if (isCheckBox)
		{
			mi = new JCheckBoxMenuItem(translation);
			mi.setSelected(isChecked);
		} else
		{
			mi = new JMenuItem(translation);
		}
		logger.debug("Created new menu item...");
		String astr = JCPPropertyHandler.getInstance().getResourceString(cmd + JCPAction.actionSuffix);
		if (astr == null)
		{
			astr = cmd;
		}
		mi.setActionCommand(astr);
		JCPAction action = JCPAction.getAction(jcpPanel, astr);
		if (action != null)
		{
			// sync some action properties with menu
			mi.setEnabled(action.isEnabled());
			mi.addActionListener(action);
			logger.debug("Coupled action to new menu item...");
		} else
		{
			logger.error("Could not find JCPAction class for:" + astr);
			mi.setEnabled(false);
		}
		return mi;
	}

}

