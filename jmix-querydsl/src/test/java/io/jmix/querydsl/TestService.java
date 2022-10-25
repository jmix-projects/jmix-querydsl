package io.jmix.querydsl;

import io.jmix.core.FetchPlan;
import io.jmix.querydsl.domain.*;
import io.jmix.querydsl.fetchplan.TypedFetchPlanFactory;
import io.jmix.querydsl.repository.CatRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class TestService {
    private final JmixQuerydslFactory querydslFactory;
    private final TypedFetchPlanFactory fetchPlanFactory;
    private final CatRepository catRepository;

    public TestService(JmixQuerydslFactory querydslFactory,
                       TypedFetchPlanFactory fetchPlanFactory, CatRepository catRepository) {
        this.querydslFactory = querydslFactory;
        this.fetchPlanFactory = fetchPlanFactory;
        this.catRepository = catRepository;
    }

    public List<Employee> findCeoEmployees(Collection<UUID> departmentIds) {
        QEmployee employee = new QEmployee("e");
        QCompany company = QCompany.company;

        FetchPlan fetchPlan = fetchPlanFactory.fetchPlan(employee, employee.firstName, employee.lastName)
                .property(employee.company, (qCompany, builder) -> builder.properties(qCompany.name, qCompany.ratingString))
                .build();

        return querydslFactory.select(employee)
                .from(company).join(company.ceo, employee)
                .where(company.departments.any().id.in(departmentIds))
                .orderBy(employee.firstName.asc())
                .fetch(fetchPlan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Company getCat() {
        QCompany company = QCompany.company;
        FetchPlan loadedFetchPlan = fetchPlanFactory.fetchPlan(company, company.name).build();

        return querydslFactory.selectFrom(company).limit(1).fetchOne(loadedFetchPlan);
    }
}
