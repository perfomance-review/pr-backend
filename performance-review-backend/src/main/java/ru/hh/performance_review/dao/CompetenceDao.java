package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.GenericDao;

@Repository
public class CompetenceDao extends GenericDao {
    public CompetenceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}