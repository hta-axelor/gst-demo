package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public interface InvoiceService {
	public Invoice calculateItems(Invoice invoice);
	public Invoice calculatePartyValues(Invoice invoice);
	public Invoice getShippingAddress(Invoice invoice);
	public void computeReference(ActionRequest request,ActionResponse response);
	public String createDomainForPartyContact();
	public String createDomainForPartyAddress();
}