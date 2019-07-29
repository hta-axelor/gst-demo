package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.repo.GstPartyRepository;
import com.axelor.gst.repo.GstProductRepository;
import com.axelor.gst.repo.GstSequenceRepository;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.InvoiceLineServiceImpl;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.PartyServiceImpl;
import com.axelor.gst.service.PartyService;
import com.axelor.gst.service.SequenceServiceImpl;
import com.axelor.gst.service.SequenceService;

public class GstModule extends AxelorModule{

	@Override
	protected void configure() {
	    bind(PartyRepository.class).to(GstPartyRepository.class);
	    bind(ProductRepository.class).to(GstProductRepository.class);
	    bind(SequenceRepository.class).to(GstSequenceRepository.class);
	    bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
	    bind(InvoiceService.class).to(InvoiceServiceImpl.class);
	    bind(PartyService.class).to(PartyServiceImpl.class);
	    bind(SequenceService.class).to(SequenceServiceImpl.class);
	    bind(InvoiceLine.class);
	}   
}
