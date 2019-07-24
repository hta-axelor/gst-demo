package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.GstPartyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.InvoiceImpl;
import com.axelor.gst.service.InvoiceLineImpl;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.PartyImpl;
import com.axelor.gst.service.PartyService;
import com.axelor.gst.service.SequenceImpl;
import com.axelor.gst.service.SequenceService;

public class GstModule extends AxelorModule{

	@Override
	protected void configure() {
	    bind(PartyRepository.class).to(GstPartyRepository.class);
	    bind(InvoiceLineService.class).to(InvoiceLineImpl.class);
	    bind(InvoiceService.class).to(InvoiceImpl.class);
	    bind(PartyService.class).to(PartyImpl.class);
	    bind(SequenceService.class).to(SequenceImpl.class);
	    bind(InvoiceLine.class);
	}   
}
