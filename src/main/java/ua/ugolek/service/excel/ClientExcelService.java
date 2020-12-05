package ua.ugolek.service.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Client;
import ua.ugolek.service.ClientService;

@Service
public class ClientExcelService extends BaseExcelService<Client>
{
    @Autowired
    public ClientExcelService(ClientService crudService, ClientCellMapper clientCellMapper)
    {
        super(clientCellMapper, crudService);
    }
}
