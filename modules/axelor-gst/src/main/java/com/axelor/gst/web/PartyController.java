package com.axelor.gst.web;

import java.util.List;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.PartyService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class PartyController {

	@Inject
	private PartyService partyService;


	public void setReference(ActionRequest request, ActionResponse response) {
	   partyService.computeReference(request,response);
	}
}