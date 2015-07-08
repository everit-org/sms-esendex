/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.sms.esendex;

import esendex.sdk.java.EsendexException;
import esendex.sdk.java.ServiceFactory;
import esendex.sdk.java.model.domain.request.SmsMessageRequest;
import esendex.sdk.java.service.BasicServiceFactory;
import esendex.sdk.java.service.MessagingService;
import esendex.sdk.java.service.auth.UserPassword;

import org.everit.sms.SMSSender;

/**
 * Esendex based implementation od the SMSSender interface.
 */
public class EsendexSMSSender implements SMSSender {

  private final String accountReference;

  private final MessagingService messagingService;

  /**
   * Constructor to set up the EsendexSMSSender.
   *
   * @param username
   *          Esendex account username.
   * @param password
   *          Esendex account password.
   * @param accountReference
   *          Esendex account reference code.
   */
  public EsendexSMSSender(final String username, final String password,
      final String accountReference) {

    this.accountReference = accountReference;

    UserPassword userPassword = new UserPassword(username, password);
    BasicServiceFactory serviceFactory =
        ServiceFactory.createBasicAuthenticatingFactory(userPassword);
    messagingService = serviceFactory.getMessagingService();
  }

  @Override
  public void sendSMS(final String recipientNumber, final String message) {

    SmsMessageRequest messageRequest = new SmsMessageRequest(recipientNumber, message);

    try {
      messagingService.sendMessage(accountReference, messageRequest);
    } catch (EsendexException e) {
      throw new RuntimeException("Failed to send the SMS "
          + "to recipientNumber [" + recipientNumber + "] "
          + "with message [" + message + "]", e);
    }
  }

}
