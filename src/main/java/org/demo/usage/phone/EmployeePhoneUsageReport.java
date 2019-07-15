/**
 * 
 */
package org.demo.usage.phone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *   the ( default ) Employee Cell Phone monthly usage report from CSV input data
 */
public class EmployeePhoneUsageReport {

    private static final Logger LOG = LoggerFactory.getLogger( EmployeePhoneUsageReport.class );

    private static final String DEFAULT_INPUTS = "src/test/resource/";

    private String  cellPhoneCsvFile = DEFAULT_INPUTS + "CellPhone.csv";
    
    private String  monthlyPhoneUsageCsvFile = DEFAULT_INPUTS + "CellPhoneUsageByMonth.csv";
    
    /**
     * 	default constructor
     */
    public EmployeePhoneUsageReport() {

    }


    /**
     * command line report launcher
     * @param args  optional input values
     */
    public static void main(String[] args) {

        // TODO add input option handling

    	
    }


    /**
     * @return the cell Phone Csv File name
     */
    private String getCellPhoneCsvFile() {
    	return cellPhoneCsvFile;
    }


    /**
     * @param cellPhoneCsvFile the cell Phone Csv File name to set
     */
    private void setCellPhoneCsvFile( final String cellPhoneCsvFile ) {
        this.cellPhoneCsvFile = StringUtils.trimToEmpty( cellPhoneCsvFile );
    }

    /**
     * @return the monthly Phone Usage Csv File name
     */
    public String getMonthlyPhoneUsageCsvFile() {
    	return monthlyPhoneUsageCsvFile;
	}


    /**
     * @param monthlyPhoneUsageCsvFile the monthly Phone Usage Csv File name to set
     */
    private void setMonthlyPhoneUsageCsvFile( final String monthlyPhoneUsageCsvFile ) {
        this.monthlyPhoneUsageCsvFile = StringUtils.trimToEmpty( monthlyPhoneUsageCsvFile );
    }


}
