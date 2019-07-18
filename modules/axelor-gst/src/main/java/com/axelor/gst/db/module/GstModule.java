package com.axelor.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.GstPartyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.InvoiceLineImpl;
import com.axelor.gst.service.InvoiceLineService;

public class GstModule extends AxelorModule{

	@Override
	protected void configure() {
	    bind(PartyRepository.class).to(GstPartyRepository.class);
	    bind(InvoiceLineService.class).to(InvoiceLineImpl.class);
	}   
}
