package com.axelor.gst.web;

import java.math.BigDecimal;
import com.axelor.gst.db.Product;
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
		Product product = (Product) request.getContext().get("product");
		BigDecimal gstRate = invoiceLineService.calculateGstRate(product);
		String item = invoiceLineService.getItem(product);
		BigDecimal price = invoiceLineService.calculateprice(product);
		response.setValue("gstRate", gstRate);
		response.setValue("item", item);
		response.setValue("price", price);
	}

	public void setAllGst(ActionRequest request, ActionResponse response) {
		invoiceLineService.calculateGstValues(request,response);	
	}
}