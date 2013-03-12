/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cups4j.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import org.apache.log4j.Logger;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJobAttributes;
import org.cups4j.WhichJobsEnum;


/**
 * An utility class to perform some reporting tasks. For now, it can :
 * 
 * <ul>
 * <li>Report anu cups server jobs and write output in a file, with Textile
 * Markup language.</li>
 * </ul>
 * @author Adrien SALES (Adrien.Sales@gmail.com)
 */
public class PrinterList {
    
    public static final String WHICH_ALL = "ALL";
public static final String WHICH_COMPLETED = "COMPLETED";
public static final String WHICH_NOT_COMPLETED = "NOT_COMPLETED";
    
    static Logger logger = Logger.getLogger(PrinterList.class.getName());
    /**
     * Reports Jobs and printers in the file, using <a href="http://en.wikipedia.org/wiki/Textile_(markup_language)">textile Markup Language</a>.
     * useful for example to report them in <a href="http://www.redmine.org/">redmine</a>
     * @param file The file we want to write report into
     * @param host The hostname/ip of the server hosting cups server
     * @param port The port of the server on which cups server is listening
     * @param which Filter Jobs you want to report, see WhichJobsEnum
     * @param user Filter jobs of mentioned user. If set to null, no filter is applied
     * @param myJobs boolean only jobs for requesting user or all jobs for this printer?
     * @throws Exception 
     */
    public void reportToTextile(String file, String host, int port, WhichJobsEnum which, String user, boolean myJobs) throws Exception{
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            
            BufferedWriter br = new BufferedWriter(out);
            PrinterList print = new PrinterList();
            print.reportToTextile(br, host, port, which, user, myJobs);
    }
    public void reportToTextile(String file, String host, String port, String which, String user, String myJobs) throws Exception{
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            
            BufferedWriter br = new BufferedWriter(out);
            PrinterList print = new PrinterList();
            
            int thePort;
        if(port == null){
            thePort = CupsClient.DEFAULT_PORT;
        }
        else{
            if(port.length() == 0){
                thePort = CupsClient.DEFAULT_PORT;
            }
            else{
                try{
                    thePort = Integer.parseInt(port);
                }
                catch(NumberFormatException e){
                    logger.error("Unable to parse provided port <" + port + "> : " + e.getMessage());
                    throw new Exception("Unable to parse provided port <" + port + "> : " + e.getMessage());
                }
            }
        }
        
        String theHost;
        if(host == null){
            theHost = CupsClient.DEFAULT_HOST;
        }
        else{
            theHost= host;
        }
        WhichJobsEnum theWhich;
        if(which.equalsIgnoreCase(WHICH_ALL)){
            theWhich = WhichJobsEnum.ALL;
        }
        else if(which.equalsIgnoreCase(WHICH_COMPLETED)){
            theWhich = WhichJobsEnum.COMPLETED;
        }
        else if(which.equalsIgnoreCase(WHICH_NOT_COMPLETED)){
            theWhich = WhichJobsEnum.NOT_COMPLETED;
        }
        else if(which.isEmpty()){
            theWhich = WhichJobsEnum.ALL;
        }
        else{
            throw (new Exception("Which option <" + which + "> not supported. Possible values are : ALL,WHICH_COMPLETED or WHICH_NOT_COMPLETED"));
        }
        
        String theUser = null;
        if(user != null){
            if(user.length() == 0){
                theUser = null;
            }
            
        }
        else{
            theUser= null;
        }
        boolean theJobs;
        if(myJobs == null){
            theJobs = false;
        }
        else{
            if(myJobs.equalsIgnoreCase("true")){
                theJobs = true;
            }
            else{
                theJobs = false;
            }
        }
        
        
        print.reportToTextile(br, theHost, thePort, theWhich, theUser, theJobs);
    }
    
    
    
    
    public void reportToTextile(BufferedWriter br, String host, int port, WhichJobsEnum which, String user, boolean myJobs) throws Exception{
        String theHost;
        
                
        if(host == null ){
            theHost = "localhost";
            logger.warn("no host provided, will use <localhost> instead.");
        }
        else{
            theHost = host;
        }
        try{
            br.write("h1. Cups report\n");
                      
            
            br.write("* Management console : http://" + host + ":" + port + "\n");
            br.write("* host : " + host + "\n");
            br.write("* port : " + port + "\n");
            if(user == null){
                br.write("* user : All users (no filter)\n");
            }
            else{
                br.write("* user : " + user + "\n");
            }
            
            
            CupsClient clt = new CupsClient(host, port);
            // the client is ok
            logger.info("Successfully created CUPS client.");
            List<CupsPrinter> printers = clt.getPrinters();
            for(CupsPrinter lPrint:printers){
                logger.info("printer found : <" + lPrint.getName() + "> : <" + lPrint.getPrinterURL() + "> <" + lPrint.getDescription() + ">");
                br.write("\nh2. " + lPrint.getName() + "\n");
                //br.write("printer found : <" + lPrint.getName() + "> : <" + lPrint.getPrinterURL() + "> <" + lPrint.getDescription() + ">");
                
                List<PrintJobAttributes> attributes = lPrint.getJobs(which, user, myJobs);
                for(PrintJobAttributes attr:attributes){
                    logger.info(attr.toString(clt));
                    br.write("\nh3. " + attr.getJobName() + "\n\n");
                    br.write("|_. Parameter |_. Value |\n");
                    br.write("|Job Name |" + attr.getJobName() + "|\n");
                    br.write("|UserName |" + attr.getUserName() + "|\n");
                    br.write("|Job ID |" + attr.getJobID() + "|\n");
                    br.write("|Job State |" + attr.getJobState() + "|\n");
                    br.write("|Job Url |" + attr.getJobURL(clt) + "|\n");
                    br.write("|PagesPrinted |" + attr.getPagesPrinted(clt) + "|\n");
                    br.write("|Printer URL |" + attr.getPrinterURL() + "|\n");
                    br.write("|Size |" + attr.getSize(clt) + "|\n");
                    br.write("|Create date |" + attr.getCreateDate(clt) + "|\n");
                    br.write("|Complete date |" + attr.getCompleteDate(clt) + "|\n");
                    br.write("\n\n");
                    //br.write("\nh3. Job name : " + attr.getJobName() + "\n");
                    //br.write(attr.toString(clt));
                }
             br.flush();
            }
            br.flush();
            br.close();
        }
        catch(Exception ex){
            logger.error("Unable to report CUPS server : " + ex.getMessage() + "\nBye.");
            throw ex;
        }

    }
    
    /**
     * Main to be used as a simple Java command. Currently used from within ant
     * but not as a ant task as i first intend to do it because of jaxb issues.
     * @param args 
     */
    public static void main(String[] args) {
       
        String host = args[0];
        String port = args[1];
        String which = args[2];
        String user = args[3];
        String myjobs = args[4];
        String filename = args[5];
        
        String theUser;
        if(user.equalsIgnoreCase("all")){
            theUser = null;
        }
        else{
            theUser = user;
        }
            
        try{

            PrinterList print = new PrinterList();
            print.reportToTextile(filename, host, port, which, theUser, myjobs);
            System.exit(0);
        }
        catch(Exception ex){
            logger.error(ex);
        }
    }
    
    
    
}
