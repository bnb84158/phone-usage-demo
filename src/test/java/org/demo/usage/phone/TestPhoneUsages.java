/**
 * 
 */
package org.demo.usage.phone;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.demo.usage.phone.PhoneUsages.MonthlyUsage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for monthly phone usage
 */
public class TestPhoneUsages {

    private static final Logger LOG = LoggerFactory.getLogger( TestPhoneUsages.class );


    /**
     * default constructor
     */
    public TestPhoneUsages() {

    }

    /**
     * check CSV import of monthly phone usage
     */
    @Test
    public void testImportUsage() {
        final String fileTmp = "src/test/resources/" + "CellPhoneUsageByMonth.csv";
        final int    nRows = 1000;

        final PhoneUsages reader = new PhoneUsages();
        final List<MonthlyUsage> found = reader.importUsage( fileTmp );

        final int nFound = ( found != null ? found.size() : 0 );
        LOG.info( "Imported {} ( expected {} ) monthly phone usage rows from '{}'.",
                  nFound, nRows, fileTmp );

        assertTrue( "Expected " + nFound + " == " + nRows + " for '" + fileTmp + "'.",
                    nFound == nRows );
    } // import usage

    /**
     * check CSV import year filtering
     */
    @Test
    public void testFilteredUsage() {
        final String fileTmp = "src/test/resources/" + "CellPhoneUsageByMonth.csv";
        final int    minRows = 10;
        final int    filterYear = 2018;

        final PhoneUsages reader = new PhoneUsages();
        final List<MonthlyUsage> found = reader.importUsage( fileTmp, filterYear );

        final int nFound = ( found != null ? found.size() : 0 );
        LOG.info( "Imported {} ( expected {} ) monthly phone usage rows from '{}'.",
                  nFound, minRows, fileTmp );

        assertTrue( "Expected " + nFound + " >== " + minRows + " for '" + fileTmp + "'.",
                    nFound >= minRows );
    } // import usage

    
    
    
    
}
