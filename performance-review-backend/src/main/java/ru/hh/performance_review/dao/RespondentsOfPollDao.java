package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.hh.performance_review.dao.base.CommonDao;

@Repository
public class RespondentsOfPollDao extends CommonDao {
    public RespondentsOfPollDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
