package com.axelor.gst.db.web;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InvoiceController {

	@Inject
	private InvoiceService invoiceService;

	public void getItems(ActionRequest request,ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceLine = invoice.getInvoiceItemsList();
		
	}
}
