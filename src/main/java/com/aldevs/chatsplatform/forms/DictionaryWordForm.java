package com.aldevs.chatsplatform.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DictionaryWordForm {
    @NotBlank
    @NotNull
    private String word;
}
