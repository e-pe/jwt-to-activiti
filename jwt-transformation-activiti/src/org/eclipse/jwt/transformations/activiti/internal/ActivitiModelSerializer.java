package org.eclipse.jwt.transformations.activiti.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.designer.eclipse.bpmn.BpmnParser;
import org.activiti.designer.export.bpmn20.export.BPMN20ExportMarshaller;
import org.activiti.designer.util.editor.Bpmn2MemoryModel;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiModelSerializer {
	
	/**
	 * 
	 * @param model
	 * @param filePath
	 */
	public static void serializeToFile(Bpmn2MemoryModel model, String filePath) {
		ActivitiModelVisualizer visualizer = new ActivitiModelVisualizer();
		visualizer.renderModel(model);
		
		BPMN20ExportMarshaller serializer = new BPMN20ExportMarshaller();
		serializer.setSaveImage(false);
		serializer.marshallDiagram(model, filePath, visualizer.getFeatureProvider());
	}
	
	/**
	 * 
	 * @param model
	 * @param stream
	 * @throws Exception 
	 */
	public static String serializeToString(Bpmn2MemoryModel model) throws Exception {
		File activitiMemoryModelFile = new File("activitiMemoryModel.actr");		
		ActivitiModelSerializer.serializeToFile(model, activitiMemoryModelFile.getAbsolutePath());		
		FileInputStream stream = new FileInputStream(activitiMemoryModelFile);
		
		try { 
			  FileChannel fc = stream.getChannel();
			  MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			  /* Instead of using default, pass in a decoder. */
			  return Charset.defaultCharset().decode(bb).toString();
		  
		} catch (Exception ex) {
			  ex.printStackTrace();
			  
		} finally {
		    stream.close();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param filePath
	 */
	public static Bpmn2MemoryModel deserializeFromFile(String filePath) throws Exception {
		Bpmn2MemoryModel model = new Bpmn2MemoryModel(null, null);
		
		File bpmnFile = new File(filePath);
	    XMLInputFactory xif = XMLInputFactory.newInstance();
		FileInputStream fileStream = new FileInputStream(bpmnFile);
	    InputStreamReader in = new InputStreamReader(fileStream, "UTF-8");
	    XMLStreamReader xtr = xif.createXMLStreamReader(in);
	    
	    BpmnParser parser = new BpmnParser();
	    parser.parseBpmn(xtr, model);
	    
	    return model;
	}
}
