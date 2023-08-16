package com.salessparrow.api.lib.salesforce.formatSalesforceEntities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.dto.entities.NoteDetailEntity;
import com.salessparrow.api.dto.entities.NoteEntity;
import com.salessparrow.api.dto.formatter.GetNoteDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;

/**
 * FormatSalesforceNoteDetails is a class that formats the response from the GetNoteDetails action for the CRM.
 * 
 */
public class FormatSalesforceNoteDetails {

    /**
     * formatNoteDetails is a method that formats the response from the GetNoteDetails action for the CRM.
     * 
     * @param noteDetailsResponse
     * @param noteContentResponse
     * 
     * @return GetNoteDetailsFormatterDto
     */ 
    public GetNoteDetailsFormatterDto formatNoteDetails(String noteDetailsResponse, String noteContentResponse){
        NoteEntity noteEntity = new NoteEntity();
        try {
            Util util = new Util();
            JsonNode rootNode = util.getJsonNode(noteDetailsResponse);
            JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

            for (JsonNode recordNode : recordsNode) {
                String noteId = recordNode.get("Id").asText();
                String textPreview = recordNode.get("TextPreview").asText();
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date lastModifiedDate = new Date();
                try {
                    lastModifiedDate = dateFormat.parse(recordNode.get("LastModifiedDate").asText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String createdBy = recordNode.get("CreatedBy").get("Name").asText();

                noteEntity = new NoteEntity(
                    noteId,
                    createdBy,
                    textPreview,
                    lastModifiedDate
                );
            }
        } catch (Exception e) {
            throw new CustomException(
                new ErrorObject(
                    "something_went_wrong",
                    "l_s_fse_fsnd_1",
                    e.getMessage()
                )
            );
        }

        GetNoteDetailsFormatterDto getNoteDetailsFormatterDto = new GetNoteDetailsFormatterDto(
            new NoteDetailEntity(
                noteEntity.getId(),
                noteEntity.getCreator(),
                noteContentResponse,
                noteEntity.getLastModifiedTime()
            )
        );

        return getNoteDetailsFormatterDto;
    }
}
