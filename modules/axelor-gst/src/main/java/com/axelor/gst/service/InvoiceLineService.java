package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;

public interface InvoiceLineService {
   public InvoiceLine calculateAllItems(Invoice invoice,InvoiceLine invoiceLine);
   public InvoiceLine setProductItems(InvoiceLine invoiceLine, Product product);
}