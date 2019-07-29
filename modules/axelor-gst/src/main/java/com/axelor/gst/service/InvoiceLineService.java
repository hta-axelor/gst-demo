package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
   public InvoiceLine calculateGstValues(Invoice invoice,InvoiceLine invoiceLine) throws Exception;
   public InvoiceLine calculateProductValues(InvoiceLine invoiceLine);
}