package com.axelor.gst.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InvoiceLineController {

	@Inject
	private InvoiceLineService invoiceLineService;

	public void setProductValues(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
		invoiceLine = invoiceLineService.calculateProductValues(invoiceLine);
		response.setValues(invoiceLine);
	}

	public void setAllGst(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);

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
						     invoiceLineService.calculateGstValues(invoice, invoiceLine);
						} else {
							response.setError("Please enter state in invoice address");
						}
					} else {
						response.setError("Please select invoice address");
					}
				} else {
					response.setError("Please enter state in company");
				}
			} else {
				response.setError("Please enter company address");
			}
		} else {
			response.setError("Please select company");
		}
		response.setValues(invoiceLine);
	}
}