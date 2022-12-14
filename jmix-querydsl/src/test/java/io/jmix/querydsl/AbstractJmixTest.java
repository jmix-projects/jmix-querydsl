/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmix.querydsl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.group.QPair;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import io.jmix.querydsl.domain.*;
import io.jmix.querydsl.qcore.group.MockTuple;
import io.jmix.querydsl.qcore.targets.*;
import io.jmix.querydsl.qcore.types.Concatenation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.Calendar;
import java.util.Map.Entry;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static io.jmix.querydsl.qcore.targets.Target.*;

/**
 * @author tiwe
 *
 */
public abstract class AbstractJmixTest {

    private static final Expression<?>[] NO_EXPRESSIONS = new Expression<?>[0];

    private static final QCompany company = QCompany.company;

    private static final QAnimal animal = QAnimal.animal;

    protected static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    private static final BooleanExpression cond1 = cat.name.length().gt(0);

    private static final BooleanExpression cond2 = otherCat.name.length().gt(0);

    private static final Predicate condition = ExpressionUtils.and(
            (Predicate) ExpressionUtils.extract(cond1),
            (Predicate) ExpressionUtils.extract(cond2));

    private static final Date birthDate;

    private static final Date date;

    private static final java.sql.Time time;

    protected final List<Cat> savedCats = new ArrayList<Cat>();

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    protected abstract JPQLQuery<?> query();

    protected abstract JPQLQuery<?> testQuery();

    protected abstract void save(Object entity);

    @BeforeEach
    public void setUp() {
        if (query().from(cat).fetchCount() > 0) {
            savedCats.addAll(query().from(cat).orderBy(cat.intId.asc()).select(cat).fetch());
            return;
        }

        Cat prev = null;
        for (Cat cat : Arrays.asList(
                Cat.cat("Bob123", 1, 1.0),
                Cat.cat("Ruth123", 2, 2.0),
                Cat.cat("Felix123", 3, 3.0),
                Cat.cat("Allen123", 4, 4.0),
                Cat.cat("Mary_123", 5, 5.0))) {
            if (prev != null) {
                cat.addKitten(prev);
            }
            cat.setBirthdate(birthDate);
            cat.setDateField(date);
            cat.setTimeField(time);
            cat.setColor(Color.BLACK);
            cat.setMate(prev);
            save(cat);
            savedCats.add(cat);
            prev = cat;
        }

        Animal animal = Animal.animal(10);
        animal.setBodyWeight(10.5);
        save(animal);

        Cat cat = Cat.cat("Some", 6, 6.0);
        cat.setBirthdate(birthDate);
        save(cat);
        savedCats.add(cat);

        Show show = Show.show(1);
        save(show);

        Company company = Company.company();
        company.setName("1234567890123456789012345678901234567890"); // 40
        company.setIntId(1);
        company.setRatingOrdinal(CompanyRating.A);
        company.setRatingString(CompanyRating.AA);
        save(company);

        Employee employee = Employee.employee();
        employee.setIntId(1);
        employee.setLastName("Smith");
        save(employee);

        Employee employee2 = Employee.employee();
        employee2.setIntId(2);
        employee2.setLastName("Doe");
        save(employee2);

        save(Entity1.entity1(1));
        save(Entity1.entity1(2));
        save(Entity2.entity2(3));

        Numeric numeric = Numeric.numeric();
        numeric.setLongId(1L);
        numeric.setValue(BigDecimal.valueOf(26.9));
        save(numeric);
    }

    @AfterEach
    public void ternDown() {

    }

    @Test
    public void aggregates_list_max() {
        assertEquals(Integer.valueOf(6), query().from(cat).select(cat.intId.max()).fetchFirst());
    }

    @Test
    public void aggregates_list_min() {
        assertEquals(Integer.valueOf(1), query().from(cat).select(cat.intId.min()).fetchFirst());
    }

    @Test
    public void aggregates_uniqueResult_max() {
        assertEquals(Integer.valueOf(6), query().from(cat).select(cat.intId.max()).fetchFirst());
    }

    @Test
    public void aggregates_uniqueResult_min() {
        assertEquals(Integer.valueOf(1), query().from(cat).select(cat.intId.min()).fetchFirst());
    }

    @Test
    public void alias() {
        assertEquals(6, query().from(cat).select(cat.id.as(cat.id)).fetch().size());
    }

    @Test
    public void any_and_gt() {
        assertEquals(0, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.gt(10.0)).fetchCount());
    }

    @Test
    public void any_and_lt() {
        assertEquals(1, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.lt(10.0)).fetchCount());
    }

    @Test
    public void any_in_order() {
        assertFalse(query().from(cat).orderBy(cat.kittens.any().name.asc()).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in_projection() {
        assertFalse(query().from(cat).select(cat.kittens.any()).fetch().isEmpty());
    }

    @Test
    public void any_in_projection2() {
        assertFalse(query().from(cat).select(cat.kittens.any().name).fetch().isEmpty());
    }

    @Test
    public void any_in_projection3() {
        assertFalse(query().from(cat).select(cat.kittens.any().name).fetch().isEmpty());
    }

    @Test
    public void any_in1() {
        //select cat from Cat cat where exists (
        //  select cat_kittens from Cat cat_kittens where cat_kittens member of cat.kittens and cat_kittens in ?1)
        assertFalse(query().from(cat).where(cat.kittens.any().in(savedCats)).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in11() {
        List<Integer> ids = Lists.newArrayList();
        for (Cat cat : savedCats) {
            ids.add(cat.getIntId());
        }
        assertFalse(query().from(cat).where(cat.kittens.any().intId.in(ids)).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in2() {
        assertFalse(query().from(cat).where(
                cat.kittens.any().in(savedCats),
                cat.kittens.any().in(savedCats.subList(0, 1)).not())
                .select(cat).fetch().isEmpty());
    }

    @Test
    public void any_simple() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).fetchCount());
    }

    @Test
    @Disabled("Jmix doesn't support generated JPQL.")
    public void any_any() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().kittens.any().name.eq("Ruth123")).fetchCount());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void arrayProjection() {
        List<String[]> results = query().from(cat)
                .select(new ArrayConstructorExpression<String>(String[].class, cat.name)).fetch();
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void as() {
        assertTrue(query().from(QAnimal.animal.as(QCat.class)).fetchCount() > 0);
    }

    @Test
    public void between() {
        assertEquals(ImmutableList.of(2, 3, 4, 5),
                query().from(cat).where(cat.intId.between(2, 5)).orderBy(cat.intId.asc()).select(cat.intId).fetch());
    }

    @Test
    public void case1() {
        assertEquals(ImmutableList.of(1, 2, 2, 2, 2, 2),
                query().from(cat).orderBy(cat.intId.asc())
                        .select(cat.name.when("Bob123").then(1).otherwise(2)).fetch());
    }

    @Test
    public void case1_long() {
        assertEquals(ImmutableList.of(1L, 2L, 2L, 2L, 2L, 2L),
                query().from(cat).orderBy(cat.intId.asc())
                        .select(cat.name.when("Bob123").then(1L).otherwise(2L)).fetch());
        List<Integer> rv = query().from(cat).select(cat.name.when("Bob").then(1).otherwise(2)).fetch();
        assertInstancesOf(Integer.class, rv);
    }

    @Test
    @Disabled("Jmix doesn't support")
    public void case1_date() {
        List<LocalDate> rv = query().from(cat).select(cat.name.when("Bob").then(LocalDate.now())
                .otherwise(LocalDate.now().plusDays(1))).fetch();
        assertInstancesOf(LocalDate.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    @Disabled("Jmix doesn't support")
    public void case1_date2() {
        List<java.sql.Date> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Date(0))
                .otherwise(new java.sql.Date(0))).fetch();
        assertInstancesOf(java.sql.Date.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    @Disabled("Jmix doesn't support")
    public void case1_time2() {
        List<java.sql.Time> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Time(0))
                .otherwise(new java.sql.Time(0))).fetch();
        assertInstancesOf(java.sql.Time.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    @Disabled("Jmix doesn't support")
    public void case1_timestamp2() {
        List<java.sql.Timestamp> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Timestamp(0))
                .otherwise(new java.sql.Timestamp(0))).fetch();
        assertInstancesOf(java.sql.Timestamp.class, rv);
    }

    @Test
    public void case2() {
        assertEquals(ImmutableList.of(4, 4, 4, 4, 4, 4),
                query().from(cat)
                        .select(Expressions.cases().when(cat.toes.eq(2)).then(cat.intId.multiply(2))
                                .when(cat.toes.eq(3)).then(cat.intId.multiply(3))
                                .otherwise(4)).fetch());
    }

    @Test
    public void case3() {
        assertEquals(ImmutableList.of(4, 4, 4, 4, 4, 4),
                query().from(cat).select(Expressions.cases()
                        .when(cat.toes.in(2, 3)).then(cat.intId.multiply(cat.toes))
                        .otherwise(4)).fetch());
    }

    @Test
    public void case5() {
        assertEquals(Arrays.asList(1, 0, 1, 1, 1, 1),
                query().from(cat).orderBy(cat.id.asc())
                        .select(cat.mate.when(savedCats.get(0)).then(0).otherwise(1)).fetch());
    }

    private static <T> void assertInstancesOf(Class<T> clazz, Iterable<T> rows) {
        for (T row : rows) {
            assertEquals(clazz, row.getClass(), row.toString());
        }
    }

    @Test
    public void caseBuilder() {
        QCat cat2 = new QCat("cat2");
        NumberExpression<Integer> casex = new CaseBuilder()
                .when(cat.weight.isNull().and(cat.weight.isNull())).then(0)
                .when(cat.weight.isNull()).then(cat2.weight)
                .when(cat2.weight.isNull()).then(cat.weight)
                .otherwise(cat.weight.add(cat2.weight));

        query().from(cat, cat2).orderBy(casex.asc()).select(cat.id, cat2.id).fetch();
        query().from(cat, cat2).orderBy(casex.desc()).select(cat.id, cat2.id).fetch();
    }

    @Test
    public void cast() {
        List<Cat> cats = query().from(cat).select(cat).fetch();
        List<Integer> weights = query().from(cat).select(cat.bodyWeight.castToNum(Integer.class)).fetch();
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf(cats.get(i).getBodyWeight().intValue()), weights.get(i));
        }
    }

    @Test
    @ExcludeIn(SQLSERVER)
    @Disabled("Jmix doesn't support")
    public void cast_toString() {
        for (Tuple tuple : query().from(cat).select(cat.breed, cat.breed.stringValue()).fetch()) {
            assertEquals(
                    tuple.get(cat.breed).toString(),
                    tuple.get(cat.breed.stringValue()));
        }
    }

    @Test
    @ExcludeIn(SQLSERVER)
    @Disabled("Will be resolved later")
    public void cast_toString_append() {
        List<Tuple> query = query().from(cat)
                .select(cat.breed, cat.breed.stringValue().append("test"))
                .fetch();

        for (Tuple tuple : query) {
            assertEquals(
                    tuple.get(cat.breed).toString() + "test",
                    tuple.get(cat.breed.stringValue().append("test")));
        }
    }

    @Test
    public void collection_predicates() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Predicate> predicates = Collections.emptyList();
        for (Predicate pred : predicates) {
            System.err.println(pred);
            query().from(cat).where(pred).select(cat).fetch();
        }
    }

    @Test
    public void collection_projections() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Expression<?>> projections = Collections.emptyList();
        for (Expression<?> proj : projections) {
            System.err.println(proj);
            query().from(cat).select(proj).fetch();
        }
    }

    @Test
    public void constant() {
        //select cat.id, ?1 as const from Cat cat
        List<Cat> cats = query().from(cat).select(cat).fetch();
        Path<String> path = Expressions.stringPath("const");
        List<Tuple> tuples = query().from(cat).select(cat.intId, Expressions.constantAs("abc", path)).fetch();
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf(cats.get(i).getIntId()), tuples.get(i).get(cat.intId));
            assertEquals("abc", tuples.get(i).get(path));
        }
    }

    @Test
    public void constant2() {
        assertFalse(query().from(cat).select(cat.id, Expressions.constant("name")).fetch().isEmpty());
    }

    @Test
    public void constructorProjection() {
        List<Projection> projections = query().from(cat)
                .select(Projections.constructor(Projection.class, cat.name, cat)).fetch();
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    @Disabled("Will be")
    public void contains_ic() {
        QFoo foo = QFoo.foo;
        assertEquals(1, query().from(foo).where(foo.bar.containsIgnoreCase("M??nchen")).fetchCount());
    }

    @Test
    public void contains1() {
        assertEquals(1, query().from(cat).where(cat.name.contains("eli")).fetchCount());
    }

    @Test
    @Disabled("Jmix doesn't support")
    public void contains2() {
        assertEquals(1L, query().from(cat).where(cat.kittens.contains(savedCats.get(0))).fetchCount());
    }

    @Test
    public void contains3() {
        assertEquals(1L, query().from(cat).where(cat.name.contains("_")).fetchCount());
    }

    @Test
    public void count() {
        QShow show = QShow.show;
        assertTrue(query().from(show).fetchCount() > 0);
    }

    @Test
    public void count_distinct() {
        QCat cat = QCat.cat;
        query().from(cat)
               .groupBy(cat.id)
               .select(cat.id, cat.breed.countDistinct()).fetch();
    }

    @Test
    @NoBatooJPA
    @NoHibernate
    public void count_distinct2() {
        QCat cat = QCat.cat;
        query().from(cat)
               .groupBy(cat.id)
               .select(cat.id, cat.birthdate.dayOfMonth().countDistinct()).fetch();
    }

    @Test
    @NoEclipseLink
    @ExcludeIn(SQLSERVER)
    @Disabled("Jmix doesn't support")
    public void distinct_orderBy() {
        QCat cat = QCat.cat;
        List<Tuple> result = query().select(cat.id, cat.mate.id)
                .distinct()
                .from(cat)
                .orderBy(
                        cat.mate.id.asc().nullsFirst(),
                        cat.id.asc().nullsFirst()
                ).fetch();
        assertThat(result, Matchers.<Tuple>contains(
                new MockTuple(new Object[]{1, null}),
                new MockTuple(new Object[]{6, null}),
                new MockTuple(new Object[]{2, 1}),
                new MockTuple(new Object[]{3, 2}),
                new MockTuple(new Object[]{4, 3}),
                new MockTuple(new Object[]{5, 4})
        ));
    }

    @Test
    @NoHibernate
    @ExcludeIn(MYSQL)
//    @Disabled("Will be")
    public void distinct_orderBy2() {
        QCat cat = QCat.cat;
        List<Tuple> result = query().select(cat.intId, cat.mate.intId)
                .distinct()
                .from(cat)
                .orderBy(cat.mate.intId.asc().nullsFirst()).fetch();
        assertThat(result, Matchers.contains(
                new MockTuple(new Object[]{2, 1}),
                new MockTuple(new Object[]{3, 2}),
                new MockTuple(new Object[]{4, 3}),
                new MockTuple(new Object[]{5, 4})
        ));
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void count_distinct3() {
        QCat kitten = new QCat("kitten");
        assertEquals(4, query().from(cat).leftJoin(cat.kittens, kitten).select(kitten.countDistinct()).fetchOne().intValue());
        assertEquals(6, query().from(cat).leftJoin(cat.kittens, kitten).select(kitten.countDistinct()).fetchCount());
    }

    @Test
    public void distinctResults() {
        System.out.println("-- fetch results");
        QueryResults<Date> res = query().from(cat).limit(2).select(cat.birthdate).fetchResults();
        assertEquals(2, res.getResults().size());
        assertEquals(6L, res.getTotal());
        System.out.println();

        System.out.println("-- fetch distinct results");
        res = query().from(cat).limit(2).distinct().select(cat.birthdate).fetchResults();
        assertEquals(1, res.getResults().size());
        assertEquals(1L, res.getTotal());
        System.out.println();

        System.out.println("-- fetch distinct");
        assertEquals(1, query().from(cat).distinct().select(cat.birthdate).fetch().size());
    }

    @Test
    public void date() {
        assertEquals(2000, query().from(cat).select(cat.birthdate.year()).fetchFirst().intValue());
        assertEquals(200002, query().from(cat).select(cat.birthdate.yearMonth()).fetchFirst().intValue());
        assertEquals(2, query().from(cat).select(cat.birthdate.month()).fetchFirst().intValue());
        assertEquals(2, query().from(cat).select(cat.birthdate.dayOfMonth()).fetchFirst().intValue());
        assertEquals(3, query().from(cat).select(cat.birthdate.hour()).fetchFirst().intValue());
        assertEquals(4, query().from(cat).select(cat.birthdate.minute()).fetchFirst().intValue());
        assertEquals(0, query().from(cat).select(cat.birthdate.second()).fetchFirst().intValue());
    }

    @Test
    @NoEclipseLink({DERBY, HSQLDB})
    @NoHibernate({DERBY, POSTGRESQL, SQLSERVER})
    public void date_yearWeek() {
        int value = query().from(cat).select(cat.birthdate.yearWeek()).fetchFirst();
        assertTrue(value == 200006 || value == 200005);
    }

    @Test
    @NoEclipseLink({DERBY, HSQLDB})
    @NoHibernate({DERBY, POSTGRESQL, SQLSERVER})
    public void date_week() {
        int value = query().from(cat).select(cat.birthdate.week()).fetchFirst();
        assertTrue(value == 6 || value == 5);
    }

    @Test
    public void endsWith() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("h123")).fetchCount());
    }

    @Test
    public void endsWith_ignoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.endsWithIgnoreCase("H123")).fetchCount());
    }

    @Test
    public void endsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.endsWith("X")).fetchCount());
    }

    @Test
    public void endsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("_123")).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void enum_eq() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.eq(CompanyRating.A.getId())).fetchCount());
        assertEquals(1, query().from(company).where(company.ratingString.eq(CompanyRating.AA.getId())).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void enum_in() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.in(CompanyRating.A.getId(), CompanyRating.AA.getId())).fetchCount());
        assertEquals(1, query().from(company).where(company.ratingString.in(CompanyRating.A.getId(), CompanyRating.AA.getId())).fetchCount());
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void enum_startsWith() {
        assertEquals(1, query().from(company).where(company.ratingString.stringValue().startsWith("A")).fetchCount());
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void factoryExpressions() {
        QCat cat = QCat.cat;
        QCat cat2 = new QCat("cat2");
        QCat kitten = new QCat("kitten");
        JPQLQuery<Tuple> query = query().from(cat)
                .leftJoin(cat.mate, cat2)
                .leftJoin(cat2.kittens, kitten)
                .select(Projections.tuple(cat.id, new QFamily(cat, cat2, kitten).skipNulls()));
        assertEquals(6, query.fetch().size());
        assertNotNull(query.limit(1).fetchOne());
    }

    @Test
    public void simpleQueryProjection() {
        List<CatBrowse> cats = query().from(cat)
                .select(new QCatBrowse(cat.name, cat.birthdate, cat.breed)).where(cat.name.eq("Bob123")).fetch();
        assertEquals(1, cats.size());
    }

    @Test
    @ExcludeIn({MYSQL, DERBY})
    @NoBatooJPA
    @Disabled("Will be")
    public void groupBy() {
        QAuthor author = QAuthor.author;
        QBook book = QBook.book;

        for (int i = 0; i < 10; i++) {
            Author a = new Author();
            a.setName(String.valueOf(i));
            save(a);
            for (int j = 0; j < 2; j++) {
                Book b = new Book();
                b.setTitle(String.valueOf(i) + " " + String.valueOf(j));
                b.setAuthor(a);
                save(b);
            }
        }

        Map<Long, List<Pair<Long, String>>> map = query()
            .from(author)
            .join(author.books, book)
            .transform(GroupBy
                    .groupBy(author.id)
                    .as(GroupBy.list(QPair.create(book.id, book.title))));

        for (Entry<Long, List<Pair<Long, String>>> entry : map.entrySet()) {
            System.out.println("author = " + entry.getKey());

            for (Pair<Long,String> pair : entry.getValue()) {
                System.out.println("  book = " + pair.getFirst() + "," + pair.getSecond());
            }
        }
    }

    @Test
    public void groupBy2() {
//        select cat0_.name as col_0_0_, cat0_.breed as col_1_0_, sum(cat0_.bodyWeight) as col_2_0_
//        from animal_ cat0_ where cat0_.DTYPE in ('C', 'DC') and cat0_.bodyWeight>?
//        group by cat0_.name , cat0_.breed
        query().from(cat)
            .where(cat.bodyWeight.gt(0))
            .groupBy(cat.name, cat.breed)
            .select(cat.name, cat.breed, cat.bodyWeight.sum()).fetch();
    }

    @Test
    @NoEclipseLink
    @Disabled("Jmix doesn't support")
    public void groupBy_yearMonth() {
        query().from(cat)
               .groupBy(cat.birthdate.yearMonth())
               .orderBy(cat.birthdate.yearMonth().asc())
               .select(cat.id.count()).fetch();
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-1902
    public void groupBy_select() {
        // select length(my_column) as column_size from my_table group by column_size
        NumberPath<Integer> length = Expressions.numberPath(Integer.class, "len");
        assertEquals(ImmutableList.of(4, 6, 7, 8),
                query().select(cat.name.length().as(length)).from(cat).orderBy(length.asc()).groupBy(length).fetch());
    }

    @Test
    public void groupBy_results() {
        QueryResults<UUID> results = query().from(cat).groupBy(cat.id).select(cat.id).fetchResults();
        assertEquals(6, results.getTotal());
        assertEquals(6, results.getResults().size());
    }

    @Test
    public void groupBy_results2() {
        QueryResults<Integer> results = query().from(cat).groupBy(cat.birthdate).select(cat.intId.max()).fetchResults();
        assertEquals(1, results.getTotal());
        assertEquals(1, results.getResults().size());
    }

    @Test
    public void in() {
        assertEquals(3L, query().from(cat).where(cat.name.in("Bob123", "Ruth123", "Felix123")).fetchCount());
        assertEquals(3L, query().from(cat).where(cat.intId.in(Arrays.asList(1, 2, 3))).fetchCount());
        assertEquals(0L, query().from(cat).where(cat.name.in(Arrays.asList("A", "B", "C"))).fetchCount());
    }

    @Test
    public void in2() {
        assertEquals(3L, query().from(cat).where(cat.intId.in(1, 2, 3)).fetchCount());
        assertEquals(0L, query().from(cat).where(cat.name.in("A", "B", "C")).fetchCount());
    }

    @Test
    public void in3() {
        assertEquals(0, query().from(cat).where(cat.name.in("A,B,C".split(","))).fetchCount());
    }

    @Test
    @Disabled("Jmix doesn't support")
    public void in4() {
        //$.parameterRelease.id.eq(releaseId).and($.parameterGroups.any().id.in(filter.getGroups()));
        assertEquals(Arrays.asList(),
                query().from(cat).where(cat.intId.eq(1), cat.kittens.any().intId.in(1, 2, 3)).select(cat).fetch());
    }

    @Test
    public void in5() {
        assertEquals(4L, query().from(cat).where(cat.mate.in(savedCats)).fetchCount());
    }

    @Test
    public void in7() {
        assertEquals(4L, query().from(cat).where(cat.kittens.any().in(savedCats)).fetchCount());
    }

    @Test
    public void in_empty() {
        assertEquals(0, query().from(cat).where(cat.name.in(ImmutableList.<String>of())).fetchCount());
    }

    @Test
    @NoOpenJPA
    public void indexOf() {
        assertEquals(Integer.valueOf(0), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("B")).fetchFirst());
    }

    @Test
    @NoOpenJPA
    public void indexOf2() {
        assertEquals(Integer.valueOf(1), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("o")).fetchFirst());
    }

    @Test
    public void instanceOf_cat() {
        assertEquals(6L, query().from(cat).where(cat.instanceOf(Cat.class)).fetchCount());
    }

    @Test
    public void instanceOf_domesticCat() {
        assertEquals(0L, query().from(cat).where(cat.instanceOf(DomesticCat.class)).fetchCount());
    }

    @Test
    public void instanceOf_entity1() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(2L, query().from(entity1).where(entity1.instanceOf(Entity1.class)).fetchCount());
    }

    @Test
    public void instanceOf_entity2() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(1L, query().from(entity1).where(entity1.instanceOf(Entity2.class)).fetchCount());
    }

    @Test
    public void isEmpty_relation() {
        assertEquals(6L, query().from(cat).where(cat.kittensSet.isEmpty()).fetchCount());
    }

    @Test
    public void length() {
        assertEquals(6, query().from(cat).where(cat.name.length().gt(0)).fetchCount());
    }

    @Test
    public void like() {
        assertEquals(0, query().from(cat).where(cat.name.like("!")).fetchCount());
        assertEquals(0, query().from(cat).where(cat.name.like("\\")).fetchCount());
    }

    @Test
    public void limit() {
        List<String> names1 = Arrays.asList("Allen123","Bob123");
        assertEquals(names1, query().from(cat).orderBy(cat.name.asc()).limit(2).select(cat.name).fetch());
    }

    @Test
    public void limit_and_offset() {
        List<String> names3 = Arrays.asList("Felix123", "Mary_123");
        assertEquals(names3, query().from(cat).orderBy(cat.name.asc()).limit(2).offset(2).select(cat.name).fetch());
    }

    @Test
    public void limit2() {
        assertEquals(Collections.singletonList("Allen123"),
                query().from(cat).orderBy(cat.name.asc()).limit(1).select(cat.name).fetch());
    }

    @Test
    public void limit3() {
        assertEquals(6, query().from(cat).limit(Long.MAX_VALUE).select(cat).fetch().size());
    }

    @Test
    @Disabled("Jmix doesn't support. Generate strange jpql query")
    @NoEclipseLink(HSQLDB)
    public void list_order_get() {
        QCat cat = QCat.cat;
        assertEquals(6, query().from(cat).orderBy(cat.kittens.get(0).name.asc()).fetch().size());
    }

    @Test
    @Disabled("Jmix doesn't support. Generate strange jpql query")
    @NoEclipseLink(HSQLDB)
    public void list_order_get2() {
        QCat cat = QCat.cat;
        assertEquals(6, query().from(cat).orderBy(cat.mate.kittens.get(0).name.asc()).fetch().size());
    }

    @Test
    public void max() {
        assertEquals(6.0, query().from(cat).select(cat.bodyWeight.max()).fetchFirst(), 0.0001);
    }

    @Test
    public void min() {
        assertEquals(1.0, query().from(cat).select(cat.bodyWeight.min()).fetchFirst(), 0.0001);
    }

    @Test
//    @Disabled("Will be")
    public void nestedProjection() {
        Concatenation concat = new Concatenation(cat.name, cat.name);
        List<Tuple> tuples = query().from(cat).select(cat.name, concat).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertEquals(
                tuple.get(concat),
                tuple.get(cat.name) + tuple.get(cat.name));
        }
    }

    @Test
    public void not_in() {
        long all = query().from(cat).fetchCount();
        assertEquals(all - 3L, query().from(cat).where(cat.name.notIn("Bob123", "Ruth123", "Felix123")).fetchCount());

        assertEquals(3L, query().from(cat).where(cat.intId.notIn(1, 2, 3)).fetchCount());
        assertEquals(6L, query().from(cat).where(cat.name.notIn("A", "B", "C")).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void not_in_empty() {
        long count = query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).where(cat.name.notIn(Collections.<String>emptyList())).fetchCount());
    }

    @Test
    public void null_as_uniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat).fetchFirst());
    }

    @Test
    @NoEclipseLink
    public void numeric() {
        QNumeric numeric = QNumeric.numeric;
        BigDecimal singleResult = query().from(numeric).select(numeric.value).fetchFirst();
        assertEquals(26.9, singleResult.doubleValue(), 0.001);
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void offset1() {
        List<String> names2 = Arrays.asList("Bob123", "Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(1).select(cat.name).fetch());
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void offset2() {
        List<String> names2 = Arrays.asList("Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(2).select(cat.name).fetch());
    }

    @Test
    public void one_to_one() {
        QEmployee employee = QEmployee.employee;
        QUser user = QUser.user;

        JPQLQuery<?> query = query();
        query.from(employee);
        query.innerJoin(employee.user, user);
        query.select(employee).fetch();
    }

    @Test
    public void order() {
        NumberPath<Double> weight = Expressions.numberPath(Double.class, "weight");
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                query().from(cat).orderBy(weight.asc()).select(cat.bodyWeight.as(weight)).fetch());
    }

    @Test
    public void order_by_count() {
        NumberPath<Long> count = Expressions.numberPath(Long.class, "c");
        query().from(cat)
            .groupBy(cat.id)
            .orderBy(count.asc())
            .select(cat.id, cat.id.count().as(count)).fetch();
    }

    @Test
    public void order_stringValue() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.intId.stringValue().asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_to_integer() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.intId.stringValue().castToNum(Integer.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_toLong() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.intId.stringValue().castToNum(Long.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_toBigInteger() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.intId.stringValue().castToNum(BigInteger.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(SQLSERVER)
    public void order_nullsFirst() {
        assertNull(query().from(cat)
                .orderBy(cat.dateField.asc().nullsFirst())
                .select(cat.dateField).fetchFirst());
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(SQLSERVER)
    public void order_nullsLast() {
        assertNotNull(query().from(cat)
                .orderBy(cat.dateField.asc().nullsLast())
                .select(cat.dateField).fetchFirst());
    }

    @Test
    public void params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123", query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .select(cat.name).fetchFirst());
    }

    @Test
    public void params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .select(cat.name).fetchFirst());
    }

    @Test
    public void params_not_set() {
        assertThrows(ParamNotSetException.class, () -> {
            Param<String> name = new Param<String>(String.class, "name");
            assertEquals("Bob123", query().from(cat).where(cat.name.eq(name)).select(cat.name).fetchFirst());
        });
    }

    @Test
    public void precedence() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123"))
                      .or(str.like("Ruth%").and(str.like("%uth123")));
        assertEquals(2L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void precedence2() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123")
                      .or(str.like("Ruth%"))).and(str.like("%uth123"));
        assertEquals(0L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void precedence3() {
        Predicate where = cat.name.eq("Bob123").and(cat.intId.eq(1))
                      .or(cat.name.eq("Ruth123").and(cat.intId.eq(2)));
        assertEquals(2L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void factoryExpression_in_groupBy() {
        Expression<Cat> catBean = Projections.bean(Cat.class, cat.id, cat.name);
        assertFalse(query().from(cat).groupBy(catBean).select(catBean).fetch().isEmpty());
    }

    @Test
    public void startsWith() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("R")).fetchCount());
    }

    @Test
    public void startsWith_ignoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.startsWithIgnoreCase("r")).fetchCount());
    }

    @Test
    public void startsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.startsWith("X")).fetchCount());
    }

    @Test
    public void startsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("Mary_")).fetchCount());
    }

    @Test
    @ExcludeIn({MYSQL, SQLSERVER, TERADATA})
    @NoOpenJPA
    public void stringOperations() {
        // NOTE : locate in MYSQL is case-insensitive
        assertEquals(0, query().from(cat).where(cat.name.startsWith("r")).fetchCount());
        assertEquals(0, query().from(cat).where(cat.name.endsWith("H123")).fetchCount());
        assertEquals(Integer.valueOf(2), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("b")).fetchFirst());
    }

    @Test
    public void subQuery() {
        QShow show = QShow.show;
        QShow show2 = new QShow("show2");
        assertEquals(0,
                query().from(show).where(select(show2.count()).from(show2)
                        .where(show2.id.ne(show.id)).gt(0L)).fetchCount());
    }

    @Test
    public void subQuery2() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        assertEquals(savedCats, query().from(cat)
                .where(cat.name.in(select(other.name).from(other)
                        .groupBy(other.name)))
                .orderBy(cat.intId.asc())
                .select(cat).fetch());
    }

    @Test
    public void subQuery3() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        assertEquals(savedCats.subList(0, 1), query().from(cat)
                .where(cat.name.eq(select(other.name).from(other)
                        .where(other.name.indexOf("B").eq(0))))
                .select(cat).fetch());
    }

    @Test
    public void subQuery4() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        query().from(cat)
                .select(cat.name, select(other.count()).from(other).where(other.name.eq(cat.name))).fetch();
    }

    @Test
    public void subQuery5() {
        QEmployee employee = QEmployee.employee;
        QEmployee employee2 = new QEmployee("e2");
        assertEquals(2, query().from(employee)
                .where(select(employee2.id.count()).from(employee2).gt(1L))
                .fetchCount());
    }

    @Test
    public void substring() {
        for (String str : query().from(cat).select(cat.name.substring(1,2)).fetch()) {
            assertEquals(1, str.length());
        }
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(ORACLE)
    public void substring2() {
        setUp();

        QCompany company = QCompany.company;
        StringExpression name = company.name;
        Integer companyId = query().from(company).select(company.intId).fetchFirst();
        JPQLQuery<?> query = query().from(company).where(company.intId.eq(companyId));
        String str = query.select(company.name).fetchFirst();

        assertEquals(Integer.valueOf(29),
                query.select(name.length().subtract(11)).fetchFirst());

        assertEquals(str.substring(0, 7),
                query.select(name.substring(0, 7)).fetchFirst());

        assertEquals(str.substring(15),
                query.select(name.substring(15)).fetchFirst());

        assertEquals(str.substring(str.length()),
                query.select(name.substring(name.length())).fetchFirst());

        assertEquals(str.substring(str.length() - 11),
                query.select(name.substring(name.length().subtract(11))).fetchFirst());
    }

    @Test
    @ExcludeIn({HSQLDB, DERBY})
    public void substring_from_right2() {
        assertEquals(Collections.emptyList(), query().from(cat)
                .where(cat.name.substring(cat.name.length().subtract(1), cat.name.length())
                        .eq(cat.name.substring(cat.name.length().subtract(2), cat.name.length().subtract(1))))
                .select(cat).fetch());
    }

    @Test
    public void sum_3() {
        assertEquals(21.0, query().from(cat).select(cat.bodyWeight.sum()).fetchFirst(), 0.0001);
    }

    @Test
    public void sum_3_projected() {
        double val = query().from(cat).select(cat.bodyWeight.sum()).fetchFirst();
        DoubleProjection projection = query().from(cat)
                .select(new QDoubleProjection(cat.bodyWeight.sum())).fetchFirst();
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void sum_4() {
        Double dbl = query().from(cat).select(cat.bodyWeight.sum().negate()).fetchFirst();
        assertNotNull(dbl);
    }

    @Test
    public void sum_5() {
        QShow show = QShow.show;
        Long lng = query().from(show).select(show.lngId.sum()).fetchFirst();
        assertNotNull(lng);
    }

    @Test
    public void sum_of_integer() {
        QCat cat2 = new QCat("cat2");
        assertEquals(Collections.emptyList(), query().from(cat)
                .where(select(cat2.breed.sum())
                        .from(cat2).where(cat2.eq(cat.mate)).gt(0))
                .select(cat).fetch());
    }

    @Test
    public void sum_of_float() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
               .where(select(cat2.doubleProperty.sum())
                      .from(cat2).where(cat2.eq(cat.mate)).gt(0.0d))
               .select(cat).fetch();
    }

    @Test
    public void sum_of_double() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
               .where(select(cat2.bodyWeight.sum())
                      .from(cat2).where(cat2.eq(cat.mate)).gt(0.0))
               .select(cat).fetch();
    }

    @Test
    public void sum_as_float() {
        Double val = query().from(cat).select(cat.doubleProperty.sum()).fetchFirst();
        assertTrue(val > 0);
    }

    @Test
    public void sum_as_float_projected() {
        Double val = query().from(cat).select(cat.doubleProperty.sum()).fetchFirst();
        DoubleProjection projection = query().from(cat)
                .select(new QDoubleProjection(cat.doubleProperty.sum())).fetchFirst();
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void sum_as_float2() {
        Double val = query().from(cat).select(cat.doubleProperty.sum().negate()).fetchFirst();
        assertTrue(val < 0);
    }

    @Test
    public void sum_coalesce() {
        int val = query().from(cat).select(cat.weight.sum().coalesce(0)).fetchFirst();
        assertEquals(0, val);
    }

    @Test
    public void sum_noRows_double() {
        assertNull(query().from(cat)
                .where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat.bodyWeight.sum()).fetchFirst());
    }

    @Test
    public void sum_noRows_float() {
        assertNull(query().from(cat)
                .where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat.doubleProperty.sum()).fetchFirst());
    }

    @Test
    public void tupleProjection() {
        List<Tuple> tuples = query().from(cat).select(cat.name, cat).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat));
        }
    }

    @Test
    public void tupleProjection_as_queryResults() {
        QueryResults<Tuple> tuples = query().from(cat).limit(1)
                .select(cat.name, cat).fetchResults();
        assertEquals(1, tuples.getResults().size());
        assertTrue(tuples.getTotal() > 0);
    }

    @Test
    @ExcludeIn(DERBY)
    @Disabled("Will be")
    public void transform_groupBy() {
        QCat kitten = new QCat("kitten");
        Map<Integer, CatProjection> result = query().from(cat).innerJoin(cat.kittens, kitten)
            .transform(GroupBy.groupBy(cat.intId)
                    .as(Projections.constructor(CatProjection.class, cat.name, cat.id,
                            GroupBy.list(Projections.constructor(Cat.class, kitten.name, kitten.id)))));

        for (CatProjection entry : result.values()) {
            assertEquals(1, entry.getKittens().size());
        }
    }

    @Test
    @ExcludeIn(DERBY)
    @Disabled("Jmix doesn't support Cursors")
    public void transform_groupBy2() {
        QCat kitten = new QCat("kitten");
        Map<List<?>, Group> result = query().from(cat).innerJoin(cat.kittens, kitten)
                .transform(GroupBy.groupBy(cat.id, kitten.id)
                        .as(cat, kitten));

        assertFalse(result.isEmpty());
        for (Tuple row : query().from(cat).innerJoin(cat.kittens, kitten)
                .select(cat, kitten).fetch()) {
            assertNotNull(result.get(Arrays.asList(row.get(cat).getId(), row.get(kitten).getId())));
        }
    }

    @Test
    @ExcludeIn(DERBY)
    @Disabled("Support FetchableQuery.transform")
    public void transform_groupBy_alias() {
        QCat kitten = new QCat("kitten");
        SimplePath<Cat> k = Expressions.path(Cat.class, "k");
        Map<UUID, Group> result = query().from(cat).innerJoin(cat.kittens, kitten)
                .transform(GroupBy.groupBy(cat.id)
                        .as(cat.name, cat.id,
                                GroupBy.list(Projections.constructor(Cat.class, kitten.name, kitten.id).as(k))));

        for (Group entry : result.values()) {
            assertNotNull(entry.getOne(cat.id));
            assertNotNull(entry.getOne(cat.name));
            assertFalse(entry.getList(k).isEmpty());
        }
    }

    @Test
    @NoBatooJPA
    public void treat() {
        QDomesticCat domesticCat = QDomesticCat.domesticCat;
        assertEquals(0, query().from(cat)
                .innerJoin(cat.mate, domesticCat._super)
                .where(domesticCat.name.eq("Bobby"))
                .fetchCount());
    }

    @Test
    @Disabled
    public void type() {
        assertEquals(Arrays.asList("C","C","C","C","C","C","A"),
                query().from(animal).orderBy(animal.id.asc()).select(JPAExpressions.type(animal)).fetch());
    }

    @Test
    @NoOpenJPA
    public void type_order() {
        assertEquals(Arrays.asList(10, 1, 2, 3, 4, 5, 6),
                query().from(animal).orderBy(JPAExpressions.type(animal).asc(), animal.intId.asc())
                        .select(animal.intId).fetch());
    }
}

