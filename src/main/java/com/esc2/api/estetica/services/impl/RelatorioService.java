package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.models.AgendamentoModel;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RelatorioService {
    public void gerarRelatorioAgendamentos(List<AgendamentoModel> agendamentos, HttpServletResponse response) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Agendamentos");


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);


        Row headerRow = sheet.createRow(0);
        String[] columns = {"Data","Hora", "Cliente", "Serviços", "Desconto", "Valor Final", "Status"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }


        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("R$ #,##0.00"));


        int rowNum = 1;

        ZoneId fusoClinica = ZoneId.of("America/Sao_Paulo");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(fusoClinica);

        for (AgendamentoModel agendamento : agendamentos) {
            Row row = sheet.createRow(rowNum++);

            Instant dataHora = agendamento.getDataHora().atZone(fusoClinica).toInstant();
            Cell dataCell = row.createCell(0);
            dataCell.setCellValue(java.util.Date.from(dataHora)); // POI trabalha melhor com java.util.Date
            dataCell.setCellStyle(dateStyle);

            row.createCell(1).setCellValue(timeFormatter.format(dataHora));


            row.createCell(2).setCellValue(agendamento.getCliente().getNome());

            String servicosNomes = agendamento.getServicosAgendados().stream()
                    .map(item -> item.getServico().getNome())
                    .collect(Collectors.joining(", "));
            row.createCell(3).setCellValue(servicosNomes);



            if(agendamento.getValorDescontoAplicado().compareTo(BigDecimal.ZERO) > 0 ) {
                row.createCell(4).setCellValue("Sim");
            } else {
                row.createCell(4).setCellValue("N/A");
            }

            Cell valorFinalCell = row.createCell(5);
            valorFinalCell.setCellValue(agendamento.getValorTotal().doubleValue());
            valorFinalCell.setCellStyle(currencyStyle);

            row.createCell(6).setCellValue(agendamento.getStatus().toString());
        }


        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }


        workbook.write(response.getOutputStream());
        workbook.close();
    }
}