package vvs_mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webapp.persistence.CustomerRowDataGateway;
import webapp.persistence.ICustomerFinder;
import webapp.services.CustomerDTO;
import webapp.services.CustomerService;

// Tests for CustomerService where ICustomerFinder is mocked
// Module A: ICustomerFinder (mocked dependency)
// Module B: CustomerService (service under test)
public class CustomerServiceMockitoTest {

    // Module A (dependency to be mocked)
    @Mock
    private ICustomerFinder mockCustomerFinder;
    
    // Module B (service to be tested)
    private CustomerService customerService;
    
    // Test data
    private static final int VALID_VAT = 197672337;
    private static final String CUSTOMER_NAME = "Test Customer";
    private static final int CUSTOMER_PHONE = 917654321;
    
    @Before
    public void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        
        // Get the singleton instance of CustomerService
        customerService = CustomerService.INSTANCE;
        
        // Inject the mock for testing
        customerService.setCustomerFinder(mockCustomerFinder);
    }
    
    // Test that getCustomerByVat returns correct DTO when customer exists
    @Test
    public void testGetCustomerByVatWhenCustomerExists() throws Exception {
        // Set Mocks
        CustomerRowDataGateway mockCustomerRow = mock(CustomerRowDataGateway.class);
        when(mockCustomerRow.getCustomerId()).thenReturn(1);
        when(mockCustomerRow.getVAT()).thenReturn(VALID_VAT);
        when(mockCustomerRow.getDesignation()).thenReturn(CUSTOMER_NAME);
        when(mockCustomerRow.getPhoneNumber()).thenReturn(CUSTOMER_PHONE);
        
        // Configure the mock CustomerFinder to return our mock customer
        when(mockCustomerFinder.getCustomerByVATNumber(VALID_VAT)).thenReturn(mockCustomerRow);
        
        // Call the method under test
        CustomerDTO customer = customerService.getCustomerByVat(VALID_VAT);
        
        // Verify the results
        assertNotNull("Customer should not be null", customer);
        assertEquals("Customer VAT should match", VALID_VAT, customer.vat);
        assertEquals("Customer name should match", CUSTOMER_NAME, customer.designation);
        assertEquals("Customer phone should match", CUSTOMER_PHONE, customer.phoneNumber);
        
        // Verify that the mock was called as expected
        verify(mockCustomerFinder).getCustomerByVATNumber(VALID_VAT);
    }
} 