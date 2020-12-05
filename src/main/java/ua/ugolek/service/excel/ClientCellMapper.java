package ua.ugolek.service.excel;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import ua.ugolek.model.Client;

import java.time.format.DateTimeFormatter;

@Component
public class ClientCellMapper extends CellMapper<Client>
{
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void createRowCells(Row row, Client client)
    {
        row.createCell(0).setCellValue(client.getId());
        row.createCell(1).setCellValue(client.getFirstName());
        row.createCell(2).setCellValue(client.getLastName());
        row.createCell(3).setCellValue(client.getEmail());
        row.createCell(4).setCellValue(client.getPhoneNumber());
        row.createCell(5).setCellValue(client.getRegistrationDate().format(dateFormatter));
        row.createCell(6).setCellValue(client.getBirthday().format(dateFormatter));
    }

    @Override
    public String[] getHeaders()
    {
        return new String[] {
            "ID", "First Name", "Last Name", "Email", "Phone number", "Registration date", "Birthday"
        };
    }
}
