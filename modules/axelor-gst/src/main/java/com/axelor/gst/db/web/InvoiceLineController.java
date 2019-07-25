package com.axelor.gst.db.web;

import java.math.BigDecimal;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Product;
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
		Product product = (Product) request.getContext().get("product");
		BigDecimal gstRate = invoiceLineService.calculateGstRate(product);
		String item = invoiceLineService.getItem(product);
		BigDecimal price = invoiceLineService.calculateprice(product);
		response.setValue("gstRate", gstRate);
		response.setValue("item", item);
		response.setValue("price", price);
	}

	public void setAllGst(ActionRequest request, ActionResponse response) {
		try {
			int qty = (int) request.getContext().get("qty");
			BigDecimal price = (BigDecimal) request.getContext().get("price");
			BigDecimal netAmount = invoiceLineService.calculateNetAmount(qty, price);
			response.setValue("netAmount", netAmount);
			
			Company company = (Company) request.getContext().getParent().get("company");
			Address companyAddress = (Address) company.getAddress();
			State companyState = companyAddress.getState();

			Address invoiceAddress = (Address) request.getContext().getParent().get("invoiceAddress");
			State invoiceAddressState = invoiceAddress.getState();

			if (companyState.equals(invoiceAddressState)) {
				BigDecimal igst = invoiceLineService.calculateIgst();
				response.setValue("igst", igst);
			}
			else {
				BigDecimal sgst = invoiceLineService.calculateSgst();
				BigDecimal cgst = invoiceLineService.calculateCgst();
				response.setValue("sgst", sgst);
				response.setValue("cgst", cgst);
			}
			
			BigDecimal grossAmount = invoiceLineService.calculateGrossAmount();
			response.setValue("grossAmount", grossAmount);
		} catch (NullPointerException e) {
			response.setAlert("Missing values cannot calculate IGST");
		}
	}
}