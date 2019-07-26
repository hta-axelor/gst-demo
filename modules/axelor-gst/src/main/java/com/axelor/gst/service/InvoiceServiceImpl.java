package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Sequence;

public class InvoiceServiceImpl implements InvoiceService {

	private Address shippingAddress;
	private Address defaultAddress;
	private Boolean isPartyContactEmpty = true;
	private Boolean isPartyAddressEmpty = true;
	private List<Contact> partyContactList;
	private List<Address> partyAddressList;

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
		
		//setting default values;
		shippingAddress = null;
		defaultAddress = null;
		
		isPartyContactEmpty = true;
		isPartyAddressEmpty = true;
		
		if (invoice.getParty() != null) {
			partyContactList = invoice.getParty().getContactList();
			if (!partyContactList.isEmpty()) {
				isPartyContactEmpty = false;
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
			
			partyAddressList = invoice.getParty().getAddressList();
			if (!partyAddressList.isEmpty()) {
				isPartyAddressEmpty = false;
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

				// Setting default address as invoice address
				else if (defaultAddress != null) {
					invoice.setInvoiceAddress(defaultAddress);
				} 

				// Setting shipping address as invoice address
				else if (shippingAddress != null) {
					invoice.setInvoiceAddress(shippingAddress);
				}
				
				else {
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
		} else if (defaultAddress != null) {
			invoice.setShippingAddress(defaultAddress);
		} else {
			invoice.setShippingAddress(null);
		}
		return invoice;
	}

	@Override
	public String computeReference(Sequence sequence) {
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		Integer padding = sequence.getPadding();
		String previousNumberStr = sequence.getNextNumber();
		String nextNumberstr;

		// splitting
		previousNumberStr = sequence.getNextNumber().substring(prefix.length(), prefix.length() + padding);

		// increment
		String incremented = String.format("%0" + previousNumberStr.length() + "d",
				Integer.parseInt(previousNumberStr) + 1);

		// merging
		if (suffix == null) {
			nextNumberstr = prefix + incremented;
		} else {
			nextNumberstr = prefix + incremented + suffix;
		}

		return nextNumberstr;
	}

	@Override
	public String createDomainForPartyContact() {
		String domain = null;
		if (!isPartyContactEmpty) {
			domain = "self.id IN " + partyContactList.stream().map(i -> i.getId())
					.collect(Collectors.toList()).toString().replace('[', '(').replace(']', ')');
		}
		else {
			domain = "self.id = null";
		}
		return domain;
	}

	@Override
	public String createDomainForPartyAddress() {	
		String domain = null;
		if (!isPartyAddressEmpty) {
			domain = "self.id IN " + partyAddressList.stream().map(i -> i.getId())
					.collect(Collectors.toList()).toString().replace('[', '(').replace(']', ')');
		}
		else {
			domain = "self.id = null";
		}
		return domain;
	}	
}