package com.aldevs.chatsplatform.annotation;

import com.aldevs.chatsplatform.annotation.impl.ParticipantsConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;


@Constraint(validatedBy = {ParticipantsConstraintValidator.class})
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParticipantsValidator  {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
