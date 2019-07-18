package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;

public interface InvoiceLineService {
   public BigDecimal calculateNetAmount(int qty,BigDecimal price);
   public BigDecimal calculateGstRate(Product product);
   public String getItem(Product product);
   public BigDecimal calculateprice(Product product);
   public BigDecimal calculateIgst();
   public BigDecimal calculateSgst();
   public BigDecimal calculateCgst();
   public BigDecimal calculateGrossAmount();
}
