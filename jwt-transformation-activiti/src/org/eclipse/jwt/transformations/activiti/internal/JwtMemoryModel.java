package org.eclipse.jwt.transformations.activiti.internal;

import org.eclipse.jwt.meta.model.core.Model;
import org.eclipse.jwt.we.model.view.Diagram;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtMemoryModel {
	private Model model;
	private Diagram diagramInterchange;
	
	/**
	 * 
	 * @param model
	 * @param diagramInterchange
	 */
	public JwtMemoryModel(Model model, Diagram diagramInterchange) {
		this.model = model;
		this.diagramInterchange = diagramInterchange;
	}
	
	/**
	 * 
	 * @return
	 */
	public Model getModel(){
		return this.model;
	}
	
	/**
	 * 
	 * @return
	 */
	public Diagram getDiagramInterchange(){
		return this.diagramInterchange;
	}
}
