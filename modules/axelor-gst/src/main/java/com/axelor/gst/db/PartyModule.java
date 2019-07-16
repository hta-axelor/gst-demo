package com.axelor.gst.db;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.PartyCardRepository;
import com.axelor.gst.db.repo.PartyRepository;

public class PartyModule extends AxelorModule{  
	 @Override
	  protected void configure() {
	    bind(PartyRepository.class).to(PartyCardRepository.class);
	  }
}  
