package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.repo.GstSequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImpl implements InvoiceService {
	
	@Inject
	private InvoiceLineService invoiceLineService;


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

		List<Contact> partyContactList = getPartyContactList(invoice);

		if (!partyContactList.isEmpty()) {
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

		List<Address> partyAddressList = getPartyAddressList(invoice);
		Address defaultAddress = null;
		Address invoiceAddress = null;
		Address shippingAddress = null;
		if (!partyAddressList.isEmpty()) {

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
		// No Party found
		if (invoice.getParty() == null) {
			invoice.setPartyContact(null);
			invoice.setInvoiceAddress(null);
			invoice.setShippingAddress(null);
		}
		return invoice;
	}

	@Override
	public Invoice getShippingAddress(Invoice invoice) {
		List<Address> partyAddressList = getPartyAddressList(invoice);
		Address defaultAddress = null;
		Address shippingAddress = null;
		if (!partyAddressList.isEmpty()) {

			for (Address a : partyAddressList) {
				// finding default type address
				if (a.getType().equals("1")) {
					defaultAddress = a;
					// finding shipping type address
				} else if (a.getType().equals("3")) {
					shippingAddress = a;
				}
			}
		}
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

	@Transactional
	@Override
	public void computeReference(Sequence sequence) {
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
		sequence.setNextNumber(nextNumberstr);
		
		GstSequenceRepository sequenceRepository = Beans.get(GstSequenceRepository.class);
		
		sequenceRepository.save(sequence);
	}

	@Override
	public String createDomainForPartyContact(Invoice invoice) {
		String domain = null;
		List<Contact> partyContactList = getPartyContactList(invoice);
		if (!partyContactList.isEmpty()) {
			domain = "self.id IN " + partyContactList.stream().map(i -> i.getId()).collect(Collectors.toList())
					.toString().replace('[', '(').replace(']', ')');
		} else {
			domain = "self.id = null";
		}
		return domain;
	}

	@Override
	public String createDomainForPartyAddress(Invoice invoice) {
		String domain = null;
		List<Address> partyAddressList = getPartyAddressList(invoice);
		if (!partyAddressList.isEmpty()) {
			domain = "self.id IN " + partyAddressList.stream().map(i -> i.getId()).collect(Collectors.toList())
					.toString().replace('[', '(').replace(']', ')');
		} else {
			domain = "self.id = null";
		}
		return domain;
	}

	public List<Contact> getPartyContactList(Invoice invoice) {
		List<Contact> partyContactList = new ArrayList<>();
		if (invoice.getParty() != null) {
			partyContactList = invoice.getParty().getContactList();
		}
		return partyContactList;
	}

	public List<Address> getPartyAddressList(Invoice invoice) {
		List<Address> partyAddressList = new ArrayList<>();
		if (invoice.getParty() != null) {
			partyAddressList = invoice.getParty().getAddressList();
		}
		return partyAddressList;
	}

	@Override
	public Invoice calculateProductItemsList(Invoice invoice, List<String> productIdList) {
		ProductRepository productRepository = Beans.get(ProductRepository.class);
		List<Product> productList = productRepository.all().filter("self.id IN ?1", productIdList).fetch();
		List<InvoiceLine> invoiceItemsList = new ArrayList<>();
		for (Product p : productList) {
			InvoiceLine invoiceLine = Beans.get(InvoiceLine.class);
			invoiceLine.setProduct(p);
			invoiceLine = invoiceLineService.calculateProductValues(invoiceLine);
			invoiceLine = invoiceLineService.calculateGstValues(invoice, invoiceLine);
			invoiceItemsList.add(invoiceLine);
		}
		invoice.setInvoiceItemsList(invoiceItemsList);
		
		return invoice;
	
	}
}