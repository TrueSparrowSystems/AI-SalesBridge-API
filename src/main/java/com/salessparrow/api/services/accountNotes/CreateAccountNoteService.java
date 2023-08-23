package com.salessparrow.api.services.accountNotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.dto.requestMapper.NoteDto;
import com.salessparrow.api.lib.crmActions.createAccountNote.CreateNoteFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CreateNoteService is a service class for the create note action for the CRM.
 */
@Service
public class CreateAccountNoteService {
  
  @Autowired
  private CreateNoteFactory createNoteFactory;
  
  /**
   * Create a note for a specific account.
   * 
   * @param request
   * @param accountId
   * @return CreateNoteFormatterDto
   */
  public CreateNoteFormatterDto createNote(HttpServletRequest request, String accountId, NoteDto note) {

      SalesforceUser currentUser = (SalesforceUser) request.getAttribute("current_user");

      return createNoteFactory.createNote(currentUser, accountId, note);
  }
}
