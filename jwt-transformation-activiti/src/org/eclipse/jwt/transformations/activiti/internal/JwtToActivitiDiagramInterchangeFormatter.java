package org.eclipse.jwt.transformations.activiti.internal;

import org.activiti.designer.bpmn2.model.FlowElement;
import org.activiti.designer.util.editor.Bpmn2MemoryModel;
import org.activiti.designer.util.editor.GraphicInfo;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jwt.transformations.activiti.internal.core.TransformationPerformer;
import org.eclipse.jwt.we.model.view.Diagram;
import org.eclipse.jwt.we.model.view.LayoutData;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtToActivitiDiagramInterchangeFormatter {
	private TransformationPerformer performer;
	
	/**
	 * 
	 * @param performer
	 */
	public JwtToActivitiDiagramInterchangeFormatter(TransformationPerformer performer) {
		this.performer = performer;
	}
	
		
	/**
	 * 
	 */
	public void transform(
			final Diagram jwtDiagram, 
			final Bpmn2MemoryModel activitiDiagram) {
		
		EList<LayoutData> layouts = jwtDiagram.getLayoutData();
		
		for(LayoutData layout : layouts) {
			this.transformLayoutDataToGraphicInfo(layout, activitiDiagram);
		}
	}
	
	/**
	 * 
	 */
	private void transformLayoutDataToGraphicInfo(
			final LayoutData layoutData, 
			final Bpmn2MemoryModel activitiDiagram){
		
		Object target = this.performer.findTargetBySource(layoutData.getDescribesElement());
		
		if(target != null) {
			FlowElement flowElement = (FlowElement)target;
			
			GraphicInfo graphicInfo = new GraphicInfo();
			graphicInfo.x = layoutData.getX();
			graphicInfo.y = layoutData.getY();
			graphicInfo.width = layoutData.getWidth();
			graphicInfo.height = layoutData.getHeight();
			graphicInfo.element = flowElement;
		
			activitiDiagram.addGraphicInfo(flowElement.getId(), graphicInfo);
		}
	}
}
