package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;

public interface InvoiceService {
  public Invoice calculateItems(Invoice invoice);
  public Invoice calculatePartyValues(Invoice invoice);
  public Invoice getShippingAddress(Invoice invoice);
}