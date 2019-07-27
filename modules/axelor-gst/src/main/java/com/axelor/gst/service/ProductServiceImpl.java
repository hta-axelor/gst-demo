package com.axelor.gst.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;

public class ProductServiceImpl implements ProductService {

	@Override
	public Map<String, Object> getInvoiceView(Invoice invoice, List<String> productIdList) {
		
		Company company = invoice.getCompany();
		Party party = invoice.getParty();
		Contact partyContact = invoice.getPartyContact();
		Address invoiceAddress = invoice.getInvoiceAddress();
		Address shippingAddress = invoice.getShippingAddress();
		Boolean isInvoiceAddress = invoice.getIsInvoiceAddress();

		ProductRepository productRepository = Beans.get(ProductRepository.class);
		List<Product> productList = productRepository.all().filter("self.id IN ?1", productIdList).fetch();
		List<InvoiceLine> invoiceItemsList = new ArrayList<>();
		for (Product p : productList) {
			InvoiceLine invoiceLine = Beans.get(InvoiceLine.class);
			invoiceLine.setProduct(p);
			invoiceLine.setPrice(p.getSalePrice());
			invoiceLine.setGstRate(p.getGstRate());
			invoiceItemsList.add(invoiceLine);
		}
		
		
		Map<String, Object> invoiceView = ActionView.define("Invoice").model(Invoice.class.getName())
				.add("form", "invoice-form").context("invoiceItemsList", invoiceItemsList)
				.context("product_company", company).context("product_party", party)
				.context("product_partyContact", partyContact).context("product_invoiceAddress", invoiceAddress)
				.context("product_shippingAddress", shippingAddress)
				.context("product_isInvoiceAddress", isInvoiceAddress).map();
		return invoiceView;
	}

}