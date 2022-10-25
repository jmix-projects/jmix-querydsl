package io.jmix.querydsl;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import io.jmix.core.*;
import io.jmix.querydsl.domain.*;
import io.jmix.querydsl.fetchplan.TypedFetchPlanFactory;
import io.jmix.querydsl.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JmixBaseTest extends AbstractJmixTest {
    @Autowired
    private UnconstrainedDataManager dataManager;
    @Autowired
    private Metadata metadata;
    @Autowired
    private CatRepository catRepository;
    @Autowired
    private TypedFetchPlanFactory fetchPlanFactory;
    @Autowired
    private EntityStates entityStates;
    @Autowired
    private FetchPlanRepository fetchPlanRepository;
    @Autowired
    private JmixQuerydslFactory querydslFactory;
    @PersistenceContext
    private EntityManager entityManager;

//    @AfterEach
//    public void classTearDown() throws Exception {
//    }

    @Override
    protected JPQLQuery<?> query() {
        return new JmixQuerydslQuery<>(dataManager, metadata, fetchPlanRepository);
    }

    @Override
    protected JPQLQuery<?> testQuery() {
        return new JmixQuerydslQuery<>(dataManager, metadata, fetchPlanRepository, new DefaultQueryMetadata());
    }

    @Override
    protected void save(Object entity) {
        dataManager.save(entity);
    }

    @Test
    public void repoSelect() {
        QCat other = new QCat("other");

        List<Cat> result = catRepository.select()
                .where(cat.name.eq(select(other.name).from(other).where(other.name.indexOf("B").eq(0))))
                .fetch();
        assertEquals(savedCats.subList(0, 1), result);
    }

    @Test
    public void repoSelectProjection() {
        List<CatBrowse> cats = catRepository
                .select(new QCatBrowse(cat.name, cat.birthdate, cat.breed))
                .where(cat.name.eq("Bob123"))
                .fetch();
        assertEquals(1, cats.size());
        CatBrowse catBrowse = cats.get(0);
        assertEquals("Bob123", catBrowse.getName());
    }

    @Test
    public void repoCustomImpl() {
        List<CatBrowse> cats = catRepository.findCustomCats();
        assertEquals(6, cats.size());
    }

    @Test
    public void repoSelectTuple() {
        List<Tuple> cats = catRepository
                .select(cat.name, cat.birthdate, cat.breed)
                .where(cat.name.eq("Bob123"))
                .fetch();
        assertEquals(1, cats.size());
        Tuple catBrowse = cats.get(0);
        assertEquals("Bob123", catBrowse.get(cat.name));
    }

    @Test
    public void fetchPlan() {
        QCompany company = QCompany.company;
        FetchPlan loadedFetchPlan = fetchPlanFactory.fetchPlan(company, company.name).build();

        Company loadedCompany = querydslFactory.selectFrom(company).limit(1).fetchOne(loadedFetchPlan);

        assertNotNull(loadedCompany);
        IllegalStateException illegalStateException = assertThrows(
                IllegalStateException.class, loadedCompany::getRatingOrdinal);

        assertTrue(illegalStateException.getMessage().contains("Cannot get unfetched attribute"));
        assertFalse(entityStates.isLoaded(loadedCompany, company.ratingString.getMetadata().getName()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Cat getCat() {
        FetchPlan loadedFetchPlan = fetchPlanFactory.fetchPlan(cat, cat.name, cat.breed).build();

        Cat loadedCat = catRepository.select().where(cat.name.eq("Bob123")).fetchOne(loadedFetchPlan);
        return loadedCat;
    }


    @Test
    public void injectedJmixQuerydslFactory() {
        List<Cat> allCats = querydslFactory.selectFrom(cat).fetch();
        assertEquals(6, allCats.size());
    }
}
