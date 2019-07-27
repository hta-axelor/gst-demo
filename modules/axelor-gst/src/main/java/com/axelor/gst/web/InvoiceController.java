package com.axelor.gst.web;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InvoiceController {

	@Inject
	private InvoiceService invoiceService;

	public void setItems(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.calculateItems(invoice);
		response.setValues(invoice);
	}

	public void setPartyValues(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.calculatePartyValues(invoice);
		response.setValues(invoice);
		String contactDomain = invoiceService.createDomainForPartyContact(invoice);
		String addressDomain = invoiceService.createDomainForPartyAddress(invoice);
		response.setAttr("partyContact", "domain", contactDomain);
		response.setAttr("invoiceAddress", "domain", addressDomain);
		response.setAttr("shippingAddress", "domain", addressDomain);
	}

	public void setShippingAddress(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.getShippingAddress(invoice);
		response.setValues(invoice);
	}

	
	public void setReference(ActionRequest request, ActionResponse response) {
		invoiceService.computeReference(request,response);
	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().get("file.upload.dir");
		request.getContext().put("AttachmentPath",attachmentPath + "/");
	}
}