package ru.hh.performance_review.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.model.RoleEnum;
import ru.hh.performance_review.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UserDao extends CommonDao {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return getSession().createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public List<User> getRespondentsByManagerId(UUID userId) {

        return getSession().createQuery("SELECT u FROM User u " +
                                                  "WHERE u.role = :roleRespondent " +
                                                  "    AND u.leader.userId = :userId", User.class)
                .setParameter("roleRespondent", RoleEnum.RESPONDENT)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<User> getExcluded(List<UUID> includedIds, UUID currentUserId) {

        return getSession().createQuery("SELECT u " +
                                           "FROM User u " +
                                           "WHERE u.role = :paramRole AND u.id != :paramCurrentUser " +
                        "                      AND u.id not in (:paramParticipantsIds)", User.class )
                .setParameter("paramRole", RoleEnum.RESPONDENT)
                .setParameter("paramCurrentUser" , currentUserId)
                .setParameterList("paramParticipantsIds", includedIds)
                .getResultList();
    }

    public List<User> getIncluded(List<UUID> includedIds) {

        return getSession().createQuery("SELECT u " +
                                                  "FROM User u " +
                                                  "WHERE u.id in (:paramParticipantsIds)", User.class )
                .setParameterList("paramParticipantsIds", includedIds)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserEmail(String userEmail) {
        return getSession().createQuery(
                "SELECT u FROM User u " +
                        " WHERE u.email = :userEmail " +
                        "", User.class)
                .setParameter("userEmail", userEmail)
                .uniqueResultOptional();
    }

    public List<User> getAllByIds(List<UUID> ids) {

        return getSession().createQuery("SELECT u " +
                "FROM User u " +
                "WHERE u.userId in (:ids)", User.class )
            .setParameterList("ids", ids)
            .getResultList();
    }

    @Transactional(readOnly = true)
    public List<User> findAllByLeadId(String managerId) {
        return getSession().createQuery(
                "SELECT u FROM User u " +
                        " WHERE u.leader = :managerId " +
                        "", User.class)
                .setParameter("managerId", managerId)
                .getResultList();
    }
}
