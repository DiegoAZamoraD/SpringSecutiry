package com.diego.SpringSecutiry.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class CustomerController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/index")
    public String index(){
        return "Hello World";
    }

    @GetMapping("/index2")
    public String index2(){
        return "Hello World sin seguridad";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
        String sessionId = "";
        User userObject =  null;
        List<Object> sessions =  sessionRegistry.getAllPrincipals();
        for (Object session:sessions) {
            if(session instanceof User){
                userObject = (User) session; //Informacion del susuario que se esta registrando
            }
            List<SessionInformation> sessionInformations =  sessionRegistry.getAllSessions(session,false);
            for (SessionInformation sessionInformation: sessionInformations) {
                sessionId = sessionInformation.getSessionId(); //Id de la sesion
            }
        }
        Map<String, Object> response =  new HashMap<>();
        response.put("Response:","Hello World");
        response.put("session Id", sessionId);
        response.put("Session User:", userObject);
        return ResponseEntity.ok(response);
    }
}
