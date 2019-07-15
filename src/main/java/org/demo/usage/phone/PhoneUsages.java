/**
 * 
 */
package org.demo.usage.phone;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.demo.usage.phone.PhoneTypes.AssignedPhone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * monthly cell phone usage
 */
public class PhoneUsages {

    private static final Logger LOG = LoggerFactory.getLogger( PhoneUsages.class );

    private static final int MIN_REPORT_YEAR = 1990;

    // current yyyy-MM year & month values in filtered usage rows
    private Set<String> yearMonths = new TreeSet<String>();

    // current employee ids  in filtered usage rows
    private Set<String> employeeIds = new TreeSet<String>();

    private List<MonthlyUsage> monthlyUsage = new ArrayList<MonthlyUsage>();

    /**
     * default constructor
     */
    public PhoneUsages() {

    }

    /**
     * Import CSV monthly cell phone usage valuse
     * @param fileName     the CSV monthly usage file name
     * @param reportYear   the reporting year to filter values
     * @return the filtered monthly usage rows
     */
    public List<MonthlyUsage> importUsage( String fileName, final int reportYear ) {
        List<MonthlyUsage> found = Collections.emptyList();
        final LocalDate maxYear = LocalDate.now();

        fileName = StringUtils.trimToEmpty( fileName );
        if ( StringUtils.isBlank( fileName ) ||
             reportYear > maxYear.getYear() || reportYear < MIN_REPORT_YEAR ) {
            LOG.error( "Missing phone types file '{}' or bad report year {}.",
                       fileName, reportYear );
            return found;
        }

        found = importUsage( fileName );
        final int nFound = ( found != null ? found.size() : 0 );
        if ( nFound <= 0 ) {
            return found;
        }  // nFound

        final Set<String> yearMonthsTmp        = getYearMonths();
        final Set<String> filteredEmployeeIds  = getEmployeeIds();
        List<MonthlyUsage> filtered            = getMonthlyUsage();
        filtered.clear();
        yearMonthsTmp.clear();
        filteredEmployeeIds.clear();

        Integer minMinutes = null;
        Integer maxMinutes = null;
        Double minData  = null;
        Double maxData  = null;
        for ( final MonthlyUsage usage : found ) {
            final Integer billYear   = usage.getBillingYear();
            final String  yearMonth  = usage.getBillingYearMonth();
            final String  employeeId = usage.getEmployeeId();
            final int     minutes    = usage.getTotalMinutes();
            final double  data       = usage.getTotalData();
            if ( billYear == null || reportYear != billYear ) {
                LOG.debug( "Skip monthly usage year '{}' .", billYear, reportYear );
                continue;
            }

            yearMonthsTmp.add( yearMonth );
            filtered.add( usage );
            filteredEmployeeIds.add( employeeId );
            if ( minMinutes == null || minutes < minMinutes ) {
            	minMinutes = minutes;
            } // minMinutes
            if ( maxMinutes == null || minutes > maxMinutes ) {
            	maxMinutes = minutes;
            } // maxMinutes
            if ( minData == null || data < minData ) {
            	minData = data;
            } // minData
            if ( maxData == null || data > maxData ) {
            	maxData = data;
            } // maxData
        } // usage
        LOG.info( "Report year {} has {} usage rows, {} year-month values, {} employees",
                   reportYear, 
                   filtered.size(), yearMonthsTmp.size(), filteredEmployeeIds.size() );
        LOG.info( "Report year {}, filtered year-months {}.", reportYear, yearMonthsTmp );
        LOG.info( "Report year {}, filtered employee ids {}.", reportYear, filteredEmployeeIds );
        LOG.info( "Report year {}, minutes {} - {}, data '{}' - '{}'.", reportYear, 
                   minMinutes, maxMinutes,
                   String.format("%.3f", minData ), String.format("%.3f", maxData ) );
        

        return filtered;
    }


    /**
     * Read employee monthly cell phone minutes & data used from CSV file
     * @param fileName  the CSV phone usage file name
     * @return monthly phone usages
     */
    protected List<MonthlyUsage> importUsage( String fileName ) {
        List<MonthlyUsage> found = Collections.emptyList();

        fileName = StringUtils.trimToEmpty( fileName );
        if ( StringUtils.isBlank( fileName ) ) {
            LOG.error( "Missing phone types file '{}'.", fileName );
            return found;
        }

        try {
            found = new CsvToBeanBuilder( new FileReader( fileName ) )
                        .withType( MonthlyUsage.class ).build().parse();
            
            final int nFound = ( found != null ? found.size() : 0 );
            LOG.debug( "Found {} phone monthly usage rows from '{}'.", nFound, fileName );
        } catch ( Exception ex ) {
            LOG.error( "Read phone monthly usage '{}' error {}.",
                       fileName, ex.getMessage(), ex );
        } // try

        return found;
    }


    /**
	 * @return the yearMonths
	 */
	private Set<String> getYearMonths() {
		return yearMonths;
	}

	/**
	 * @param yearMonths the yearMonths to set
	 */
	private void setYearMonths(Set<String> yearMonths) {
		this.yearMonths = yearMonths;
	}


	/**
	 * @return the monthlyUsage
	 */
	private List<MonthlyUsage> getMonthlyUsage() {
		return monthlyUsage;
	}

	/**
	 * @param monthlyUsage the monthlyUsage to set
	 */
	private void setMonthlyUsage(List<MonthlyUsage> monthlyUsage) {
		this.monthlyUsage = monthlyUsage;
	}


	/**
	 * @return the employeeIds
	 */
	private Set<String> getEmployeeIds() {
		return employeeIds;
	}

	/**
	 * @param employeeIds the employeeIds to set
	 */
	private void setEmployeeIds(Set<String> employeeIds) {
		this.employeeIds = employeeIds;
	}


	public static class MonthlyUsage {

        private static final Logger LOG = LoggerFactory.getLogger( MonthlyUsage.class );

        @CsvBindByName(column = "emplyeeId")
        private String employeeId = "";

        @CsvBindByName(column = "date")
        @CsvDate("MM/dd/yyyy")
        private Date   billingDate = null;

        @CsvBindByName
        private int totalMinutes = 0;

        @CsvBindByName
        private double totalData = 0;


        /**
         * default constructor
         */
        public MonthlyUsage() {
        	
        }

        /**
         * @return the employee Id
         */
        public String getEmployeeId() {
        	return employeeId;
        }

        /**
         * @return the billing Date
         */
        public Date getBillingDate() {
        	return billingDate;
        }

        /**
         * @return the total minutes usage (for month)
         */
        public int getTotalMinutes() {
        	return totalMinutes;
        }

        /**
         * @return the total data usage (for month)
         */
        public double getTotalData() {
        	return totalData;
        }

        /**
         * @return the billing year month formatted as yyyy-MM if a valid date; otherwise null
         */
        public String getBillingYearMonth() {
            final LocalDate dateTmp = getLocalDate();
            return ( dateTmp != null ?
                     dateTmp.format( DateTimeFormatter.ofPattern("yyyy-MM") ) : null );
        }
        
        /**
         * @return phone billing year if known; null otherwise
         */
        public Integer getBillingYear() {
            final LocalDate dateTmp = getLocalDate();
            return ( dateTmp != null ? dateTmp.getYear() : null );
        }

        /**
         * @return phone billing month if known; null otherwise
         */
        public Integer getBillingMonthValue() {
            final LocalDate dateTmp = getLocalDate();
            return ( dateTmp != null ? dateTmp.getMonthValue() : null );
        }

        /**
         * @return the local billing date
         */
        private LocalDate getLocalDate() {
            return ( getBillingDate() != null ?
                     getBillingDate().toInstant().atZone( ZoneId.systemDefault() ).toLocalDate()
                     : null );
        }

        @Override
        public String toString() {
            final Gson gson = new GsonBuilder().create();
            return gson.toJson( this );
        }

    }  // monthly usage

}
