package com.axelor.gst.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;

public class ProductServiceImpl implements ProductService {

	@Override
	public Map<String,Object> getInvoiceView(Company company, Party party, List<String> productIdList) {
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
		Map<String,Object> invoiceView = ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form")
		.context("invoiceItemsList", invoiceItemsList).context("product_company", company)
		.context("product_party", party).map();
		return invoiceView;
	}

}