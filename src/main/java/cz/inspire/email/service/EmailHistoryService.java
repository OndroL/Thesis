package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.utils.FileStorageUtil;
import jakarta.data.Limit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class EmailHistoryService extends BaseService<EmailHistoryEntity, String, EmailHistoryRepository> {

    private final FileStorageUtil fileStorageUtil = new FileStorageUtil("FILE_SYSTEM");
    private static final String FILE_NAME_PATTERN = "voucher-d-M-yyyy.pdf";
    // Rename this to some number, which will not be easily recognised
    private static final String ATTACHMENTS_DIRECTORY = "attachments";

    public EmailHistoryService() {
    }

    @Inject
    public EmailHistoryService(EmailHistoryRepository repository) {
        super(repository);
    }

    public List<Map<String, String>> saveAttachments(Map<String, byte[]> attachments) throws IOException {
        List<Map<String, String>> savedAttachments = new ArrayList<>();
        for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {

            String originalFileName = entry.getKey();
            byte[] fileData = entry.getValue();

            // Generated fileName if one is not present by using generateFileName() with pattern voucher-d-M-yyyy.pdf
            String fileName = (originalFileName != null && !originalFileName.isEmpty())
                    ? originalFileName
                    : generateFileName();


            savedAttachments.add(fileStorageUtil.saveFile(fileData, fileName, ATTACHMENTS_DIRECTORY));
        }
        return savedAttachments;
    }

    public byte[] readFile(String filePath) throws IOException {
        return fileStorageUtil.readFile(filePath); // Delegate to FileStorageUtil
    }


    // Brainstorm naming pattern for vouchers, because with this implementation which is identical
    // to controller implementation, problem with same names for 2 different voucher can occur.
    // E.g. two vouchers created in same day for same EmailHistory will be returned as only one file
    // because their name is the key in map and the existing (first) file for that key will be overwritten with the new file.
    public static String generateFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        String datePart = LocalDate.now().format(formatter);
        return FILE_NAME_PATTERN.replace("d-M-yyyy", datePart);
    }

    public List<EmailHistoryEntity> findAll() { return repository.findAllOrdered(); }

    public List<EmailHistoryEntity> findAll(int offset, int count) {
        return repository.findAll(new Limit(count, offset)); }

    public List<EmailHistoryEntity> findByDate(Date dateFrom, Date dateTo, int offset, int count) {
        return repository.findByDate(dateFrom, dateTo, new Limit(count, offset));
    }

    public Optional<EmailHistoryEntity> findById(String emailHistoryId) { return repository.findById(emailHistoryId); }

}
