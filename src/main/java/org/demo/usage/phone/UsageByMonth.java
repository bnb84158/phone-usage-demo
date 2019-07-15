/**
 * 
 */
package org.demo.usage.phone;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.demo.usage.phone.PhoneTypes.AssignedPhone;
import org.demo.usage.phone.PhoneUsages.MonthlyUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *   Cell Phone usage by month report
 */
public class UsageByMonth {

    private static final Logger LOG = LoggerFactory.getLogger( UsageByMonth.class );

    private final LocalDate reportDate = LocalDate.now();

    private int reportYear = reportDate.getYear();

    private PhoneTypes employeeToPhoneTypes = null;

    private PhoneUsages employeeMonthlyUsages = null;

    /**
     * default constructor
     */
    public UsageByMonth( final int reportYear, final PhoneTypes employeePhones,
                         final PhoneUsages monthlyUsage ) {

        setEmployeeMonthlyUsages( monthlyUsage );
        setEmployeeToPhoneTypes( employeePhones );

        init();
    }

    /**
     * summarize phone usage values
     */
    protected void init(  ) {



    }  // init

	/**
	 * @return the employeeMonthlyUsages
	 */
	private PhoneUsages getEmployeeMonthlyUsages() {
		return employeeMonthlyUsages;
	}

	/**
	 * @param employeeMonthlyUsages the employeeMonthlyUsages to set
	 */
	private void setEmployeeMonthlyUsages(PhoneUsages employeeMonthlyUsages) {
		this.employeeMonthlyUsages = employeeMonthlyUsages;
	}

	/**
	 * @return the employeeToPhoneTypes
	 */
	private PhoneTypes getEmployeeToPhoneTypes() {
		return employeeToPhoneTypes;
	}

	/**
	 * @param employeeToPhoneTypes the employeeToPhoneTypes to set
	 */
	private void setEmployeeToPhoneTypes(PhoneTypes employeeToPhoneTypes) {
		this.employeeToPhoneTypes = employeeToPhoneTypes;
	}
    
    
    
}
