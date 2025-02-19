package cz.inspire;

public class Main {
    private static final int ONE_MILLION = 1000000; // 1-5 minutes, dependent on PC
    private static final int ONE_BILLION = 1000000000; // Do not use if you don't have ~5hours
    private static final int BATCH_SIZE = 1000;

    public static void main(String[] args) {
        ///StressTestOfUUID(); -- Do not run if you don't have free time and running DB
    }


//    private static void StressTestOfUUID() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("your-persistence-unit");
//        EntityManager em = emf.createEntityManager();
//
//        Random rand = new Random();
//        int count = 0;
//
//        try {
//            em.getTransaction().begin();
//
//            for (int i = 0; i < ONE_MILLION; i++) {
//                HeaderEntity entity = new HeaderEntity();
//                entity.setField(rand.nextInt(1000));
//                entity.setLocation(rand.nextInt(1000));
//                em.persist(entity);
//
//                if (i % BATCH_SIZE == 0) {
//                    em.flush();
//                    em.clear();
//                    em.getTransaction().commit();
//                    em.getTransaction().begin();
//                }
//                count++;
//            }
//            em.flush();
//            em.clear();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//            System.err.println("We failed after " + count + " inserts: " + e.getMessage());
//        } finally {
//            em.close();
//            emf.close();
//        }
//    }
}