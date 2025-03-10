package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.repository.InstructorRepository;
import cz.inspire.utils.FileAttributes;
import cz.inspire.utils.FileStorageUtil;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class InstructorService extends BaseService<InstructorEntity, String, InstructorRepository> {

    private final FileStorageUtil fileStorageUtil = new FileStorageUtil("FILE_SYSTEM");
    // Rename this to some number, which will not be easily recognised
    private static final String ATTACHMENTS_DIRECTORY = "photos";
    private Logger logger;

    public InstructorService() {
    }

    @Inject
    public InstructorService(InstructorRepository repository, Logger logger) {
        super(repository);
        this.logger = logger;
    }

    public FileAttributes savePhoto(byte[] photo) throws IOException {
        if (photo == null || photo.length == 0) {
            /// We can use here some default profile photo, if it's not handled in FrontEnd,
            /// but it is probably more efficient to do it on side of FrontEnd than in BackEnd
            return null;
        }
        return fileStorageUtil.saveFile(photo, null, ATTACHMENTS_DIRECTORY);
    }

    public byte[] readFile(String filePath) throws IOException {
        return fileStorageUtil.readFile(filePath);
    }

    public void deleteFile(String filePath) throws IOException {
        fileStorageUtil.removeFile(filePath);
    }


    public List<InstructorEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAll(),
                "Error retrieving all InstructorEntity records, Ordered by index"
        );
    }

    public List<InstructorEntity> findAll(int offset, int count, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(count, offset, deleted),
                "Error retrieving paginated InstructorEntity records (offset = " + offset +
                        ", count = " + count + ", deleted = " + deleted + ")"
        );
    }

    public List<InstructorEntity> findAllByActivity(String activityId, int offset, int count, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.findAllByActivity(activityId, count, offset, deleted),
                "Error retrieving InstructorEntity records by activityId = " + activityId +
                        " with pagination (offset = " + offset + ", count = " + count + ", deleted = " + deleted + ")"
        );
    }

    public Long countInstructors(boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.countInstructors(deleted),
                "Error retrieving total count of InstructorEntity records (deleted = " + deleted + ")"
        );
    }

    public Long countInstructorsByActivity(String activityId, boolean deleted) throws FinderException {
        return wrapDBException(
                () -> repository.countInstructorsByActivity(activityId, deleted),
                "Error retrieving count of InstructorEntity records by activityId = " + activityId +
                        " (deleted = " + deleted + ")"
        );
    }

}
