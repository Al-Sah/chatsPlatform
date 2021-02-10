package com.aldevs.chatsplatform.annotation.impl;

import com.aldevs.chatsplatform.annotation.ParticipantsValidator;
import com.aldevs.chatsplatform.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class ParticipantsConstraintValidator implements ConstraintValidator<ParticipantsValidator, Object> {

    private final UserRepository userRepository;
    public ParticipantsConstraintValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void initialize(ParticipantsValidator constraintAnnotation) {}

    public Set<String> findDuplicates(List<String> participants) {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();
        for (String el : participants) if(!set1.add(el)) setToReturn.add(el);
        return setToReturn;
    }
    
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        List<String> errors = new LinkedList<>();
        if(value == null){
            errors.add("field participant cannot be null");
        } else if(value instanceof String){
            if(!userRepository.existsByUsername((String) value)){
                errors.add("Username ["+value+"] not found");
            }
        } else if(value instanceof List<?>){
            var duplicates = findDuplicates((List<String>)value);
            if (duplicates.isEmpty()){
                for (String user : (List<String>)value){
                    if(!userRepository.existsByUsername(user)){
                        errors.add("Username ["+user+"] not found");
                    }
                }
            } else{
                StringBuilder sb = new StringBuilder();
                duplicates.forEach((s-> sb.append(s).append(", ")));
                errors.add("Duplicates found: " + sb);
            }
        }
        if(errors.isEmpty()){
            return true;
        }else {
            String messageTemplate = String.join(".\n", errors);
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation().disableDefaultConstraintViolation();
            return false;
        }
    }
}
