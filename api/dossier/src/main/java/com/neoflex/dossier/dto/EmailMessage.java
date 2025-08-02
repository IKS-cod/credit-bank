package com.neoflex.dossier.dto;

import com.neoflex.dossier.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {

    private String address;    // email адрес клиента

    private Theme theme;       // тема письма - enum

    private Long statementId;  // идентификатор заявления

    private String text;       // текст письма
}