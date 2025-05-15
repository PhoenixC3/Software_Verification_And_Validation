package vvs_htmlunit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class TestInsertNewCustomers {

	private WebClient webClient;
	private HtmlPage page;
	
	private final String VAT1 = "257961321";
    private final String NAME1 = "Test Customer 1";
    private final String PHONE1 = "912345678";
    
    private final String VAT2 = "246163399";
    private final String NAME2 = "Test Customer 2";
    private final String PHONE2 = "999999999";

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
        vatInput.setValueAttribute(VAT1);

        HtmlSubmitInput submitButton = form.getInputByName("submit");
        submitButton.click();
        
        HtmlPage page2 = webClient.getPage("http://localhost:8080/VVS_webappdemo/RemoveCustomer.jsp");
        HtmlForm form2 = page2.getForms().get(0);
        HtmlTextInput vatInput2 = form2.getInputByName("vat");
        vatInput2.setValueAttribute(VAT2);

        HtmlSubmitInput submitButton2 = form2.getInputByName("submit");
        submitButton2.click();
        
	    webClient.close();
	}

    @Test
    public void createCustomerSaleDeliveryTest() throws IOException {
        createCustomer(VAT1, NAME1, PHONE1);
        createCustomer(VAT2, NAME2, PHONE2);
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
}
