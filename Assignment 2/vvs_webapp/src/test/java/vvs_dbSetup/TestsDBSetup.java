package vvs_dbSetup;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import static vvs_dbSetup.DBSetupUtils.*;
import webapp.services.*;

public class TestsDBSetup {
	
private static Destination dataSource;
	
    // the tracker is static because JUnit uses a separate Test instance for every test method.
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
	
    @BeforeAll
    public static void setupClass() {
    	startApplicationDatabaseForTesting();
		dataSource = DriverManagerDestination.with(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
	@BeforeEach
	public void setup() throws SQLException {
		Operation initDBOperations = Operations.sequenceOf(
			  DELETE_ALL
			, INSERT_CUSTOMER_ADDRESS_DATA
			, INSERT_CUSTOMER_SALE_DATA
			);
		
		DbSetup dbSetup = new DbSetup(dataSource, initDBOperations);
		
        // Use the tracker to launch the DBSetup. This will speed-up tests 
		// that do not change the DB. Otherwise, just use dbSetup.launch();
        dbSetupTracker.launchIfNecessary(dbSetup);
		//dbSetup.launch();
	}
	
	// The SUT does not allow to add a new client with an existing VAT
	@Test
	public void addCustomerWithExistingVATTest() {
		// Existing VAT
		int existingVAT = 197672337; // JOSE FARIA
		
		// Exception should be thrown
		assertThrows(ApplicationException.class, () -> {
			CustomerService.INSTANCE.addCustomer(existingVAT, "JOSE FARIA 2", 912345678);
		});
	}
	
	// After the update of a costumer contact, that information should be properly saved
	@Test
	public void updateCustomerContactTest() throws ApplicationException {
		// Existing VAT
		int existingVAT = 197672337; // JOSE FARIA
		
		CustomerService.INSTANCE.updateCustomerPhone(existingVAT, 999999999);
		
		CustomerDTO updatedCustomer = CustomerService.INSTANCE.getCustomerByVat(existingVAT);
		
		assertEquals(updatedCustomer.phoneNumber, 999999999);
	}
	
	// After deleting all costumers, the list of all customers should be empty
	@Test
	public void deleteAllCustomersTest() throws ApplicationException {
		List<CustomerDTO> allCustomers = CustomerService.INSTANCE.getAllCustomers().customers;
		
		for (CustomerDTO currCust : allCustomers) {
			CustomerService.INSTANCE.removeCustomer(currCust.vat);
		}
		
		List<CustomerDTO> allCustomersAfter = CustomerService.INSTANCE.getAllCustomers().customers;
		
		assertEquals(allCustomersAfter.size(), 0);
	}
	
	// After deleting a certain costumer, it’s possible to add it back without lifting exceptions
	@Test
	public void addCustomerBackTest() throws ApplicationException {
		List<CustomerDTO> allCustomers = CustomerService.INSTANCE.getAllCustomers().customers;
		
		int removedVAT = allCustomers.get(0).vat;
		
		CustomerService.INSTANCE.removeCustomer(removedVAT);
		CustomerService.INSTANCE.addCustomer(removedVAT, "weBack", 999999999);
		
		List<CustomerDTO> allCustomersUpdated = CustomerService.INSTANCE.getAllCustomers().customers;
		boolean found = false;
		
		for (CustomerDTO cust : allCustomersUpdated) {
			if (cust.vat == removedVAT) {
				found = true;
			}
		}
		
		assertTrue(found);
	}
	
	// After deleting a certain costumer, its sales should be removed from the database
	@Test
	public void salesRemovedOnCustomerRemoveTest() throws ApplicationException {
		int existingVAT = 197672337; // JOSE FARIA
		
		CustomerService.INSTANCE.removeCustomer(existingVAT);
		
		List<SaleDTO> custSales = SaleService.INSTANCE.getSaleByCustomerVat(existingVAT).sales;
		
		assertEquals(custSales.size(), 0);
	}
	
	// Adding a new sale increases the total number of all sales by one
	@Test
	public void increaseNumberOfSalesTest() throws ApplicationException {
		int initialSize = SaleService.INSTANCE.getAllSales().sales.size();
		
		int existingVAT = 197672337; // JOSE FARIA
		SaleService.INSTANCE.addSale(existingVAT);
		
		assertEquals(SaleService.INSTANCE.getAllSales().sales.size(), initialSize + 1);
	}
	
	// EXTRA SALE TESTS
	
	// After closing a sale, its status should appear as "C"
	@Test
	public void closeSaleTest() throws ApplicationException {
		SaleDTO existingSale = SaleService.INSTANCE.getAllSales().sales.get(0);
		
		SaleService.INSTANCE.updateSale(existingSale.id);
		
		List<SaleDTO> allSalesUpdated = SaleService.INSTANCE.getAllSales().sales;
		
		SaleDTO foundSale = null;
		for (SaleDTO sale : allSalesUpdated) {
			if (sale.id == existingSale.id) {
				foundSale = sale;
			}
		}
		
		assertNotNull(foundSale);
		
		assertEquals(foundSale.statusId, "C");
	}
	
	// Adding two sales to a customer increases the total number of sales associated to the VAT by two
	@Test
	public void addNewSaleTest() throws ApplicationException {
		int existingVAT = 197672337; // JOSE FARIA
		int initialSize = SaleService.INSTANCE.getSaleByCustomerVat(existingVAT).sales.size();
		
		SaleService.INSTANCE.addSale(existingVAT);
		SaleService.INSTANCE.addSale(existingVAT);
		
		List<SaleDTO> allSalesUpdated = SaleService.INSTANCE.getSaleByCustomerVat(existingVAT).sales;
		
		assertEquals(allSalesUpdated.size(), initialSize + 2);
	}
	
	// EXTRA SALE DELIVERIES TESTS
	
	// Adding a sale delivery to a user increases the number of his sale deliveries by one
	@Test
	public void addSaleDeliveryTest() throws ApplicationException {
		int existingVAT = 197672337; // JOSE FARIA
		int initialSize = SaleService.INSTANCE.getSalesDeliveryByVat(existingVAT).sales_delivery.size();
		int saleId = SaleService.INSTANCE.getSaleByCustomerVat(existingVAT).sales.get(0).id;
		int addrId = CustomerService.INSTANCE.getAllAddresses(existingVAT).addrs.get(0).id;
		
		SaleService.INSTANCE.addSaleDelivery(saleId, addrId);
		
		assertEquals(SaleService.INSTANCE.getSalesDeliveryByVat(existingVAT).sales_delivery.size(), initialSize + 1);
	}
	
	// When retrieving sale deliveries for a customer with multiple deliveries, all deliveries should be returned with the correct customer VAT.
	@Test
	public void addSaleDeliveryReturnsCorrectVATTest() throws ApplicationException {
		int existingVAT = 197672337; // JOSE FARIA	
		int saleId = SaleService.INSTANCE.getSaleByCustomerVat(existingVAT).sales.get(0).id;
		int addrId1 = CustomerService.INSTANCE.getAllAddresses(existingVAT).addrs.get(0).id;
		int addrId2 = CustomerService.INSTANCE.getAllAddresses(existingVAT).addrs.get(0).id;
		
		SaleService.INSTANCE.addSaleDelivery(saleId, addrId1);
		SaleService.INSTANCE.addSaleDelivery(saleId, addrId2);
		
		List<SaleDeliveryDTO> allDels = SaleService.INSTANCE.getSalesDeliveryByVat(existingVAT).sales_delivery;
		
		boolean allCorrect = true;
		for (SaleDeliveryDTO del : allDels) {
			if (del.customer_vat != existingVAT) {
				allCorrect = false;
			}
		}
		
		assertTrue(allCorrect);
	}
}