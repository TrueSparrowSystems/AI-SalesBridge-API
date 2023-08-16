package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.NoteDto;
import com.salessparrow.api.dto.formatter.CreateNoteFormatterDto;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.services.accounts.CreateNoteService;
import com.salessparrow.api.services.accounts.GetAccountListService;
import com.salessparrow.api.services.accounts.GetNoteDetailsService;
import com.salessparrow.api.services.accounts.GetNotesListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private GetAccountListService getAccountListService;

  @Autowired
  private GetNotesListService getNotesListService;

  @Autowired
  private GetNoteDetailsService getNoteDetailsService;

  @Autowired
  private CreateNoteService createNoteService;

  @PostMapping("/{account_id}/notes")
  public ResponseEntity<CreateNoteFormatterDto> addNoteToAccount(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId, 
    @Valid @RequestBody NoteDto note
  ) {
    CreateNoteFormatterDto createNoteFormatterDto = createNoteService.createNote(request, accountId, note);

    return ResponseEntity.ok().body(createNoteFormatterDto);
  }

  @GetMapping("")
  public ResponseEntity<GetAccountsFormatterDto> getAccounts(HttpServletRequest request, @RequestParam String q) {
    logger.info("Request received");

    GetAccountsFormatterDto getAccountsResponse = getAccountListService.getAccounts(request, q);

    return ResponseEntity.ok().body(getAccountsResponse);
  }

  @GetMapping("/{account_id}/notes")
  public ResponseEntity<GetNotesListFormatterDto> getNotesList(HttpServletRequest request,@PathVariable("account_id") String accountId) {

    GetNotesListFormatterDto getNotesListResponse = getNotesListService.getNotesList(request, accountId);

    return ResponseEntity.ok().body(getNotesListResponse);
  }
  
  @GetMapping("/{account_id}/notes/{note_id}")
  public ResponseEntity<GetNoteDetailsFormatterDto> getNoteFromAccount(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId, 
    @PathVariable("note_id") String noteId
  ) {

    GetNoteDetailsFormatterDto getNoteDetailsResponse = getNoteDetailsService.getNoteDetails(request, noteId);
    
    return ResponseEntity.ok().body(getNoteDetailsResponse);
  }
}
