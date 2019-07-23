package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public class InvoiceImpl implements InvoiceService {

	private Address shippingAddress;
	private Address defaultAddress;

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
		defaultAddress = null;
		if (invoice.getParty() != null) {
			if (!invoice.getParty().getContactList().isEmpty()) {
				List<Contact> partyContactList = invoice.getParty().getContactList();
				for (Contact c : partyContactList) {
					// finding primary contact
					if (c.getType().equals("1")) {
						invoice.setPartyContact(c);
						break;
					}
				}
				// no primary contact found
				if (invoice.getPartyContact() == null)
					invoice.setPartyContact(partyContactList.get(0));
			} else {
				invoice.setPartyContact(null);
			}
			if (!invoice.getParty().getAddressList().isEmpty()) {
				List<Address> partyAddressList = invoice.getParty().getAddressList();
				Address invoiceAddress = null;
				for (Address a : partyAddressList) {
					// finding default type address
					if (a.getType().equals("1")) {
						defaultAddress = a;
						// finding invoice type address
					} else if (a.getType().equals("2")) {
						invoiceAddress = a;
						// finding shipping type address
					} else if (a.getType().equals("3")) {
						shippingAddress = a;
					}
				}
				// Setting invoice address as invoice address
				if (invoiceAddress != null) {
					invoice.setInvoiceAddress(invoiceAddress);
				}

				else if (shippingAddress != null) {
					invoice.setShippingAddress(shippingAddress);
				}
				// Setting default address as invoice address
				else if (defaultAddress != null) {
			        invoice.setInvoiceAddress(defaultAddress);
				} else {
					invoice.setInvoiceAddress(partyAddressList.get(0));
				}

			}
			// No address found for party
			else {
				invoice.setInvoiceAddress(null);
				invoice.setShippingAddress(null);
			}
		}
		// No Party found
		else {
			invoice.setPartyContact(null);
			invoice.setInvoiceAddress(null);
			invoice.setShippingAddress(null);
		}
		return invoice;
	}

	@Override
	public Invoice getShippingAddress(Invoice invoice) {
		if (invoice.getIsInvoiceAddress()) {
			invoice.setShippingAddress(invoice.getInvoiceAddress());
		} else if (shippingAddress != null) {
			invoice.setShippingAddress(shippingAddress);
		}
		else if(defaultAddress != null) {
			invoice.setShippingAddress(defaultAddress);
		}
		else {
			invoice.setShippingAddress(null);
		}
		return invoice;
	}
}