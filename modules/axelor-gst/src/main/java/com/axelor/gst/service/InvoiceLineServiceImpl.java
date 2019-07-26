package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public BigDecimal calculateGstRate(Product product) {
		BigDecimal gstRate = product.getGstRate();
		return gstRate;
	}

	@Override
	public String getItem(Product product) {
		String item = product.getCategory().getName();
		return item;
	}

	@Override
	public BigDecimal calculateprice(Product product) {
		BigDecimal price = product.getSalePrice();
		return price;
	}

	@Override
	public void calculateGstValues(ActionRequest request, ActionResponse response) {
		BigDecimal igst = BigDecimal.ZERO;
		BigDecimal sgst = BigDecimal.ZERO;
		BigDecimal cgst = BigDecimal.ZERO;

		int qty = (int) request.getContext().get("qty");
		BigDecimal price = (BigDecimal) request.getContext().get("price");
		BigDecimal gstRate = (BigDecimal) request.getContext().get("gstRate");

		BigDecimal netAmount = price.multiply(new BigDecimal(qty));
		response.setValue("netAmount", netAmount);

		Company company = (Company) request.getContext().getParent().get("company");
		if (company != null) {
			Address companyAddress = (Address) company.getAddress();
			if (companyAddress != null) {
				State companyState = (State) companyAddress.getState();
				if (companyState != null) {
					Address invoiceAddress = (Address) request.getContext().getParent().get("invoiceAddress");
					if (invoiceAddress != null) {
						State invoiceAddressState = invoiceAddress.getState();
						if (invoiceAddressState != null) {
							if (companyState.equals(invoiceAddressState)) {
								igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
								response.setValue("igst", igst);
							} else {
								sgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
								cgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
								response.setValue("sgst", sgst);
								response.setValue("cgst", cgst);
							}
							BigDecimal grossAmount = netAmount.add(igst).add(sgst).add(cgst);
							response.setValue("grossAmount", grossAmount);
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
	}
}