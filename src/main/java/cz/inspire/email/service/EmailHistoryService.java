package cz.inspire.email.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.utils.FileStorageUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class EmailHistoryService extends BaseService<EmailHistoryEntity, EmailHistoryRepository> {

    // Rename this to some number, which will not be easily recognised
    private static final String ATTACHMENTS_DIRECTORY = "attachments";
    private final FileStorageUtil fileStorageUtil;

    private static final String FILE_NAME_PATTERN = "voucher-d-M-yyyy.pdf";

    @Inject
    public EmailHistoryService(Logger logger, EmailHistoryRepository repository) {
        super(logger, repository, EmailHistoryEntity.class);
        this.fileStorageUtil = new FileStorageUtil("FILE_SYSTEM");
    }

    public List<Map<String, String>> saveAttachments(Map<String, byte[]> attachments) throws IOException {
        List<Map<String, String>> savedAttachments = new ArrayList<>();
        for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {

            String originalFileName = entry.getKey();
            byte[] fileData = entry.getValue();

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

    // Currently this is not used

    // Brainstorm naming pattern for vouchers, because with this implementation which is identical
    // to controller implementation, problem with same names for 2 different voucher can occur.
    // E.g. two vouchers created in same day for same EmailHistory will be stored in FileSystem
    // with same name and second one will rewrite first one!
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

    public Optional<EmailHistoryEntity> findById(String emailHistoryId) { return repository.findById(emailHistoryId); }

}
