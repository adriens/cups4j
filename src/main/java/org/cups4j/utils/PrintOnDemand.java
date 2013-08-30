/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cups4j.utils;

import java.net.URL;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author salad74
 */
public class PrintOnDemand {
    private static final Logger logger = LoggerFactory.getLogger(PrintOnDemand.class);
    
    private String host;
    private int port;
    private String printerName;
    private String printerUrl;
    
    public PrintOnDemand(){
        
    }
    public PrintOnDemand(String host, int port, String printerName){
        setHost(host);
        setPort(port);
        setPrinterName(printerName);
        setPrinterUrl("http://" + getHost() + ":" + getPort() + "/printers/" + getPrinterName());
    }
        
    public static void main (String[] args){
            
        if(args == null){
            logger.error("No args provided.");
            System.exit(1);
        }
        else{
            if(args.length < 4){
                logger.error("Not enough args provided.");
                System.exit(1);
            }
            else{
                System.out.println("host : <" + args[0] + ">");
                String host = args[0];
                System.out.println("port : <" + args[1] + ">");
                int port = Integer.parseInt(args[1]);
                System.out.println("printer name : <" + args[2] + ">");
                String printerName = args[2];
                System.out.println("document commonVFS url : <" + args[3] + ">");
                String vfsUrl = args[3];
                
                PrintOnDemand p = new PrintOnDemand(host, port, printerName);
                try{
                    p.print(vfsUrl);
                    System.out.println("Print opration success.");
                    System.exit(0);
                }
                catch(Exception ex){
                    System.err.println("Unable to print : " + ex.getMessage());
                    System.exit(1);
                }
            }
        }

    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the printerUrl
     */
    public String getPrinterUrl() {
        return getPrinterName();
    }

    /**
     * @param printerUrl the printerUrl to set
     */
    public void setPrinterUrl(String printerUrl) {
        this.setPrinterName(printerUrl);
    }

    /**
     * @return the printerName
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * @param printerName the printerName to set
     */
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
        
    }
}
