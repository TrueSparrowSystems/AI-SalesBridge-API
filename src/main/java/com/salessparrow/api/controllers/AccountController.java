package com.salessparrow.api.controllers;

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
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.formatter.GetNotesListFormatterDto;
import com.salessparrow.api.services.accounts.GetAccountListService;
import com.salessparrow.api.services.accounts.GetNotesListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  @Autowired
  private GetAccountListService getAccountListService;

  @Autowired
  private GetNotesListService getNotesListService;

  @PostMapping("/{account_id}/notes")
  public ResponseEntity<String> addNoteToAccount(
    @PathVariable("account_id") String accountId, 
    @Valid @RequestBody NoteDto note
  ) {
    return ResponseEntity.ok("Note added to Account");
  }

  @GetMapping("")
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, @RequestParam String q) {

    return getAccountListService.getAccounts(request, q);
  }

  @GetMapping("/{account_id}/notes")
  public GetNotesListFormatterDto getNotesList(HttpServletRequest request,@PathVariable("account_id") String accountId) {

    return getNotesListService.getNotesList(request, accountId);
  }
  
  @GetMapping("/{account_id}/notes/{note_id}")
  public ResponseEntity<String> getNoteFromAccount(
    @PathVariable("account_id") String accountId, 
    @PathVariable("note_id") String noteId
  ) {
    return ResponseEntity.ok("Note from Account");
  }
}
