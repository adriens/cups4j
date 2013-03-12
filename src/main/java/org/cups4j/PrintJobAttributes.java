package org.cups4j;

/**
 * Copyright (C) 2009 Harald Weyhing
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * program; if not, see
 * <http://www.gnu.org/licenses/>.
 */
import java.net.URL;
import java.util.Date;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Holds print job attributes
 *
 *
 */
public class PrintJobAttributes {

    static Logger logger = Logger.getLogger(PrintJobAttributes.class.getName());
    URL jobURL = null;
    URL printerURL = null;
    int jobID = -1;
    JobStateEnum jobState = null;
    String jobName = null;
    String userName = null;
    Date jobCreateTime;
    Date jobCompleteTime;
    int pagesPrinted = 0;
    // Size of the job in kb (this value is rounded up by the IPP server)
    // This value is optional and might not be reported by your IPP server
    int size = -1;

    public URL getJobURL() {
        return jobURL;
    }

    public void setJobURL(URL jobURL) {
        this.jobURL = jobURL;
    }

    public URL getPrinterURL() {
        return printerURL;
    }

    public void setPrinterURL(URL printerURL) {
        this.printerURL = printerURL;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public JobStateEnum getJobState() {
        return jobState;
    }

    public void setJobState(JobStateEnum jobState) {
        this.jobState = jobState;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getJobCreateTime() {
        return jobCreateTime;
    }

    public void setJobCreateTime(Date jobCreateTime) {
        this.jobCreateTime = jobCreateTime;
    }

    public Date getJobCompleteTime() {
        return jobCompleteTime;
    }

    public void setJobCompleteTime(Date jobCompleteTime) {
        this.jobCompleteTime = jobCompleteTime;
    }

    public int getPagesPrinted() {
        return pagesPrinted;
    }

    public void setPagesPrinted(int pagesPrinted) {
        this.pagesPrinted = pagesPrinted;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString(){
        StringBuilder buff = new StringBuilder();
            buff.append("JobName/ID : [").append(getJobName()).append("/").append(getJobID()).append("]\n");
            buff.append("UserName : [").append(getUserName()).append("]\n");
            buff.append("State : [").append(getJobState()).append("]\n");
            buff.append("Job URL : [").append(getJobURL()).append("]\n");
            buff.append("Printer URL : [").append(getPrinterURL()).append("]\n");
            buff.append("Size/Printed : [").append(getSize()).append("/").append(getPagesPrinted()).append("]\n");
            return buff.toString();
    }
    
    public String toString(CupsClient clt){
        return toString(clt, null);
    }
    
    public URL getJobURL(CupsClient clt) throws Exception{
        return clt.getJobAttributes(getJobID()).getJobURL();
    }
    
    public int getPagesPrinted(CupsClient clt) throws Exception {
        return clt.getJobAttributes(getJobID()).getPagesPrinted();
    }
    
    public int getSize(CupsClient clt) throws Exception {
        return clt.getJobAttributes(getJobID()).getSize();
    }
    
    public DateTime getCreateDate(CupsClient clt, DateTimeFormatter dtf) throws Exception{
        return new DateTime(clt.getJobAttributes(getJobID()).getJobCreateTime().getTime());
    }
    public DateTime getCreateDate(CupsClient clt) throws Exception{
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
        return getCreateDate(clt, dtf);
    }
    
    public DateTime getCompleteDate(CupsClient clt, DateTimeFormatter dtf) throws Exception{
        return new DateTime(clt.getJobAttributes(getJobID()).getJobCompleteTime().getTime());
    }
    
    public DateTime getCompleteDate(CupsClient clt) throws Exception{
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();
        return getCompleteDate(clt, dtf);
    }
    
    
    public String toString(CupsClient clt, DateTimeFormatter dtf) {
        try {
            StringBuilder buff = new StringBuilder();
            DateTimeFormatter fmt;
            if (dtf == null) {
                fmt = ISODateTimeFormat.dateTime();
            } else {
                fmt = dtf;
            }

            Date createDate;
            Date completeDate;
            DateTime createTime;
            DateTime completeTime;

            if (clt != null) {
                createDate = clt.getJobAttributes(getJobID()).getJobCreateTime();
                completeDate = clt.getJobAttributes(getJobID()).getJobCompleteTime();

                createTime = new DateTime(createDate.getTime());
                completeTime = new DateTime(completeDate.getTime());

                buff.append("CreateTime : [").append(createTime.toString(fmt)).append("]\n");
                buff.append("CompleteTime : [").append(completeTime.toString(fmt)).append("]\n");
            }

            return buff.toString();
        } catch (Exception ex) {
            logger.error("Not able to get creation and completion Dates fo Job", ex);
            return toString();
        }




    }
}
