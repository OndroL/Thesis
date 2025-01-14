package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmailHistoryService extends BaseService<EmailHistoryEntity, EmailHistoryRepository> {

    private static final String FILESYSTEM_PATH = "FILE_SYSTEM/Email_History/";

    private static final String FILE_NAME_PATTERN = "voucher-d-M-yyyy.pdf";

    @Inject
    public EmailHistoryService(Logger logger, EmailHistoryRepository repository) {
        super(logger, repository, EmailHistoryEntity.class);
    }

    public String saveFileToFileSystem(byte[] fileData, String entityId) throws IOException {
        String directoryPath = FILESYSTEM_PATH + entityId;
        Files.createDirectories(Paths.get(directoryPath)); // Create directories if they don't exist

        String filePath = directoryPath + "/" + generateFileName();
        Files.write(Paths.get(filePath), fileData); // Save file to the file system
        return filePath; // Return the path to store in the database
    }

    public static String generateFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        String datePart = LocalDate.now().format(formatter);
        return FILE_NAME_PATTERN.replace("d-M-yyyy", datePart);
    }

    public List<EmailHistoryEntity> findAll() { return repository.findAll(); }

    public List<EmailHistoryEntity> findAll(int offset, int count) { return repository.findAll(offset, count); }

    public List<EmailHistoryEntity> findByDate(Date dateFrom, Date dateTo, int offset, int count) {
        return repository.findByDate(dateFrom, dateTo, offset, count);
    }

    public Optional<EmailHistoryEntity> findById(String emailHistoryId) {return repository.findById(emailHistoryId); }

}
