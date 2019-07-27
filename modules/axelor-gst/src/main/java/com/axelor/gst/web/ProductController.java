package com.axelor.gst.web;

import java.util.List;
import java.util.Map;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.service.ProductService;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ProductController {
	
	@Inject
	private ProductService productService;

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().get("file.upload.dir");
		request.getContext().put("AttachmentPath",attachmentPath + "/");
	}

	public void getInvoiceDetails(ActionRequest request, ActionResponse response) {
		List<String> productIdList =  (List<String>) request.getContext().get("_ids");
		response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form-popup").context("productIds", productIdList).param("popup", "true")
				.param("show-toolbar", "false").param("show-confirm", "false").param("popup-save", "false").map());
	}

	public void createInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<String> productIdList = (List<String>) request.getContext().get("productIds");
		Map<String,Object> invoiceView = productService.getInvoiceView(invoice,productIdList);

		response.setView(invoiceView);
		response.setCanClose(true);
	}

}