package com.challenge.rental_cars_spring_api.core.service;

import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.queries.ListarCarrosQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GerarRelatorioPdfService {

    private final ListarAlugueisQuery listarAlugueisQuery;
    private final ListarCarrosQuery listarCarrosQuery;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarRelatorioAlugueis() throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relatório de Aluguéis", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            String[] headers = {"Data Aluguel", "Cliente", "Telefone", "Carro", "Valor", "Pago", "Data Devolução"};
            for (String header : headers) {
                table.addCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            }

            ListarAlugueisQuery.Result resultado = listarAlugueisQuery.execute();
            for (ListarAlugueisQueryResultItem aluguel : resultado.alugueis()) {
                table.addCell(aluguel.dataAluguel().format(DATE_FORMATTER));
                table.addCell(aluguel.nomeCliente());
                table.addCell(aluguel.telefoneCliente());
                table.addCell(aluguel.modeloCarro());
                table.addCell("R$ " + aluguel.valor().toString());
                table.addCell(aluguel.pago());
                table.addCell(aluguel.dataDevolucao().format(DATE_FORMATTER));
            }

            document.add(table);

            Paragraph totalNaoPago = new Paragraph(
                String.format("Valor Total Não Pago: R$ %.2f", resultado.valorTotalNaoPago()),
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
            );
            totalNaoPago.setAlignment(Element.ALIGN_RIGHT);
            totalNaoPago.setSpacingBefore(20);
            document.add(totalNaoPago);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    public byte[] gerarRelatorioCarros() throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Relatório da Frota de Carros", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            String[] headers = {"Modelo", "Ano", "Passageiros", "KM", "Fabricante", "Valor Diária"};
            for (String header : headers) {
                table.addCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
            }

            List<ListarCarrosQueryResultItem> carros = listarCarrosQuery.execute();
            for (ListarCarrosQueryResultItem carro : carros) {
                table.addCell(carro.modelo());
                table.addCell(carro.ano());
                table.addCell(carro.qtdPassageiros().toString());
                table.addCell(carro.km().toString());
                table.addCell(carro.fabricante());
                table.addCell("R$ " + carro.vlrDiaria().toString());
            }

            document.add(table);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }
} 