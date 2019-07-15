/**
 * 
 */
package org.demo.usage.phone;

import java.io.FileReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

/**
 *  Known Cell Phone types
 */
public class PhoneTypes {
	
    private static final Logger LOG = LoggerFactory.getLogger( PhoneTypes.class );

    private final Map<String,AssignedPhone> employeeIdToPhone = new TreeMap<String,AssignedPhone>();
    /**
     * default constructor
     */
    public PhoneTypes() {

    }

    protected void init( final Collection<AssignedPhone> employeePhones ) {

        final Map<String,AssignedPhone> knownTypes = getEmployeeIdToPhone();
        for ( final AssignedPhone phoneType : employeePhones ) {
            final String id = phoneType.getEmployeeId();
            AssignedPhone phone = knownTypes.get(id);

            if ( phone != null ) {
                LOG.warn( "Duplicate phone per employee id '{}'.",  id  );
            } else {
                knownTypes.put( id, phoneType );
            } // phone
            
        } // phoneType

    } // init


    /**
     * Read employee assigned phones from CSV file
     * @param fileName  the phone types CSV file
     * @return phone types found
     */
    @SuppressWarnings("unchecked")
    public List<AssignedPhone> importPhones( String fileName ) {
        List<AssignedPhone> found = Collections.emptyList();

        fileName = StringUtils.trimToEmpty( fileName );
        if ( StringUtils.isBlank( fileName ) ) {
            LOG.error( "Missing phone types file '{}'.", fileName );
            return found;
        }

        try {
            found = new CsvToBeanBuilder( new FileReader( fileName ) )
                        .withType( AssignedPhone.class ).build().parse();

            final int nFound = ( found != null ? found.size() : 0 );
            LOG.debug( "Found {} phone types rows from '{}'.", nFound, fileName );
        } catch ( Exception ex ) {
            LOG.error( "Read phone types '{}' error {}.",
                       fileName, ex.getMessage(), ex );
        } // try

        init( found );

        return found;
    }

    /**
     * @param employeeId  the employee id to check
     * @return the assigned phone details, if found; otherwise null
     */
    public AssignedPhone getPhoneById( final String employeeId ) {
        final String key = StringUtils.trimToEmpty( employeeId );
        if ( StringUtils.isBlank( key ) ) {
            LOG.error( "Blank employee id '{}'.", employeeId );
        }
        return getEmployeeIdToPhone().get( key );	
    }

    /**
     * @return the employeeIdToPhone map
     */
    private Map<String,AssignedPhone> getEmployeeIdToPhone() {
    	return employeeIdToPhone;
    }


	public static class AssignedPhone {

        private static final Logger LOG = LoggerFactory.getLogger( AssignedPhone.class );

        @CsvBindByName
        private String employeeId = "";

        @CsvBindByName
        private String employeeName = "";

        @CsvBindByName
        private String model = "";

        @CsvBindByName
        @CsvDate("yyyyMMdd")
        private Date   purchaseDate = null;

        /**
         * default constructor
         */
        public AssignedPhone() {
        	
        }

        /**
         * @return the employee Id
         */
        public String getEmployeeId() {
        	return employeeId;
        }

        /**
         * @return the employee name
         */
        public String getEmployeeName() {
        	return employeeName;
        }

        /**
         * @return the phone model
         */
        public String getModel() {
        	return model;
        }

        /**
         * @return the phone purchase date
         */
        public Date getPurchaseDate() {
        	return purchaseDate;
        }

        /**
         * @param employeeId the employee Id to set
         */
        private void setEmployeeId( final String employeeId ) {
        	this.employeeId = StringUtils.trimToEmpty( employeeId );
        }

        /**
         * @param employeeName the employee Name to set
         */
        private void setEmployeeName( final String employeeName ) {
        	this.employeeName = StringUtils.trimToEmpty( employeeName );
        }

        /**
         * @param model the phone model to set
         */
        private void setModel( final String model ) {
        	this.model = StringUtils.trimToEmpty( model );
        }

        /**
         * @param purchaseDate the phone purchase Date to set
         */
        private void setPurchaseDate( final Date purchaseDate ) {
        	this.purchaseDate = purchaseDate;
        }

        @Override
        public String toString() {
            final Gson gson = new GsonBuilder().create();
            return gson.toJson( this );
        }

        
    }  // assigned phone

}
