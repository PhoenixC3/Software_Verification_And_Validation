package vvs_htmlunit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class TestNewSaleDelivery {

	private WebClient webClient;
	private HtmlPage page;
	
	private final String VAT = "257961321";
    private final String NAME = "Test Customer";
    private final String PHONE = "912345678";
    private final String ADDRESS = "Test Street";
    private final String DOOR = "1A";
    private final String POSTAL_CODE = "1234-567";
    private final String LOCALITY = "Testville";

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
		HtmlPage page = webClient.getPage("http://localhost:8080/VVS_webappdemo/RemoveCustomer.jsp");
        HtmlForm form = page.getForms().get(0);
        HtmlTextInput vatInput = form.getInputByName("vat");
        vatInput.setValueAttribute(VAT);

        HtmlSubmitInput submitButton = form.getInputByName("submit");
        submitButton.click();
        
	    webClient.close();
	}

    @Test
    public void createCustomerSaleDeliveryTest() throws IOException {
        //Create a customer
        createCustomer(VAT, NAME, PHONE);
        
        // Add an address to the created customer
        insertAddress(VAT, ADDRESS, DOOR, POSTAL_CODE, LOCALITY);
        
        // Create a sale for the customer
        String saleId = createSale(VAT);
        HtmlAnchor link = page.getAnchorByHref("saleDeliveryVat.html");
        HtmlPage nextPage = (HtmlPage) link.openLinkInNewWindow();
        assertEquals("Enter Name", nextPage.getTitleText());
        
        // Check if the landing page has correct information
        HtmlForm form = nextPage.getForms().get(0);
        form.getInputByName("vat").setValueAttribute(VAT);
        HtmlSubmitInput submitButton = form.getInputByValue("Get Customer");
        HtmlPage customerPage = submitButton.click();

        HtmlTable addressTable = null;
        
        for (DomElement table : customerPage.getElementsByTagName("table")) {
            if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Id") && 
                ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Address")) {
                addressTable = (HtmlTable) table;
                break;
            }
        }
        
        assertNotNull("Address table not found", addressTable);
        
        String addrId = addressTable.getRows().get(1).getCell(0).asText();

        // Add delivery to the sale
        WebRequest postRequest = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/AddSaleDeliveryPageController"), HttpMethod.POST);
        postRequest.setRequestParameters(new ArrayList<>());
        postRequest.getRequestParameters().add(new NameValuePair("addr_id", addrId));
        postRequest.getRequestParameters().add(new NameValuePair("sale_id", saleId));

        HtmlPage resultPage;
        try (final WebClient webClient = new WebClient(BrowserVersion.getDefault())) {
            resultPage = webClient.getPage(postRequest);
        }
        
        // Check the landing page information
        String resultText = resultPage.asText();
        assertTrue(resultText.contains(addrId));
        assertTrue(resultText.contains(saleId));
        
        // Check if the delivery was successfully added
        WebRequest getRequest = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/GetSaleDeliveryPageController"), HttpMethod.GET);
        getRequest.setRequestParameters(new ArrayList<>());
        getRequest.getRequestParameters().add(new NameValuePair("vat", VAT));
        getRequest.getRequestParameters().add(new NameValuePair("submit", "Get+Sales"));

        try (final WebClient webClient = new WebClient(BrowserVersion.getDefault())) {
            HtmlPage deliveryPage = webClient.getPage(getRequest);
            
            // Find the table in the delivery page
            HtmlTable deliveryTable = null;
            for (DomElement table : deliveryPage.getElementsByTagName("table")) {
                if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Id") && 
                    ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Sale Id")) {
                    deliveryTable = (HtmlTable) table;
                    break;
                }
            }
            
            assertNotNull("Delivery table not found", deliveryTable);
            HtmlTableRow lastRow = deliveryTable.getRows().get(deliveryTable.getRowCount() - 1);
            assertEquals(saleId, lastRow.getCell(1).asText());
            assertEquals(addrId, lastRow.getCell(2).asText());
        }
    }

    private void createCustomer(String vat, String name, String phone) throws IOException {
        HtmlAnchor link = page.getAnchorByHref("addCustomer.html");
        HtmlPage formPage = link.click();
        assertEquals("Enter Name", formPage.getTitleText());

        HtmlForm form = formPage.getForms().get(0);
        form.getInputByName("vat").setValueAttribute(vat);
        form.getInputByName("designation").setValueAttribute(name);
        form.getInputByName("phone").setValueAttribute(phone);

        HtmlPage resultPage = form.getInputByValue("Get Customer").click();
        assertEquals("Customer Info", resultPage.getTitleText());

        List<HtmlElement> paragraphs = resultPage.getBody().getElementsByTagName("p");

        boolean foundName = false;
        boolean foundPhone = false;

        for (HtmlElement p : paragraphs) {
        	String text = p.getTextContent().trim();
        	
            if (text.contains("Name:") && text.contains(name)) {
            	foundName = true;
            }
            
            if (text.contains("Contact Info:") && text.contains(phone)) {
            	foundPhone = true;
            }
        }

        assertTrue("Name not displayed correctly", foundName);
        assertTrue("Phone not displayed correctly", foundPhone);
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

    private String createSale(String vat) throws IOException {
        HtmlAnchor link = page.getAnchorByHref("addSale.html");
        HtmlPage formPage = (HtmlPage) link.openLinkInNewWindow();
        assertEquals("New Sale", formPage.getTitleText());

        HtmlForm form = formPage.getForms().get(0);
        form.getInputByName("customerVat").setValueAttribute(vat);

        HtmlPage resultPage = form.getInputByValue("Add Sale").click();
        assertTrue(resultPage.asText().contains(vat));

        WebRequest getRequest = new WebRequest(new java.net.URL("http://localhost:8080/VVS_webappdemo/GetSalePageController"), HttpMethod.GET);
        getRequest.setRequestParameters(new ArrayList<>());
        getRequest.getRequestParameters().add(new NameValuePair("customerVat", vat));
        getRequest.getRequestParameters().add(new NameValuePair("submit", "Get+Sales"));

        try (final WebClient webClient = new WebClient(BrowserVersion.getDefault())) {
            HtmlPage salesPage = webClient.getPage(getRequest);
            
            HtmlTable salesTable = null;
            for (DomElement table : salesPage.getElementsByTagName("table")) {
                if (((HtmlTable) table).getRow(0).getCell(0).asText().equals("Id") && 
                    ((HtmlTable) table).getRow(0).getCell(1).asText().equals("Date") &&
                    ((HtmlTable) table).getRow(0).getCell(2).asText().equals("Total") &&
                    ((HtmlTable) table).getRow(0).getCell(3).asText().equals("Status") &&
                    ((HtmlTable) table).getRow(0).getCell(4).asText().equals("Customer Vat Number")) {
                    salesTable = (HtmlTable) table;
                    break;
                }
            }
            
            assertNotNull("Sales table not found", salesTable);
            HtmlTableRow lastRow = salesTable.getRows().get(salesTable.getRowCount() - 1);
            assertEquals("O", lastRow.getCell(3).asText());
            
            return lastRow.getCell(0).asText();
        }
    }
}
