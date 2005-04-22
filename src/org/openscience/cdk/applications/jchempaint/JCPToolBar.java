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

import java.awt.GridLayout;

import javax.swing.JToolBar;

/**
 * The toolbar for JChemPaint
 *
 * @cdk.module jchempaint
 * @author     steinbeck
 * @created    22. April 2005
 */
public class JCPToolBar extends JToolBar
{
	private int orientation = HORIZONTAL;


	/**
	 *  method invocation without parameter causes creation of a horizontal toolbar
	 */
	public JCPToolBar()
	{
		this(HORIZONTAL);
	}


	/**
	 *  constructor method sets the layout for the toolbar
	 *
	 *@param  orientation  (of the toolbar)
	 */
	public JCPToolBar(int orientation)
	{
		checkOrientation(orientation);
		this.orientation = orientation;

		if (orientation == VERTICAL)
		{
			this.setLayout(new GridLayout(1, 10));
		} else
		{
			this.setLayout(new GridLayout(6, 1));
		}
//    addPropertyChangeListener( new PropertyChangeHandler() );
		updateUI();
	}


	/**
	 *  Sets the orientation of the Toolbar
	 *
	 *@param  o
	 */
	public void setOrientation(int o)
	{
		checkOrientation(o);

		if (orientation != o)
		{
			int old = orientation;
			orientation = o;

			if (o == VERTICAL)
			{
				setLayout(new GridLayout(1, 10));
				//setLayout( new FlowLayout() );
			} else
			{
				setLayout(new GridLayout(6, 1));
				//setLayout( new FlowLayout() );
			}
			firePropertyChange("orientation", old, o);
			revalidate();
			repaint();
		}
	}


	/**
	 *  Exception handling for the JCPToolbar constructor.
	 *
	 *@param  orientation
	 */
	private void checkOrientation(int orientation)
	{
		switch (orientation)
		{
						case VERTICAL:
						case HORIZONTAL:
							break;
						default:
							throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
		}
	}

}

