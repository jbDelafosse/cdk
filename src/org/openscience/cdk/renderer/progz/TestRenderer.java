
package org.openscience.cdk.renderer.progz;

import org.openscience.cdk.renderer.ISimpleRenderer2D;
import org.openscience.cdk.renderer.Renderer2D;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.renderer.IRenderer2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.templates.MoleculeFactory;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
//import org.openscience.cdk.interfaces.ISetOfMolecules;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;

public class TestRenderer extends JPanel {

	JFrame frame;
	SwingPainter painter = new SwingPainter();
	StructureDiagramGenerator sdg = new StructureDiagramGenerator();
	protected IChemObjectBuilder builder;
	public void setUp() {
       	builder = DefaultChemObjectBuilder.getInstance();
    }
	public class RendererListner implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			//System.out.println(e); 
			Point2D ptSrc = e.getPoint();
			
			//painter.getGraphics2D(),
			//Point2D ptDst = Java2DRenderer.getCoorFromScreen( ptSrc);
			Point2D ptDst = painter.renderer.getCoorFromScreen( ptSrc);
			System.out.println("Mouse click at " + ptSrc + " real world coordinates: " + ptDst);
			Java2DRenderer.showClosestAtomOrBond(painter.getMolecule(), ptDst);
			
		}
		public void mouseEntered(MouseEvent e) { 	}
		public void mouseExited(MouseEvent e) { 	}
		public void mousePressed(MouseEvent e) { 	}
		public void mouseReleased(MouseEvent e) { 	}
	}
	
	private TestRenderer() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		painter = new SwingPainter();
		//painter.addMouseMotionListener(new TestRendererMouseE());
		//only react on mouse clicks for now
		painter.addMouseListener(new RendererListner());
		setUp();
		
		IMolecule mol;
		//mol = MoleculeFactory.makeAlphaPinene();
		//mol = MoleculeFactory.makeThiazole();
		//mol = MoleculeFactory.makeAlkane(3);
		
		//mol = MoleculeFactory.makeBenzene();
		//mol = makeBenzene();
		//mol = makeWedgeTest();
		mol = makeSWedgeTest();
System.out.println("molecule: " + mol);
		
		
		sdg.setMolecule(mol);
		try {
			sdg.generateCoordinates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		painter.setMolecule(sdg.getMolecule());
		frame.add(painter);
		
		painter.setBackground(Color.WHITE);
		
		
	}

	private void run() {
		frame.setSize(400, 400);

//		frame.show();
		frame.setVisible(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestRenderer prog = new TestRenderer();
		prog.run();
	}
	
	public class SwingPainter extends JComponent {
		private static final long serialVersionUID = 2;

		Renderer2DModel model = new Renderer2DModel();
		
		//IRenderer2D renderer = new Java2DRenderer(model);
		IJava2DRenderer renderer = new Java2DRenderer(model);

		private IMolecule molecule;
		
		Graphics2D graphic;
		
		public void setMolecule(IMolecule molecule) {
			this.molecule = molecule;
		}
		public IMolecule getMolecule() {
			return this.molecule;
		}
		public Graphics2D getGraphics2D() {			
			return this.graphic;
		}
		AffineTransform affinelast = new AffineTransform();
		public void paint(Graphics g) {
			//if (isOpaque()) { //paint background
	        //    g.setColor(getBackground());
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
	       // }
			super.paint(g);
			//System.out.println("Painting molecule..!");
			graphic = (Graphics2D)g;
			model.setZoomFactor(1);
			
			if (!affinelast.equals(graphic.getTransform())) {
				System.out.println("swing changed matrix to:" + graphic.getTransform());
				affinelast = graphic.getTransform();
			}

			renderer.paintMolecule(molecule, (Graphics2D)g, (Rectangle2D)getBounds());
		}
	}
	public IMolecule makeSWedgeTest() {
		IMolecule mol = builder.newMolecule();
		IAtom atomC0 = new Atom("C");
	    atomC0.setID("C0"); atomC0.setHydrogenCount(0);
		

		IAtom atomO1 = new Atom("O");
		atomO1.setID("O1"); atomO1.setHydrogenCount(0);
		
		IAtom atomH0 = new Atom("H");
		atomH0.setID("H0"); atomH0.setHydrogenCount(0);
		IAtom atomH1 = new Atom("H");
		atomH1.setID("H1"); atomH1.setHydrogenCount(0);

	    IBond bondB1 = builder.newBond(atomC0, atomO1);
	    bondB1.setElectronCount(2);
	    IBond bondB2 = builder.newBond(atomC0, atomH0);
	    bondB2.setElectronCount(1);
    bondB2.setStereo(CDKConstants.STEREO_BOND_DOWN);
	    
    IBond bondB3 = builder.newBond(atomC0, atomH1);
    bondB3.setElectronCount(1);

		mol.addAtom(atomC0); 
		mol.addAtom(atomO1);
		mol.addAtom(atomH0);
		mol.addAtom(atomH1);
mol.addBond(bondB1);
 mol.addBond(bondB2);
 mol.addBond(bondB3);

	  return mol;	
	}
	public IMolecule makeWedgeTest() {
		IMolecule mol = builder.newMolecule();
		IAtom atomC0 = new Atom("C");
	    atomC0.setID("C0"); atomC0.setHydrogenCount(3);
		IAtom atomC1 = new Atom("C");
		atomC1.setID("C1"); atomC1.setHydrogenCount(0);
		

		IAtom atomC2 = new Atom("C");
		atomC1.setID("C2"); atomC2.setHydrogenCount(0);
		IAtom atomO1 = new Atom("O");
		atomO1.setID("O1"); atomO1.setHydrogenCount(1);
		IAtom atomO2 = new Atom("O");
		atomO2.setID("O2"); atomO2.setHydrogenCount(0);
		
		IAtom atomH0 = new Atom("H");
		atomH0.setID("H0"); atomH0.setHydrogenCount(0);
		IAtom atomH1 = new Atom("H");
		atomH0.setID("H1"); atomH1.setHydrogenCount(0);
		IAtom atomH2 = new Atom("H");
		atomH0.setID("H2"); atomH2.setHydrogenCount(0);

		IBond bondB0 = builder.newBond(atomC0, atomC1);
	    bondB0.setElectronCount(1);
	    IBond bondB1 = builder.newBond(atomC1, atomO1);
	    bondB1.setElectronCount(1);
	    bondB1.setStereo(CDKConstants.STEREO_BOND_UP);
	    
	    IBond bondB2 = builder.newBond(atomC1, atomC2);
	    bondB2.setElectronCount(1);
	    IBond bondB3 = builder.newBond(atomC2, atomO2);
	    bondB3.setElectronCount(2);
	  
		IBond bondB4 = builder.newBond(atomO1, atomH0);
		bondB4.setElectronCount(1);
		IBond bondB5 = builder.newBond(atomC1, atomH1);
		bondB5.setElectronCount(1);
		bondB5.setStereo(CDKConstants.STEREO_BOND_DOWN);

		IBond bondB6 = builder.newBond(atomC2, atomH2);
		bondB6.setElectronCount(1);

		mol.addAtom(atomC0); mol.addAtom(atomC1);
		mol.addAtom(atomC2); mol.addAtom(atomO1);
	  mol.addAtom(atomO2);
	  mol.addAtom(atomH0);
	  mol.addAtom(atomH1);
	  mol.addAtom(atomH2);
mol.addBond(bondB0); mol.addBond(bondB1);
	  mol.addBond(bondB2); mol.addBond(bondB3);
	  mol.addBond(bondB4);
	  mol.addBond(bondB5);
	  mol.addBond(bondB6);

	  return mol;	
	}
	public IMolecule makeBenzene() {
		  IMolecule benzene = builder.newMolecule();

		  System.out.println("testing..");
		  IAtom atomC0 = new Atom("C");
		    atomC0.setID("C0"); atomC0.setHydrogenCount(1);
		  IAtom atomC1 = new Atom("C");
		    atomC1.setID("C1"); atomC1.setHydrogenCount(1);
		  IAtom atomC2 = new Atom("C");
		    atomC2.setID("C2"); atomC2.setHydrogenCount(1);
		  IAtom atomC3 = new Atom("C");
		    atomC3.setID("C3"); atomC3.setHydrogenCount(1);
		  IAtom atomC4 = new Atom("C"); 
		    atomC4.setID("C4"); atomC4.setHydrogenCount(1);
		  IAtom atomC5 = new Atom("C"); 
		    atomC5.setID("C5"); atomC5.setHydrogenCount(1);

		    atomC0.setFlag(CDKConstants.ISAROMATIC, true);
		    atomC1.setFlag(CDKConstants.ISAROMATIC, true);
		    atomC2.setFlag(CDKConstants.ISAROMATIC, true);
		    atomC3.setFlag(CDKConstants.ISAROMATIC, true);
		    atomC4.setFlag(CDKConstants.ISAROMATIC, true);
		    atomC5.setFlag(CDKConstants.ISAROMATIC, true);

		  IBond bondB0 = builder.newBond(atomC0, atomC1);
		    bondB0.setElectronCount(2);
		  IBond bondB1 = builder.newBond(atomC1, atomC2);
		    bondB1.setElectronCount(2);
		  IBond bondB2 = builder.newBond(atomC2, atomC3);
		    bondB2.setElectronCount(2);
		  IBond bondB3 = builder.newBond(atomC3, atomC4);
		    bondB3.setElectronCount(2);
		  IBond bondB4 = builder.newBond(atomC4, atomC5);
		    bondB4.setElectronCount(2);
		  IBond bondB5 = builder.newBond(atomC0, atomC5);
		    bondB5.setElectronCount(2);

		  IBond bondingSystem = builder.newBond();
		    bondingSystem.setElectronCount(6);
		    bondingSystem.setAtoms(
		      new IAtom[] { atomC0, atomC1, atomC2, 
		                    atomC3, atomC4, atomC5}
		    );

		  benzene.addAtom(atomC0); benzene.addAtom(atomC1);
		  benzene.addAtom(atomC2); benzene.addAtom(atomC3);
		  benzene.addAtom(atomC4); benzene.addAtom(atomC5);

		  benzene.addBond(bondB0); benzene.addBond(bondB1);
		  benzene.addBond(bondB2); benzene.addBond(bondB3);
		  benzene.addBond(bondB4); benzene.addBond(bondB5);
		  benzene.addBond(bondingSystem);

		  return benzene;
		}
}
