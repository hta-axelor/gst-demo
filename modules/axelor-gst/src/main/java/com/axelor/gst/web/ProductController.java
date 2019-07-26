package com.axelor.gst.web;

import java.util.ArrayList;
import java.util.List;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
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
		request.getContext().put("AttachmentPath",attachmentPath + "/");
	}

	public void getInvoiceDetails(ActionRequest request, ActionResponse response) {
		List<String> productIdList =  (List<String>) request.getContext().get("_ids");
		ActionViewBuilder popupViewBuilder = ActionView.define("Invoice");
		popupViewBuilder.model(Invoice.class.getName()).add("form", "invoice-form-popup").context("productIds", productIdList).param("popup", "true")
				.param("show-toolbar", "false").param("show-confirm", "false").param("popup-save", "false");
		response.setView(popupViewBuilder.map());
	}

	public void createInvoice(ActionRequest request, ActionResponse response) {
		Company company = (Company) request.getContext().get("company");
		Party party = (Party) request.getContext().get("party");
       
		List<String> productIdList = (List<String>) request.getContext().get("productIds");
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
		response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form")
				.context("invoiceItemsList", invoiceItemsList).context("product_company", company)
				.context("product_party", party).map());
		response.setCanClose(true);
	}

}