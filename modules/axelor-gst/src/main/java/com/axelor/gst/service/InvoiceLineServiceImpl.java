package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public InvoiceLine calculateProductValues(InvoiceLine invoiceLine) {
		Product product = invoiceLine.getProduct();
		
		invoiceLine.setGstRate(product.getGstRate());
		invoiceLine.setItem(product.getCategory().getName() + " :[" + product.getCode() + "]");
		invoiceLine.setPrice(product.getSalePrice());
		return invoiceLine;
	}
	
	@Override
	public InvoiceLine calculateGstValues(Invoice invoice,InvoiceLine invoiceLine) {
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;

		int qty = invoiceLine.getQty();
		BigDecimal price = invoiceLine.getPrice();
		BigDecimal gstRate = invoiceLine.getGstRate();

		BigDecimal netAmount = price.multiply(new BigDecimal(qty));
		invoiceLine.setNetAmount(netAmount);
		
		State companyState = invoice.getCompany().getAddress().getState();
		State invoiceAddressState = invoice.getInvoiceAddress().getState();
		
		if (companyState.equals(invoiceAddressState)) {
			igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
			invoiceLine.setIgst(igst);
		} else {
			sgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
			cgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
			invoiceLine.setSgst(sgst);
			invoiceLine.setCgst(cgst);
		}
		BigDecimal grossAmount = netAmount.add(igst).add(sgst).add(cgst);
		invoiceLine.setGrossAmount(grossAmount);

		return invoiceLine;
	}
	
}