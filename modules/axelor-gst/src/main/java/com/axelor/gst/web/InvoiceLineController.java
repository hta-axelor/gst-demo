package com.axelor.gst.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
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
		try {
		invoiceLineService.calculateGstValues(invoice, invoiceLine);
		}
		catch(Exception e) {
			response.setError(e.getMessage());
		}
		response.setValues(invoiceLine);
	}
}