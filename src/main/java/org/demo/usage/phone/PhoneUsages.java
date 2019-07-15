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

    private Set<String> yearMonths = new TreeSet<String>();

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

        final Set<String> yearMonthsTmp = yearMonths;
        List<MonthlyUsage> filtered = monthlyUsage;
        filtered.clear();
        yearMonthsTmp.clear();

        for ( final MonthlyUsage usage : found ) {
            final Integer billYear = usage.getBillingYear();
            final String  yearMonth = usage.getBillingYearMonth();
            if ( billYear == null || reportYear != billYear ) {
                LOG.debug( "Skip monthly usage year '{}' .", billYear, reportYear );
                continue;
            }

            yearMonthsTmp.add( yearMonth );
            filtered.add( usage );
        } // usage
        LOG.debug( "Filtered to {} usage rows, {} year-month values, report year {}.", 
                    filtered.size(), yearMonthsTmp.size(), reportYear );

        return found;
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


    public static class MonthlyUsage {

        private static final Logger LOG = LoggerFactory.getLogger( MonthlyUsage.class );

        @CsvBindByName
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
