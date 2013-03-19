package org.cups4j;

/**
 * Copyright (C) 2009 Harald Weyhing
 * 
 * This file is part of Cups4J. Cups4J is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Cups4J is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Cups4J. If
 * not, see <http://www.gnu.org/licenses/>.
 */
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Print job class
 * 
 * 
 */
public class PrintJob {
  private InputStream document;
  private int copies;
  private String pageRanges;
  private String userName;
  private String jobName;
  private boolean duplex = false;

  private Map<String, String> attributes;

  /**
   * <p>
   * Builds PrintJob objects like so:
   * </p>
   * <p>
   * PrintJob printJob = new
   * PrintJob.Builder(document).jobName("jobXY").userName
   * ("harald").copies(2).build();
   * </p>
   * <p>
   * documents are supplied as byte[] or as InputStream
   * </p>
   */
  public static class Builder {
    private static final Logger logger = LoggerFactory.getLogger(Builder.class);
    private InputStream document;
    private int copies = 1;
    private String pageRanges = null;;
    private String userName = null;
    private String jobName = null;
    private boolean duplex = false;
    private Map<String, String> attributes;

    /**
     * Constructor
     * 
     * @param byte[] document
     * 
     */
    public Builder(byte[] document) {
      this.document = new ByteArrayInputStream(document);
    }
    
    /**
     * Constructor to print a <b>single file</b>, relying on commonVFS
     * See <a href="http://commons.apache.org/proper/commons-vfs//filesystems.html">
     * Common-VFS</a> for examples.
     * 
     * @param String The commonVFS URI of the file to print
     * 
     */
    public Builder(String commonVFSURI) throws Exception{
      //this.document = new ByteArrayInputStream(document);
        try{
            logger.debug("Trying to build FileSystem Manager ...");
            FileSystemManager fsManager = VFS.getManager();
            logger.debug("FileSystem Manager built.");
            logger.debug("Resolving fileobject <" + commonVFSURI + ">...");
            FileObject fo = fsManager.resolveFile(commonVFSURI);
            logger.debug("Fileobject successfully built.");
           this.document = fo.getContent().getInputStream();
                /*
                 * int nbChildren = fo.getChildren().length;
                if(nbChildren > 1){
                    String msg = "Cannot deal with multiple childs. Provide url that matches single file";
                    logger.warn(msg);
                    throw new Exception(msg);
                }
                else{
                    logger.debug("Single Child found : <" + fo.getURL() + ">");
                    //this.document = fo.getContent().
                    logger.debug("Feeding stream in document ...");
                    this.document = fo.getContent().getInputStream();
                    logger.debug("Document feed with stream.");
                }
                * */
        
           }
        catch(FileSystemException ex){
            logger.error("Unable to mount Virtual Filesystem Manager : " + ex.getMessage());
            throw ex;
        }
        
        }

    /**
     * Constructor
     * 
     * @param InputStream
     *          document
     * 
     */
    public Builder(InputStream document) {
      this.document = document;
    }

    /**
     * Number of copies - 0 and 1 are both treated as one copy
     * 
     * @param copies
     * @return Builder
     */
    public Builder copies(int copies) {
      this.copies = copies;
      return this;
    }

    /**
     * Page ranges
     * 
     * @pageRanges pageRanges 1-3, 5, 8, 10-13
     * @return Builder
     */
    public Builder pageRanges(String pageRanges) {
      this.pageRanges = pageRanges;
      return this;
    }

    /**
     * User name
     * 
     * @param userName
     * @return Builder
     */
    public Builder userName(String userName) {
      this.userName = userName;
      return this;
    }

    /**
     * Job name
     * 
     * @param jobName
     * @return Builder
     */
    public Builder jobName(String jobName) {
      this.jobName = jobName;
      return this;
    }

    /**
     * Duplex mode
     * 
     * @param duplex
     * @return Builder
     */
    public Builder duplex(boolean duplex) {
      this.duplex = duplex;
      return this;
    }

    /**
     * Additional attributes for the print operation and the print job
     * 
     * @param attributes
     *          provide operation attributes and/or a String of job-attributes
     *          <p>
     *          job attributes are sperated by "#"
     *          </p>
     * 
     *          <p>
     *          example:
     *          </p>
     *          <p>
     *          attributes.put("compression","none");
     *          </p>
     *          <p>
     *          attributes.put("job-attributes",
     *          "print-quality:enum:3#sheet-collate:keyword:collated#sides:keyword:two-sided-long-edge"
     *          );
     *          </p>
     *          <p>
     *          -> take a look config/ippclient/list-of-attributes.xml for more
     *          information
     *          </p>
     * 
     * @return Builder
     */
    public Builder attributes(Map<String, String> attributes) {
      this.attributes = attributes;
      return this;
    }

    /**
     * Builds the PrintJob object.
     * 
     * @return PrintJob
     */
    public PrintJob build() {
      return new PrintJob(this);
    }
  }

  PrintJob(Builder builder) {
    this.document = builder.document;
    this.jobName = builder.jobName;
    this.copies = builder.copies;
    this.pageRanges = builder.pageRanges;
    this.userName = builder.userName;
    this.duplex = builder.duplex;
    this.attributes = builder.attributes;

  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public InputStream getDocument() {
    return document;
  }

  public int getCopies() {
    return copies;
  }

  public String getPageRanges() {
    return pageRanges;
  }

  public String getUserName() {
    return userName;
  }

  public boolean isDuplex() {
    return duplex;
  }

  public void setAttributes(Map<String, String> printJobAttributes) {
    this.attributes = printJobAttributes;
  }

  public String getJobName() {
    return jobName;
  }

}
