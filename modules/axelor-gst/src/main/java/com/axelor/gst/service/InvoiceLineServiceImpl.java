package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public InvoiceLine calculateProductValues(InvoiceLine invoiceLine) {
		Product product = invoiceLine.getProduct();

		if (product != null) {
			invoiceLine.setHsbn(product.getHsbn());
			invoiceLine.setGstRate(product.getGstRate());
			invoiceLine.setItem(product.getCategory().getName() + " :[" + product.getCode() + "]");
			invoiceLine.setPrice(product.getSalePrice());
		}
		else {
			invoiceLine.setItem(null);
			invoiceLine.setPrice(new BigDecimal(0));
			invoiceLine.setHsbn(null);
		}
		return invoiceLine;
	}

	@Override
	public InvoiceLine calculateGstValues(Invoice invoice, InvoiceLine invoiceLine) throws Exception {
		
		Company company = invoice.getCompany();
		if (company != null) {
			Address companyAddress = (Address) company.getAddress();
			if (companyAddress != null) {
				State companyState = (State) companyAddress.getState();
				if (companyState != null) {
					Address invoiceAddress = invoice.getInvoiceAddress();
					if (invoiceAddress != null) {
						State invoiceAddressState = invoiceAddress.getState();
						if (invoiceAddressState != null) {
							BigDecimal igst = BigDecimal.ZERO;
							BigDecimal cgst = BigDecimal.ZERO;

							int qty = invoiceLine.getQty();
							BigDecimal price = invoiceLine.getPrice();
							BigDecimal gstRate = invoiceLine.getGstRate();

							BigDecimal netAmount = price.multiply(new BigDecimal(qty));
							invoiceLine.setNetAmount(netAmount);

							companyState = invoice.getCompany().getAddress().getState();
							invoiceAddressState = invoice.getInvoiceAddress().getState();

							if (companyState.equals(invoiceAddressState)) {
								igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
								invoiceLine.setIgst(igst);
							} else {
								cgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
								invoiceLine.setSgst(cgst);
								invoiceLine.setCgst(cgst);
							}
							BigDecimal grossAmount = netAmount.add(igst).add(cgst).add(cgst);
							invoiceLine.setGrossAmount(grossAmount);
						} else {
							throw new Exception("Please select Company");
						}
					} else {
						throw new Exception("Please select invoice address");
					}
				} else {
					throw new Exception("Please enter state in company");
				}
			} else {
				throw new Exception("Please enter company address");
			}
		} else {
			throw new Exception("Please select company");
		}
	return invoiceLine;
	}

}