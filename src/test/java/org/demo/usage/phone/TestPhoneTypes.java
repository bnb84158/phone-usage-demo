/**
 * 
 */
package org.demo.usage.phone;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.demo.usage.phone.PhoneTypes.AssignedPhone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 */
public class TestPhoneTypes {

    private static final Logger LOG = LoggerFactory.getLogger( TestPhoneTypes.class );

    /**
     * default constructor 
     */
    public TestPhoneTypes() {

    }

    /**
     * test CSV import of phone types
     */
    @Test
    public void testImport() {
        final String fileTmp = "src/test/resources/" + "CellPhone.csv";
        final int    nPhones = 9;

        final PhoneTypes reader = new PhoneTypes();
        List<AssignedPhone> found = reader.importPhones( fileTmp );

        final int nFound = ( found != null ? found.size() : 0 );
        LOG.info( "Imported {} (expected {} ) phone types from '{}'.",
                  nFound, nPhones, fileTmp );

        assertTrue( "Expected " + nFound + " == " + nPhones + " for '" + fileTmp + "'.",
                    nFound == nPhones );
    }

}
