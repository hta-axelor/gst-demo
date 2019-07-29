package com.axelor.gst.web;

import java.util.List;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.repo.GstSequenceRepository;
import com.axelor.gst.service.InvoiceService;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
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
		Invoice invoice = request.getContext().asType(Invoice.class);
		GstSequenceRepository sequenceRepository = Beans.get(GstSequenceRepository.class);
		Sequence sequence = sequenceRepository.all().filter("self.metaModel.fullName = ?1", request.getModel())
				.fetchOne();
		if (sequence == null) {
			response.setError("No Sequence Found, Please enter the sequence");
			return;
		}
		String reference = invoice.getReference();
		if (reference == null) {
			response.setValue("reference", sequence.getNextNumber());
			invoiceService.computeReference(sequence);
		}
	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().get("file.upload.dir");
		request.getContext().put("AttachmentPath", attachmentPath + "/");
	}

	public void createInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<String> productIdList = (List<String>) request.getContext().get("productIds");
		
//		Company company = invoice.getCompany();
//		if (company != null) {
//			Address companyAddress = (Address) company.getAddress();
//			if (companyAddress != null) {
//				State companyState = (State) companyAddress.getState();
//				if (companyState != null) {
//					List<Address> partyAddressList = invoice.getParty().getAddressList();
//					if (!partyAddressList.isEmpty()) {
//						for(Address)
//						if (invoiceAddressState != null) {
//						     invoiceLineService.calculateGstValues(invoice, invoiceLine);
//						} else {
//							response.setError("Please enter state in invoice address");
//						}
//					} else {
//						response.setError("Please enter address in party");
//					}
//				} else {
//					response.setError("Please enter state in company");
//				}
//			} else {
//				response.setError("Please enter company address");
//			}
//		} else {
//			response.setError("Please select company");
//		}

		response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form")
				.context("companyId", invoice.getCompany().getId()).context("partyId", invoice.getParty().getId())
				.context("product_ids", productIdList).map());
	}

	public void setInvoiceDetails(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		List<String> productIdList = (List<String>) request.getContext().get("product_ids");

		CompanyRepository companyRepository = Beans.get(CompanyRepository.class);
		Company company = companyRepository.all().filter("self.id = ?1", request.getContext().get("companyId")).fetchOne();

		PartyRepository partyRepository = Beans.get(PartyRepository.class);
		Party party = partyRepository.all().filter("self.id = ?1", request.getContext().get("partyId")).fetchOne();

		invoice.setIsInvoiceAddress(true);
		invoice.setCompany(company);
		invoice.setParty(party);

		invoice = invoiceService.calculatePartyValues(invoice);
		invoice = invoiceService.getShippingAddress(invoice);

		invoice = invoiceService.calculateProductItemsList(invoice, productIdList);
			
		response.setValue("invoiceItemsList", request.getContext().get("product_invoice_items"));
		
		invoiceService.calculateItems(invoice);
		
		String contactDomain = invoiceService.createDomainForPartyContact(invoice);
		String addressDomain = invoiceService.createDomainForPartyAddress(invoice);
		response.setAttr("partyContact", "domain", contactDomain);
		response.setAttr("invoiceAddress", "domain", addressDomain);
		response.setAttr("shippingAddress", "domain", addressDomain);

		response.setValues(invoice);

	}

}