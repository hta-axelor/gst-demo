package com.axelor.gst.service;

import java.util.List;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;

public interface InvoiceService {
	public Invoice calculateItems(Invoice invoice);
	public Invoice calculatePartyValues(Invoice invoice);
	public Invoice getShippingAddress(Invoice invoice);
	public void computeReference(Sequence sequence);
	public Invoice calculateProductItemsList(Invoice invoice, List<String> productIdList);
	public void checkPartyNullStates(Invoice invoice) throws Exception;
	public void checkCompanyNullStates(Invoice invoice) throws Exception;
}