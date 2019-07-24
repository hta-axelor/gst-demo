package com.axelor.gst.db.web;

import java.util.ArrayList;
import java.util.List;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.actions.ActionView.ActionViewBuilder;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Singleton;

@Singleton
public class ProductController {

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().get("file.upload.dir");
		request.getContext().put("AttachmentPath", "file://" + attachmentPath + "/");
	}

	public void createInvoices(ActionRequest request, ActionResponse response) {
		String ids = (String) request.getContext().get("_ids").toString().replace('[', '(').replace(']', ')');
		ProductRepository productRepository = Beans.get(ProductRepository.class);
		List<Product> productList = productRepository.all().filter("self.id IN " + ids).fetch();
		List<InvoiceLine> invoiceItemsList = new ArrayList<>();
		for(Product p :productList) {
		    InvoiceLine invoiceLine = Beans.get(InvoiceLine.class);
		    invoiceLine.setProduct(p);
		    invoiceLine.setPrice(p.getSalePrice());
		    invoiceLine.setGstRate(p.getGstRate());
		    invoiceItemsList.add(invoiceLine);
		}
		ActionViewBuilder viewBuilder = ActionView.define("Invoice");
		viewBuilder.model(Invoice.class.getName()).add("form", "invoice-form").context("invoiceItemsList", invoiceItemsList);
		response.setView(viewBuilder.map());

	}
}