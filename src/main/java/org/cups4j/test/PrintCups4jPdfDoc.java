/**
 * Copyright (C) 2009 Sales Adrien
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this program; if not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cups4j.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;

/**
 *
 * @author Adrien SALES
 */
public class PrintCups4jPdfDoc {
    
    static Logger logger = Logger.getLogger(PrintCups4jPdfDoc.class.getName());
    
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String host = "172.16.24.5";
            int port = 631;
            
            CupsClient cc = new CupsClient(host,port);
            //CupsPrinter cp = cc.getDefaultPrinter();
            
            
            // Passing by the url version
            CupsPrinter cp = cc.getPrinter(new URL("http://" + host + ":" + port + "/printers/IM1058"));
            logger.info("Printer url : " + cp.getPrinterURL());
            
            
            cp.setDescription("Some dedicated printer");
            
            InputStream is = new FileInputStream("target/site/cups4j.pdf");
            PrintJob pj = new PrintJob.Builder(is).jobName("cups4j mavenized pdf doc print test.").userName("adrien").build();
            
            
            // set Job attributes
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("", "job-name=cups4j print test");
            attributes.put("job-attributes", "job-more-info=Some more comments about the job.\nblablablabla\nblablabla.");
            attributes.put("job-attributes", "job-originating-user-name=job-originating-user-name");
            //attributes.put("job-attributes", "print-quality:enum:3 media:keyword:iso_a5_148x210mm");
            attributes.put("job-attributes","detailed-name=Detailed description of the job.");
            attributes.put("job-attributes","document-name=cups4j.pdf");
            attributes.put("job-attributes","document-natural-language=EN");
            
            pj.setAttributes(attributes);

            cp.print(pj);
            System.exit(0);            
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            System.exit(1);
        }
    }

}
