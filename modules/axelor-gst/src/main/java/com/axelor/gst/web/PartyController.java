package com.axelor.gst.web;

import com.axelor.gst.service.PartyService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PartyController {

	@Inject
	private PartyService partyService;

	public void setReference(ActionRequest request, ActionResponse response) {
	   partyService.computeReference(request,response);
	}
}