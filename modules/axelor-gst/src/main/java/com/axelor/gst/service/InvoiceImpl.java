package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public class InvoiceImpl implements InvoiceService {
	
	private Address shippingAddress;

	@Override
	public Invoice calculateItems(Invoice invoice) {
		BigDecimal netAmountSum = BigDecimal.ZERO;
		BigDecimal gstRateSum = BigDecimal.ZERO;
		BigDecimal igstSum = BigDecimal.ZERO;
		BigDecimal cgstSum = BigDecimal.ZERO;
		BigDecimal sgstSum = BigDecimal.ZERO;
		BigDecimal grossAmount = BigDecimal.ZERO;	

		List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
		for (InvoiceLine il : invoiceLineList) {
			netAmountSum = netAmountSum.add(il.getNetAmount());
			gstRateSum = gstRateSum.add(il.getGstRate());
			igstSum = igstSum.add(il.getIgst());
			cgstSum = cgstSum.add(il.getCgst());
			sgstSum = sgstSum.add(il.getSgst());
			grossAmount = grossAmount.add(il.getGrossAmount());
		}
		invoice.setNetAmount(netAmountSum);
		invoice.setNetIgst(igstSum);
		invoice.setNetCgst(cgstSum);
		invoice.setNetSgst(sgstSum);
		invoice.setGrossAmount(grossAmount);

		return invoice;
	}

	@Override
	public Invoice calculatePartyValues(Invoice invoice) {
		shippingAddress = null;
		if (invoice.getParty() != null) {
			if (!invoice.getParty().getContactList().isEmpty()) {
				List<Contact> partyContactList = invoice.getParty().getContactList();
				for (Contact c : partyContactList) {
					if (c.getType().equals("1")) {
						invoice.setPartyContact(c);
						break;
					}
				}
				if (invoice.getPartyContact() == null)
					invoice.setPartyContact(partyContactList.get(0));
			} else {
				invoice.setPartyContact(null);
			}
			if (!invoice.getParty().getAddressList().isEmpty()) {
				List<Address> partyAddressList = invoice.getParty().getAddressList();
				for (Address a : partyAddressList) {
					if (a.getType().equals("2")) {
						invoice.setInvoiceAddress(a);
					} else if (a.getType().equals("3")) {
						shippingAddress = a;
						invoice.setShippingAddress(shippingAddress);
					}
				}
				if (invoice.getInvoiceAddress() == null)
					invoice.setInvoiceAddress(partyAddressList.get(0));
			} else {
				invoice.setInvoiceAddress(null);
				invoice.setShippingAddress(null);
			}
		} else {
			invoice.setPartyContact(null);
			invoice.setInvoiceAddress(null);
			invoice.setShippingAddress(null);
		}
		return invoice;
	}

	@Override
	public Invoice getShippingAddress(Invoice invoice) {
		System.out.println(invoice.getInvoiceAddress());
		if (invoice.getIsInvoiceAddress()) {
			invoice.setShippingAddress(invoice.getInvoiceAddress());
		} else if (shippingAddress != null) {
		    invoice.setShippingAddress(shippingAddress);
		} else {
			invoice.setShippingAddress(null);
		}
		return invoice;
	}
}