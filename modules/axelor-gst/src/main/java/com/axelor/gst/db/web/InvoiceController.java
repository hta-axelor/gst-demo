package com.axelor.gst.db.web;

import java.util.stream.Collectors;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.InvoiceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

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
		String contactDomain = invoiceService.createDomainForPartyContact();
		String addressDomain = invoiceService.createDomainForPartyAddress();
		response.setAttr("partyContact", "domain", contactDomain);
		response.setAttr("invoiceAddress", "domain", addressDomain);
		response.setAttr("shippingAddress", "domain", addressDomain);
	}

	public void setShippingAddress(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice = invoiceService.getShippingAddress(invoice);
		response.setValues(invoice);
	}

	@Transactional
	public void setReference(ActionRequest request, ActionResponse response) {
		SequenceRepository sequenceRepository = Beans.get(SequenceRepository.class);
		Sequence sequence = sequenceRepository.all().filter("self.metaModel.fullName = ?1", request.getModel())
				.fetchOne();
		String reference = (String) request.getContext().get("reference");
		if (sequence == null) {
			response.setError("No Sequence Found, Please enter the sequence");
		} else if (reference == null) {
			response.setValue("reference", sequence.getNextNumber());
			String nextNumber = invoiceService.computeReference(sequence);
			sequence.setNextNumber(nextNumber);
			sequenceRepository.save(sequence);
		}
	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		String attachmentPath = AppSettings.get().get("file.upload.dir");
		request.getContext().put("AttachmentPath", "file://" + attachmentPath + "/");
	}
}