

import jakarta.persistence.*;

import org.junit.jupiter.api.*;



public class TestUser {
    private static EntityManagerFactory emf;
    private EntityManager em;


    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("MyPu");
    }

    @BeforeEach
    void setup() {

        em = emf.createEntityManager();
    }

    @Test
    public void testQuery() {
        var list = em.createQuery("select t from TestUser t", TestUser.class).getResultList();
        list.forEach(System.out::println);
        Assertions.assertNotNull(list);
    }

    @AfterEach
    void tearDown() {
        em.close();
    }

    @AfterAll
    static void shutDown() {
        emf.close();
        
    }
}
