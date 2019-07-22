package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Product;

public class InvoiceLineImpl implements InvoiceLineService {

	private BigDecimal gstRate;
	private BigDecimal netAmount;
	private BigDecimal igst = BigDecimal.ZERO;
	private BigDecimal sgst = BigDecimal.ZERO;
	private BigDecimal cgst = BigDecimal.ZERO;
	private BigDecimal grossAmount = BigDecimal.ZERO;
	private String item;
	private BigDecimal price;

	@Override
	public BigDecimal calculateNetAmount(int qty, BigDecimal price) {
		netAmount = price.multiply(new BigDecimal(qty));
		return netAmount;
	}

	@Override
	public BigDecimal calculateGstRate(Product product) {
		gstRate = product.getGstRate();
		return gstRate;
	}
	
	@Override
	public String getItem(Product product) {
		item = product.getCategory().getName();
		return item;
	}
	
	@Override
	public BigDecimal calculateprice(Product product) {
		price = product.getSalePrice();
		return price;
	}

	@Override
	public BigDecimal calculateIgst() {
		igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
		return igst;
	}

	@Override
	public BigDecimal calculateSgst() {
		sgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
		return sgst;
	}

	@Override
	public BigDecimal calculateCgst() {
		cgst = netAmount.multiply(gstRate).divide(new BigDecimal(200));
		return cgst;
	}

	@Override
	public BigDecimal calculateGrossAmount() {
		grossAmount = netAmount.add(igst).add(sgst).add(cgst);
		return grossAmount;
	}
}