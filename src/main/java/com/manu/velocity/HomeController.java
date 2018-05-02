package com.manu.velocity;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@RestController
public class HomeController {

	@RequestMapping(value = "/" , method = RequestMethod.GET)
	public void getHtml(HttpServletRequest req,HttpServletResponse res) {
		
		// Initialize velocity engine
		VelocityEngine ve = new VelocityEngine();
		
		
		// set class path to locate template
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");                             
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		
		// get vm template
	    Template t = ve.getTemplate("templates/VMDemo.vm");

	    //Put values in context to evaluate in template
	    VelocityContext ctx = new VelocityContext();
	    ctx.put("name", "Manoj");
	    ctx.put("lastName", "Saini");
	    
	    // write output to writer
	    Writer writer = new StringWriter();
	    t.merge(ctx, writer);

	    System.out.println(writer);
	    
	    
	    // Create new document
        Document document = new Document();
        PdfWriter pdfWriter;
        
        // set content type to download file
        res.setContentType("application/pdf pdf");
		res.setHeader("Content-Disposition", "attachment; filename=" +"test.pdf");
		try {
			//pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
			pdfWriter = PdfWriter.getInstance(document, res.getOutputStream());
			
	        document.open();
	        
	        //write html content to pdf file and then output stream
	        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8))); 
	         document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
      
	}
}
