package com.axelor.gst.service;

import java.util.List;
import java.util.Map;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;

public interface InvoiceService {
	public Invoice calculateItems(Invoice invoice);
	public Invoice calculatePartyValues(Invoice invoice);
	public Invoice getShippingAddress(Invoice invoice);
	public void computeReference(Sequence sequence);
	public String createDomainForPartyContact(Invoice invoice);
	public String createDomainForPartyAddress(Invoice invoice);
	public Invoice calculateProductItemsList(Invoice invoice, List<String> productIdList);
}