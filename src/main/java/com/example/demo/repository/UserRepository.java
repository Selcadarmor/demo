package com.example.demo.repository;

import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
        private EntityManager em;

        @Transactional
        public void save(User user) {
            if(user.getId() == null){
                em.persist(user);
            } else {
                em.merge(user);
            }
        }
        @Transactional
        public User findById(Long id) {
            return em.find(User.class, id);
        }
        @Transactional
        public void delete( Long id) {
            User user = em.find(User.class, id);
            if(user != null) {
                em.remove(em.merge(user));;
            }
        }
        @Transactional
        public void update (User user) {
            em.merge(user);
        }
        @Transactional
        public List<User> findAllUser(){
            return em.createQuery(" FROM User ", User.class).getResultList();
        }
}
