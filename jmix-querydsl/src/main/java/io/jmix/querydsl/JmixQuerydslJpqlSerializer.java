package io.jmix.querydsl;

import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;

import javax.persistence.EntityManager;

/**
 * {@code JmixJpqlSerializer} serializes Querydsl expressions into JPQL syntax.
 *
 * Based on {@link JPQLSerializer}
 */
public class JmixQuerydslJpqlSerializer extends JPQLSerializer {

    public static final String QUERY_PARAM_HOLDER = "qdslparam";


    public JmixQuerydslJpqlSerializer(JPQLTemplates templates) {
        super(templates);
    }

    public JmixQuerydslJpqlSerializer(JPQLTemplates templates, EntityManager em) {
        super(templates, em);
    }

    @Override
    protected void serializeConstant(int parameterIndex, String constantLabel) {
        append(":").append(QUERY_PARAM_HOLDER);
        append(constantLabel);
    }
}
