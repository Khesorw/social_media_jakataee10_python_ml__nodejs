

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;



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
