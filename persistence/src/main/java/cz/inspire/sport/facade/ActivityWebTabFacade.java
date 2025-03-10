package cz.inspire.sport.facade;

import cz.inspire.sport.dto.ActivityWebTabDto;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.mapper.ActivityWebTabMapper;
import cz.inspire.sport.service.ActivityWebTabService;
import cz.inspire.sport.service.ObjektService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ActivityWebTabFacade {

    @Inject
    ActivityWebTabService activityWebTabService;
    @Inject
    ActivityWebTabMapper activityWebTabMapper;
    @Inject
    ObjektService objektService;
    @Inject


    private Logger logger;

    public ActivityWebTabFacade(Logger logger) {
        this.logger = logger;
    }


    public String create(ActivityWebTabDto dto) throws CreateException {
        try {
            ActivityWebTabEntity entity = activityWebTabMapper.toEntity(dto);

            activityWebTabService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityWebTab entity : " + e);
        }
    }

    public void delete(ActivityWebTabEntity entity) throws RemoveException {
        activityWebTabService.delete(entity);
    }

//    public void updateActivityForWeb(Map<Integer, List<String>> tabObjectsMap) {
//        List<ActivityWebTabDto> newEntities = new ArrayList<ActivityWebTabDto>();
//        for (Map.Entry<Integer, List<String>> entry : tabObjectsMap.entrySet()) {
//            int tabIndex = entry.getKey();
//            for (String objectId : entry.getValue()) {
//                try {
//                    ObjektEntity object = objektService.findByPrimaryKey(objectId);
//                    List<SportEntity> objectSports = objektService.getSports();
//                    for (ObjektSportEntity objectSport : objectSports) {
//                        String activityId = objectSport.getSport().getActivity().getId();
//                        String sportId = objectSport.getSport().getId();
//                        if (activityId != null) {
//                            newEntities.add(new ActivityWebTabDto(null, sportId, activityId, objectId, tabIndex));
//                        } else {
//                            // String sportName = sportDetails.getLocaleData().get(BundleManager.getLanguage()).getNazev();
//                            String sportName = objectSport.getSport().getLocaleData().getFirst().getNazev();
//                            logger.error("U cinnosti " + sportName + " chybi aktivita!");
//                        }
//                    }
//                } catch (Exception ex) {
//                    logger.error("Nepodarilo se ziskat objekt s ID " + objectId + "!", ex);
//                }
//            }
//        }
//
//        try {
//            List<ActivityWebTabEntity> oldEntities = activityWebTabService.findAll();
//            deleteOldEntities(oldEntities);
//        } catch (FinderException ex) {
//            logger.error("Nepodarilo se ziskat oblibene zalozky pro web!", ex);
//        }
//
//        createNewEntities(newEntities);
//    }


//    public void updateActivityForWebByObject(ObjektDto objekt) throws ApplicationException {
//        Set<Integer> tabIndexes = new HashSet<Integer>();
//        String objectId = objekt.getId();
//        try {
//            List<ActivityWebTabEntity> oldEntities = activityWebTabService.findByObject(objectId);
//            for (ActivityWebTabEntity oldEntity : oldEntities) {
//                tabIndexes.add(oldEntity.getTabIndex());
//            }
//            deleteOldEntities(oldEntities);
//        } catch (FinderException ex) {
//            logger.error("Nepodarilo se ziskat oblibene zalozky pro web!", ex);
//        }
//
//        if (!tabIndexes.isEmpty()) {
//            List<ActivityWebTabDto> newEntities = new ArrayList<ActivityWebTabDto>();
//            if (objektService.getSports(objekt) != null) {
//                for (SportEntity sport : objektService.getSports(objekt)) {
//                    String sportId = sport.getId();
//                    String activityId = sport.getActivity().getId();
//                    for (Integer tabIndex : tabIndexes) {
//                        newEntities.add(new ActivityWebTabDto(null, sportId, activityId, objectId, tabIndex));
//                    }
//                }
//            }
//            createNewEntities(newEntities);
//        }
//    }

    public void deleteSportFromWebTab(String sportId) {
        try {
            List<ActivityWebTabEntity> oldEntities = activityWebTabService.findBySport(sportId);
            deleteOldEntities(oldEntities);
        } catch (FinderException ex) {
            logger.error("Nepodarilo se ziskat oblibene zalozky pro web!", ex);
        }
    }

    private void deleteOldEntities(List<ActivityWebTabEntity> oldEntities) {
        for (ActivityWebTabEntity oldEntity : oldEntities) {
            try {
                delete(oldEntity);
            } catch (Exception ex) {
                logger.error("Nepodarilo se odstranit oblibenou zalozku!", ex);
            }
        }
    }

    private void createNewEntities(List<ActivityWebTabDto> newEntities) {
        for (ActivityWebTabDto newEntity : newEntities) {
            try {
                create(newEntity);
            } catch (CreateException ex) {
                logger.error("Nepodarilo se vytvorit");
            }
        }
    }

    public ActivityWebTabDto mapToDto (ActivityWebTabEntity entity) {
        return activityWebTabMapper.toDto(entity);
    }

    public List<ActivityWebTabDto> findAll() throws FinderException {
        return activityWebTabService.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findBySport(String sportId) throws FinderException {
        return activityWebTabService.findBySport(sportId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findByActivity(String activityId) throws FinderException {
        return activityWebTabService.findByActivity(activityId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findByObject(String objectId) throws FinderException {
        return activityWebTabService.findByObject(objectId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
