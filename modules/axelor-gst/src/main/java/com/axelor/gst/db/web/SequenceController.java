package com.axelor.gst.db.web;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SequenceController {

	@Inject
	private SequenceService sequenceService;

	public void setNextNumber(ActionRequest request, ActionResponse response) {
		Sequence sequence = request.getContext().asType(Sequence.class);
		String nextNumber = sequenceService.computeNextSequence(sequence);
        response.setValue("nextNumber", nextNumber);
	}
}