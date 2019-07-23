package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;

public interface InvoiceService {
	public Invoice calculateItems(Invoice invoice);
	public Invoice calculatePartyValues(Invoice invoice);
	public Invoice getShippingAddress(Invoice invoice);
	public String computeReference(Sequence sequence);
	public String createDomainForPartyContact();
	public String createDomainForPartyAddress();
}