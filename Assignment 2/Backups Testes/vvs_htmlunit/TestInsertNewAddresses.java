package vvs_htmlunit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class TestInsertNewAddresses {

	private WebClient webClient;
	private HtmlPage page;
	
	private String VAT;
    
    private final String ADDRESS1 = "Test Street";
    private final String DOOR1 = "1A";
    private final String POSTAL_CODE1 = "1234-567";
    private final String LOCALITY1 = "Testville";
    
    private final String ADDRESS2 = "Test Street 2";
    private final String DOOR2 = "2A";
    private final String POSTAL_CODE2 = "1234-267";
    private final String LOCALITY2 = "Testville 2";

	@Before
	public void setUp() throws Exception {
	    webClient = new WebClient(BrowserVersion.getDefault());
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(true);

	    page = webClient.getPage("http://localhost:8080/VVS_webappdemo/");
	    assertEquals(200, page.getWebResponse().getStatusCode());
	}
	
	@After
	public void tearDown() throws IOException {
	    webClient.close();
	}

    @Test
    public void insertNewAddressesTest() throws IOException {
    	int initialSize = 0;
    	
		// Get first customer
		WebRequest getRequestAllCust = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/GetAllCustomersPageController"), HttpMethod.GET);

    	HtmlPage allCustPage = webClient.getPage(getRequestAllCust);
         
        HtmlTable custTable = null;
        for (DomElement table : allCustPage.getElementsByTagName("table")) {
            if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Name") && 
                ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Phone") &&
                ((HtmlTable) table).getRow(0).getCell(2).asText().equals("Vat")) {
            	custTable = (HtmlTable) table;
                break;
            }
        }
         
        assertNotNull("Customer table not found", custTable);
         
        VAT = custTable.getRow(1).getCell(2).asText();
        
        // Get first customer address table count before adding
 		WebRequest getRequestCust = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/GetCustomerPageController"), HttpMethod.GET);
 		getRequestCust.setRequestParameters(new ArrayList<>());
 		getRequestCust.getRequestParameters().add(new NameValuePair("vat", VAT));
 		getRequestCust.getRequestParameters().add(new NameValuePair("submit", "Get+Customer"));

     	HtmlPage custPage = webClient.getPage(getRequestCust);
          
        HtmlTable custAddTable = null;
        for (DomElement table : custPage.getElementsByTagName("table")) {
            if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Address") && 
                ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Door") &&
                ((HtmlTable) table).getRow(0).getCell(2).asText().equals("Postal Code") &&
                ((HtmlTable) table).getRow(0).getCell(3).asText().equals("Locality")) {
            	custAddTable = (HtmlTable) table;
                break;
            }
        }
         
        if (custAddTable != null) {
        	initialSize = custAddTable.getRowCount() - 1;
        }
        
        // Add 2 addresses to the created customer
        insertAddress(VAT, ADDRESS1, DOOR1, POSTAL_CODE1, LOCALITY1);
        insertAddress(VAT, ADDRESS2, DOOR2, POSTAL_CODE2, LOCALITY2);
        
        // Get first customer address table count after adding
  		WebRequest getRequestCust2 = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/GetCustomerPageController"), HttpMethod.GET);
  		getRequestCust2.setRequestParameters(new ArrayList<>());
  		getRequestCust2.getRequestParameters().add(new NameValuePair("vat", VAT));
  		getRequestCust2.getRequestParameters().add(new NameValuePair("submit", "Get+Customer"));

      	HtmlPage custPage2 = webClient.getPage(getRequestCust2);
           
        HtmlTable custAddTable2 = null;
        for (DomElement table : custPage2.getElementsByTagName("table")) {
        	if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Address") && 
                ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Door") &&
                ((HtmlTable) table).getRow(0).getCell(2).asText().equals("Postal Code") &&
                ((HtmlTable) table).getRow(0).getCell(3).asText().equals("Locality")) {
            	custAddTable2 = (HtmlTable) table;
                break;
            }
        }
        
        assertNotNull("Address table not found", custAddTable2);
        assertEquals(custAddTable2.getRowCount() - 1, initialSize + 2);
    }

    private void insertAddress(String vat, String address, String door, String postalCode, String locality) throws IOException {
        HtmlAnchor link = page.getAnchorByHref("addAddressToCustomer.html");
        HtmlPage formPage = (HtmlPage) link.openLinkInNewWindow();
        assertEquals("Enter Address", formPage.getTitleText());

        HtmlForm form = formPage.getForms().get(0);
        form.getInputByName("vat").setValueAttribute(vat);
        form.getInputByName("address").setValueAttribute(address);
        form.getInputByName("door").setValueAttribute(door);
        form.getInputByName("postalCode").setValueAttribute(postalCode);
        form.getInputByName("locality").setValueAttribute(locality);

        HtmlPage resultPage = form.getInputByValue("Insert").click();
        
        HtmlTable addTable = null;
        for (DomElement table : resultPage.getElementsByTagName("table")) {
            if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Address") && 
                ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Door") &&
                ((HtmlTable) table).getRow(0).getCell(2).asText().equals("Postal Code") &&
                ((HtmlTable) table).getRow(0).getCell(3).asText().equals("Locality")) {
            	addTable = (HtmlTable) table;
                break;
            }
        }
        
        assertNotNull("Address table not found", addTable);
        
        HtmlTableRow lastRow = addTable.getRows().get(addTable.getRowCount() - 1);
        
        assertEquals(address, lastRow.getCell(0).asText());
        assertEquals(door, lastRow.getCell(1).asText());
        assertEquals(postalCode, lastRow.getCell(2).asText());
        assertEquals(locality, lastRow.getCell(3).asText());
    }
}
