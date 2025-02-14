package nl.minvenj.nfi.websockets.db.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.minvenj.nfi.websockets.db.domain.ForensicCase;

@DataJpaTest
class ForensicCaseRepoIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ForensicCaseRepo forensicCaseRepo;

    @BeforeEach
    void setup() {
        final ForensicCase forensicCase = new ForensicCase("2020.01.01.001",
                                                           "HFGT6574NL",
                                                           "ACTIVE",
                                                           "M. VlaFlip");
        entityManager.persistAndFlush(forensicCase);
    }

    @Test
    void findAll() {
        assertEquals(1, forensicCaseRepo.findAll().size());
    }
}