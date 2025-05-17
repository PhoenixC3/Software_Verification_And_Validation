package webapp.persistence;

/**
 * Interface for CustomerFinder to enable mocking
 */
public interface ICustomerFinder {
    
    /**
     * Gets a customer by its VAT number
     * 
     * @param vat The VAT number of the customer to search for
     * @return The customer row data gateway
     * @throws PersistenceException When there is an error getting the customer
     * from the database.
     */
    CustomerRowDataGateway getCustomerByVATNumber(int vat) throws PersistenceException;
} 