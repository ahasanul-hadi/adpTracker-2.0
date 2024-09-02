package com.tracker.adp.document;

import com.tracker.adp.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.OutputStream;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class DocumentController {

    private final DocumentService documentService;


    @RequestMapping(value = {"/api/document/preview/{id}"}, method = RequestMethod.GET)
    public void getDocumentById(HttpServletResponse response, HttpServletRequest request, @PathVariable String id) throws Exception {
        DocumentEntity dto = documentService.findById(id).orElseThrow(() -> new ApplicationException("No document Found!", HttpStatus.NOT_FOUND));
        OutputStream outStream = response.getOutputStream();
        byte[] imageBytes= documentService.getImageBytes(dto);
        // modifies response
        response.setContentType("application/octet-stream");
        response.setContentLength( imageBytes.length);

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", dto.getOriginalFileName());
        response.setHeader(headerKey, headerValue);

        outStream.write(imageBytes);
        outStream.close();

    }

}
