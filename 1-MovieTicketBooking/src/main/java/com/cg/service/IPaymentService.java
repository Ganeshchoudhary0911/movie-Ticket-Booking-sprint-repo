package com.cg.service;

import com.cg.dto.PaymentDto;

public interface IPaymentService {
	public PaymentDto chooseMethodAndPay(Long bookingId, String method);

}
